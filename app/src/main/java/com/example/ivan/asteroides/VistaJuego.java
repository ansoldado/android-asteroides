package com.example.ivan.asteroides;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

/**
 * Created by Ivan on 6/10/16.
 */

public class VistaJuego extends View implements SensorEventListener {
    // //// ASTEROIDES //////
    private Vector<Grafico> asteroides; // Vector con los Asteroides
    private int numAsteroides = 5; // Número inicial de asteroides
    private int numFragmentos = 3; // Fragmentos en que se divide
    // //// NAVE //////
    private Grafico nave; // Gráfico de la nave
    private int giroNave; // Incremento de dirección
    private double aceleracionNave; // aumento de velocidad
    private static final int MAX_VELOCIDAD_NAVE = 20; // Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    // //// THREAD Y TIEMPO //////
    // Thread encargado de procesar el juego
    private ThreadJuego thread = new ThreadJuego();
    // Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;
    // Cuando se realizó el último proceso
    private long ultimoProceso = 0;

    //Controladores pantalla táctil
    private float mX=0, mY=0;
    private boolean disparo=false;

    private Context contextAux;
    //Sensores
    private boolean hayValorInicial = false;
    private float valorInicial;
    private boolean hayValorInicialA = false;
    private float valorInicialA;
    public Sensor accelerometerSensor;
    public  SensorManager mSensorManager = null;

    private Drawable drawableMisil;
    // //// MISIL //////
   // private Grafico misil;
    private Vector<Grafico> misiles;
    private static int PASO_VELOCIDAD_MISIL = 12;

    //private boolean misilActivo = false;
    //private int tiempoMisil;
    Vector<Double> tiempoMisiles = new Vector<Double>();


    // //// MULTIMEDIA //////
    SoundPool soundPool;
    int idDisparo, idExplosion;
    MediaPlayer mpDisparo, mpExplosion;

    //PUNTUACIONES
    private int puntuacion = 0;

    private Activity padre;


    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.contextAux = context;
        Drawable drawableNave, drawableAsteroide, drawableMisil;
        SharedPreferences pref0 = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (pref0.getString("graficos", "1").equals("0")) {
            Path pathNave = new Path();
            pathNave.moveTo((float) 0.0, (float) 0.0);
            pathNave.lineTo((float) 0.0, (float) 1.0);
            pathNave.lineTo((float) 1.0, (float) 0.5);
            pathNave.lineTo((float) 0.0, (float) 0.0);
            ShapeDrawable dNave = new ShapeDrawable(new PathShape(pathNave, 1, 1));
            dNave.getPaint().setColor(Color.WHITE);
            dNave.getPaint().setStyle(Paint.Style.STROKE);
            dNave.setIntrinsicWidth(20);
            dNave.setIntrinsicHeight(15);
            drawableNave = dNave;
            setBackgroundColor(Color.BLACK);
        } else {
            drawableNave = context.getResources().getDrawable(R.drawable.nave);
        }

       /*drawableNave = context.getResources().getDrawable(
                R.drawable.nave);*/
        SharedPreferences pref = PreferenceManager.
                getDefaultSharedPreferences(getContext());
        if (pref.getString("graficos", "1").equals("0")) {
            Path pathAsteroide = new Path();
            pathAsteroide.moveTo((float) 0.3, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.3);
            pathAsteroide.lineTo((float) 0.8, (float) 0.2);
            pathAsteroide.lineTo((float) 1.0, (float) 0.4);
            pathAsteroide.lineTo((float) 0.8, (float) 0.6);
            pathAsteroide.lineTo((float) 0.9, (float) 0.9);
            pathAsteroide.lineTo((float) 0.8, (float) 1.0);
            pathAsteroide.lineTo((float) 0.4, (float) 1.0);
            pathAsteroide.lineTo((float) 0.0, (float) 0.6);
            pathAsteroide.lineTo((float) 0.0, (float) 0.2);
            pathAsteroide.lineTo((float) 0.3, (float) 0.0);
            ShapeDrawable dAsteroide = new ShapeDrawable(
                    new PathShape(pathAsteroide, 1, 1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicWidth(50);
            dAsteroide.setIntrinsicHeight(50);
            drawableAsteroide = dAsteroide;
            setBackgroundColor(Color.BLACK);
        } else {
            drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
        }

        /*drawableAsteroide = context.getResources().getDrawable(
                R.drawable.asteroide1);*/
        SharedPreferences pref2 = PreferenceManager.
                getDefaultSharedPreferences(getContext());

        if (pref2.getString("graficos", "1").equals("0")) {
            ShapeDrawable dMisil = new ShapeDrawable(
                    new RectShape());
            dMisil.getPaint().setColor(Color.WHITE);
            dMisil.getPaint().setStyle(Paint.Style.STROKE);
            dMisil.setIntrinsicWidth(15);
            dMisil.setIntrinsicHeight(3);
            this.drawableMisil = dMisil;
        } else {
            ImageView misilAnimado =  new ImageView(contextAux);
            misilAnimado.setBackgroundResource(R.drawable.animacion);

            this.drawableMisil = (AnimationDrawable) misilAnimado.getBackground();



        }


        nave = new Grafico(this, drawableNave);
        misiles = new Vector<Grafico>();
        asteroides = new Vector<Grafico>();
        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }
        activarSensores();

        // MULTIMEDIA
        soundPool = new SoundPool( 5, AudioManager.STREAM_MUSIC , 0);
        idDisparo = soundPool.load(context, R.raw.disparo, 0);
        idExplosion = soundPool.load(context, R.raw.explosion, 0);

    }

