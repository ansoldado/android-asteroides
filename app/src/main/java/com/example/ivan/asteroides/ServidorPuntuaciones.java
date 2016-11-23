package com.example.ivan.asteroides;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by Ivan on 22/11/16.
 */

public class ServidorPuntuaciones {
    public static void main(String args[]) {
        Vector<String> puntuaciones = new Vector<String>();
        try {
            ServerSocket s = new ServerSocket(1234);
            System.out.println("Esperando conexiones...");
            while (true) {
                Socket cliente = s.accept();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintWriter salida = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream()), true);
                String datos = entrada.readLine();
                if (datos.equals("PUNTUACIONES")) {
                    for (int n = 0; n < puntuaciones.size(); n++) {
                        salida.println(puntuaciones.get(n));
                    }
                } else {
                    puntuaciones.add(0, datos);
                    salida.println("OK");
                }
                cliente.close();
            }
        } catch (IOException e) {
            System.out.println(e); }
    }
}