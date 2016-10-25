package com.example.ivan.asteroides;

import java.util.Vector;

/**
 * Created by Ivan on 5/10/16.
 */

public interface AlmacenPuntuaciones {
    public void guardarPuntuacion(int puntos,String nombre,long fecha);
    public Vector<String> listaPuntuaciones(int cantidad);
}
