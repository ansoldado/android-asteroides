package com.example.ivan.asteroides;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {


    public static AlmacenPuntuaciones almacen;
    private Button bAcercaDe1;
    public MediaPlayer mp;

    //private ThreadMain thread = new ThreadMain();
    ///////private VistaJuego vistaJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asteroides);
        bAcercaDe1 = (Button) findViewById(R.id.button3);
        bAcercaDe1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarAcercaDe(null);
            }
        });
        mp = MediaPlayer.create(this, R.raw.audio);


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getString("puntuaciones", "1").equals("0")) {
            Log.d("Puntuaciones", "Guardando en 0");
            almacen = new AlmacenPuntuacionesPreferencias(this);
        }
        if (pref.getString("puntuaciones", "2").equals("1")){
            Log.d("Puntuaciones", "Guardando en 1");
            almacen = new AlmacenPuntuacionesFicheroInterno(this);
        }
        if (pref.getString("puntuaciones", "3").equals("2")){
            Log.d("Puntuaciones", "Guardando en 2");
            almacen =new AlmacenPuntuacionesArray();
        }
        if (pref.getString("puntuaciones", "4").equals("3")){
            Log.d("Puntuaciones", "Guardando en memoria externa");
            almacen = new AlmacenPuntuacionesFicheroExterno(this);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mi_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }
        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lanzarAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

    public void lanzarPreferencias(View view) {
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
        Log.e("ALMACEN",almacen.toString());
        Intent i = new Intent(this, Juego.class);
        startActivityForResult(i, 1234);

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1234 && resultCode==RESULT_OK && data!=null) {
            int puntuacion = data.getExtras().getInt("puntuacion");
            String nombre = "Yo";
// Mejor leer nombre desde un AlertDialog.Builder o preferencias
            Log.e("ALMACEN 2",almacen.toString());
            almacen.guardarPuntuacion(puntuacion, nombre, System.currentTimeMillis());
            lanzarPuntuaciones(null);
        }
    }


    public void mostrarPreferencias(View view) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = "música: " + pref.getBoolean("musica", true) + ", gráficos: " + pref.getString("graficos", "?") +
                "multijugador: " + pref.getBoolean("multijugador", false) + ", jugadoresmax: " + pref.getString("jugadoresmax", "?") +
                "tipo conexion: " + pref.getString("tipo conexion", "?") + "fragmentos: " + pref.getString("fragmentos", "?");
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


    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp.start();
    }


    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado) {
        super.onSaveInstanceState(estadoGuardado);
        if (mp != null) {
            int pos = mp.getCurrentPosition();
            estadoGuardado.putInt("posicion", pos);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle estadoGuardado) {
        super.onRestoreInstanceState(estadoGuardado);
        if (estadoGuardado != null && mp != null) {
            int pos = estadoGuardado.getInt("posicion");
            mp.seekTo(pos);
        }
    }


}