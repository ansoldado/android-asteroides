package com.example.ivan.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {


    public static AlmacenPuntuaciones almacen= new AlmacenPuntuacionesArray();
    private Button bAcercaDe;
    public MediaPlayer mp;

   //private ThreadMain thread = new ThreadMain();
    ///////private VistaJuego vistaJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asteroides);
        bAcercaDe = (Button) findViewById(R.id.button3);
        bAcercaDe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarAcercaDe(null);
            }
        });
        mp = MediaPlayer.create(this, R.raw.audio);
        mp.start();



        //ciclo de vida de una actividad
        //Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

//animacion texto
        TextView texto = (TextView) findViewById(R.id.Asteroides);
        Animation animacion = AnimationUtils.loadAnimation(this, R.anim.giro_con_zoom);
        texto.startAnimation(animacion);
//animación boton jugar
        Button boton0Anim = (Button) findViewById(R.id.button);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.aparecer);
        boton0Anim.startAnimation(animation1);
        //animación segundo boton
        Button boton2Anim = (Button) findViewById(R.id.button2);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.traslacion);
        boton2Anim.startAnimation(animation2);
        //animacion tercer boton
        Button boton3Anim = (Button) findViewById(R.id.button3);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.zoom_agrandar);
        boton3Anim.startAnimation(animation3);
        //animacion cuarto boton
        Button boton4Anim = (Button) findViewById(R.id.button4);
        Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.aparecer_trasladado);
        boton4Anim.startAnimation(animation4);




    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mi_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }
        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        }
        return super.onOptionsItemSelected(item); }

    public void lanzarAcercaDe (View view) {
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

    public void lanzarPreferencias (View view) {
        Intent i = new Intent(this, Preferencias.class);
        startActivity(i);
        mp.pause();
    }

    public void lanzarPuntuaciones(View view) {
        Intent i = new Intent(this, Puntuaciones.class);
        startActivity(i);
        mp.pause();
    }

    public void lanzarJuego(View view) {
        Intent i = new Intent(this, Juego.class);
        startActivity(i);
        mp.pause();
    }


    public void mostrarPreferencias(View view){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = "música: " + pref.getBoolean("musica",true) +", gráficos: " + pref.getString("graficos","?")+
                "multijugador: " + pref.getBoolean("multijugador", false) +", jugadoresmax: " + pref.getString("jugadoresmax", "?")+
                "tipo conexion: " + pref.getString("tipo conexion", "?")+ "fragmentos: " + pref.getString("fragmentos", "?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


/*
    //ciclo de vida de una actividad
    @Override protected void onStart() { super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show(); }

    @Override protected void onResume() { super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show(); }
    @Override protected void onPause() {
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        super.onPause(); }

    @Override protected void onStop() { super.onStop();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show(); }
    @Override protected void onRestart() { super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show(); }
    @Override protected void onDestroy() { super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show(); }
        */



    //////////trabajando aqui


   /* class ThreadMain extends Thread {
        private boolean pausado, corriendo;

        public synchronized void pausar() {
            pausado = true;
        }
        public synchronized void restablecer() {
            pausado = false;
            notify();
        }
        public void detener() {
            corriendo = false;
            if (pausado) restablecer();
        }
        @Override public void run() {
            corriendo = true;
            while (corriendo) {
                mp.start();
                synchronized (this) {
                    while (pausado) {
                        try {
                            mp.pause();

                        } catch (Exception e) {mp.stop(); }
                    }
                }
            }
        }
    }

    public ThreadMain getThread() {
        return thread;
    }


    @Override protected void onPause() {
        super.onPause();
        vistaJuego.getThread().pausar();

    }
    @Override protected void onResume() {
        super.onResume();
        vistaJuego.getThread().reanudar();

    }*/
    @Override protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }

    @Override protected void onSaveInstanceState(Bundle estadoGuardado){
        super.onSaveInstanceState(estadoGuardado);
        if (mp != null) {
            int pos = mp.getCurrentPosition();
            estadoGuardado.putInt("posicion", pos); }
    }
    @Override protected void onRestoreInstanceState(Bundle estadoGuardado){
        super.onRestoreInstanceState(estadoGuardado);
        if (estadoGuardado != null && mp != null) {
            int pos = estadoGuardado.getInt("posicion");
            mp.seekTo(pos);
        }
    }
}