    public void setPadre(Activity padre) {
        this.padre = padre;
    }

    protected void actualizaFisica() {
        long ahora = System.currentTimeMillis();
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            return; // Salir si el período de proceso no se ha cumplido.
        }
        // Para una ejecución en tiempo real calculamos retardo
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora;
        // Para la próxima vez
        // Actualizamos velocidad y dirección de la nave a partir de
        // giroNave y aceleracionNave (según la entrada del jugador)
        nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
        double nIncX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
        double nIncY = nave.getIncY() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
        // Actualizamos si el módulo de la velocidad no excede el máximo
        if (Math.hypot(nIncX, nIncY) <= MAX_VELOCIDAD_NAVE) {
            nave.setIncX(nIncX);
            nave.setIncY(nIncY);
        }
        nave.incrementaPos(retardo); // Actualizamos posición
        for (Grafico asteroide : asteroides) {
            asteroide.incrementaPos(retardo);
        }
        // Actualizamos posición de misil
        /*if (misilActivo) {
            misil.incrementaPos(retardo);
            tiempoMisil -= retardo;
            if (tiempoMisil < 0) {
                misilActivo = false;
            } else {
                for (int i = 0; i < asteroides.size(); i++)
                    if (misil.verificaColision(asteroides.elementAt(i))) {
                        destruyeAsteroide(i);
                        break;
                    }
            }
        }*/
        for(int i=0; i<misiles.size(); i++){
            Grafico misilAux = misiles.get(i);
            misilAux.incrementaPos(retardo);
            Double tiempoDeMisil = tiempoMisiles.get(i) - retardo;

            tiempoMisiles.set(i, tiempoDeMisil);

            for (int x = 0; x<asteroides.size(); x++){
                if(misilAux.verificaColision(asteroides.elementAt(x))){
                    destruyeAsteroide(x);
                    misiles.remove(i);
                    tiempoMisiles.remove(i);
                }
            }
        }
        for (Grafico asteroide : asteroides) {
            if (asteroide.verificaColision(nave)) {
                salir(); }
        }
    }


    private void salir() {
        Bundle bundle = new Bundle();
        bundle.putInt("puntuacion", puntuacion);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        padre.setResult(Activity.RESULT_OK, intent);
        padre.finish();
    }

    @Override protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
    // Una vez que conocemos nuestro ancho y alto.
        nave.setCenX(ancho/2);
        nave.setCenY(alto/2);
        for (Grafico asteroide: asteroides) {
            do {
                asteroide.setCenX((int) (Math.random()*ancho));
                asteroide.setCenY((int) (Math.random()*alto));
            } while(asteroide.distancia(nave) < (ancho+alto)/5);
        }
        ultimoProceso = System.currentTimeMillis();
        thread.start();
    }

    @Override protected void onDraw(Canvas canvas){
            super.onDraw(canvas);
            synchronized (asteroides) {
                for (Grafico asteroide : asteroides) {
                    asteroide.dibujaGrafico(canvas);
                }
            }
            nave.dibujaGrafico(canvas);
            //if (misiles.size() == 0) {
                //misil.dibujaGrafico(canvas);
            for(int i=0; i<misiles.size(); i++){
                if(tiempoMisiles.get(i)>0) {
                    misiles.get(i).dibujaGrafico(canvas);
                }else{
                    misiles.remove(i);
                    tiempoMisiles.remove(i);
                }
            }

        }

    private void destruyeAsteroide(int i) {
        synchronized (asteroides) {
            asteroides.remove(i);
            //misilActivo = false;
            soundPool.play(idExplosion, 1, 1, 0, 1, 2);
            puntuacion += 1000;
            if (asteroides.isEmpty()) {
                salir(); }
        }
    }

    /*

    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      ImageView rocketImage = (ImageView) findViewById(R.id.rocket_image);
      rocketImage.setBackgroundResource(R.drawable.rocket_thrust);
      rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
    }

     */
    private void activaMisil() {

        Grafico misil = new Grafico(this, drawableMisil);

        if(drawableMisil instanceof  AnimationDrawable){
            ((AnimationDrawable) drawableMisil).start();
        }

        misil.setCenX(nave.getCenX());
        misil.setCenY(nave.getCenY());
        misil.setAngulo(nave.getAngulo());
        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        //tiempoMisil = (int) Math.min(this.getWidth() / Math.abs(misil.getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
        //misilActivo = true;
        misiles.add(misil);
        tiempoMisiles.add(Math.min(this.getWidth() / Math.abs(misil.getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2);
        soundPool.play(idDisparo, 1, 1, 1, 1, 2);
}

    @Override
    public void onSensorChanged(SensorEvent event) {
        float valor = event.values[1];
        if (!hayValorInicial){
            valorInicial = valor;
            hayValorInicial = true;
        }
        giroNave=(int) (valor-valorInicial)/3 ;
        float valorA = event.values[2];
        if (!hayValorInicialA){
            valorInicialA = valor;
            hayValorInicialA = true;
        }
        aceleracionNave= (valorA-valorInicialA)/15;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    class ThreadJuego extends Thread {
        private boolean pausa,corriendo;
        public synchronized void pausar() {
            pausa = true; }
        public synchronized void reanudar() {
            pausa = false;
            notify(); }
        public void detener() {
            corriendo = false;
            if (pausa) reanudar(); }
        @Override public void run() {
            corriendo = true;
            while (corriendo) {
                actualizaFisica();
                synchronized (this) {
                    while (pausa) {
                        try {
                        wait();
                    } catch (Exception e) { }
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int codigoTecla, KeyEvent evento) {
        super.onKeyDown(codigoTecla, evento);
        // Suponemos que vamos a procesar la pulsación
        boolean procesada = true;
        switch (codigoTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = +PASO_ACELERACION_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                giroNave = -PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = +PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                activaMisil();
                break; default:
                // Si estamos aquí, no hay pulsación que nos interese
                procesada = false;
                break; }
        return procesada;
    }

    @Override public boolean onKeyUp(int codigoTecla, KeyEvent evento) {
        super.onKeyUp(codigoTecla, evento);
// Suponemos que vamos a procesar la pulsación
        boolean procesada = true;
        switch (codigoTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = 0;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                giroNave = 0;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = 0;
                break;
            default:
                procesada = false;
                break;
        }
        return procesada;
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                disparo=true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy<6 && dx>6){
                    giroNave = Math.round((x - mX) / 2);
                    disparo = false;
                } else if (dx<6 && dy>6){
                    aceleracionNave = Math.abs(Math.round((mY - y) / 25));
                    disparo = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                giroNave = 0;
                aceleracionNave = 0;
                if (disparo){
                    activaMisil();
                }
                break;
        }
        mX=x;
        mY=y;
        return true;
    }

    public ThreadJuego getThread() {
        return thread;
    }

    public void activarSensores(){
        SharedPreferences pref3 = PreferenceManager.
                getDefaultSharedPreferences(getContext());
        if (pref3.getString("controles", "0").equals("0")) {
            mSensorManager = (SensorManager) contextAux.getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (!listSensors.isEmpty()) {
                accelerometerSensor = listSensors.get(0);
                mSensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
            }
        }

    }



    public void  desactivarSensores() {
        if(mSensorManager != null)
            mSensorManager.unregisterListener(this);
    }




}
