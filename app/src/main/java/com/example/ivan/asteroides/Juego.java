package com.example.ivan.asteroides;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Ivan on 6/10/16.
 */


public class Juego extends Activity {
    private VistaJuego vistaJuego;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);
        vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
    }

    @Override protected void onPause() {
        super.onPause();
        vistaJuego.desactivarSensores();
        vistaJuego.getThread().pausar();

    }
    @Override protected void onResume() {
        super.onResume();
        vistaJuego.activarSensores();
        vistaJuego.getThread().reanudar();

    }
    @Override protected void onDestroy() {
        super.onDestroy();
        vistaJuego.getThread().detener();
        }
}