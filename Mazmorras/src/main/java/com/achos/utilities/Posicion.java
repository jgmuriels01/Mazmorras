package com.achos.utilities;

import java.util.ArrayList;

import com.achos.enums.TipoCelda;
import com.achos.model.Mapa;
/**
 * Clase Posicion
 * 
 * Esta clase contiene métodos para trabajar con posiciones en un mapa, como crear una cruceta de posiciones,
 * verificar si una posición está dentro de los límites del mapa, limpiar posiciones fuera de límites y
 * calcular distancias entre posiciones.
 */
public class Posicion {

    /**
     * A partir de una posicion crea un array de 5 posiciones (original, izquierda,
     * derecha, arriba y abajo)
     * 
     * @param posicion
     * @return regresa un array de 5 posiciones
     */
    public static ArrayList<int[]> crearCruceta(int[] posicion) {
        int fila = posicion[0];
        int columna = posicion[1];
        ArrayList<int[]> cruceta = new ArrayList<>();
        /* cruceta.add(new int[] { fila, columna }); // original */
        cruceta.add(new int[] { fila, columna - 1 }); // izquierda
        cruceta.add(new int[] { fila, columna + 1 }); // derecha
        cruceta.add(new int[] { fila - 1, columna }); // arriba
        cruceta.add(new int[] { fila + 1, columna }); // abajo
        return cruceta;
    }

    /**
     * Elimina posiciones de la cruceta que sean fuera del mapa
     * 
     * @param cruceta
     * @param mapa
     */
    public static void limpiarFueraLimites(ArrayList<int[]> cruceta, Mapa mapa) {
        for (int i = 0; i < cruceta.size(); i++) {
            if (!dentroLimites(cruceta.get(i), mapa)) {
                cruceta.remove(i);
                i--;
            }
        }
    }

    /**
     * Posicion dentro de los límities, SOLO MAPAS CUADRADOS
     * 
     * @param posicion
     * @param mapa
     * @return
     */
    public static boolean dentroLimites(int[] posicion, Mapa mapa) {
        int filas = mapa.getCeldas().size();
        int columnas = mapa.getCeldas().get(0).size();
        return (posicion[0] < filas && posicion[0] >= 0) && (posicion[1] < columnas && posicion[1] >= 0);
    }

    /**
     * Elimina posiciones de la cruceta de posiciones que colisionen con un
     * muro
     * 
     * @param cruceta
     * @param mapa
     */
    public static void limpiarMuro(ArrayList<int[]> cruceta, Mapa mapa) {
        for (int i = 0; i < cruceta.size(); i++) {
            if (!noPared(cruceta.get(i), mapa)) {
                cruceta.remove(i);
                i--;
            }
        }
    }

    /**
     * Posicion sin colision con pared
     * 
     * @param posicion
     * @param mapa
     * @return regresa true si la posicion no es una pared
     */
    public static boolean noPared(int[] posicion, Mapa mapa) {
        return mapa.getCeldas().get(posicion[0]).get(posicion[1]).getTipoCelda() != TipoCelda.PARED;
    }

    /**
     * Devuelve en double la distancia entre dos posiciones
     * 
     * @param posicion1
     * @param posicion2
     * @return regresa la distancia entre las dos posiciones
     */
    public static double distancia(int[] posicion1, int[] posicion2) {
        int fila1 = posicion1[0];
        int columna1 = posicion1[1];
        int fila2 = posicion2[0];
        int columna2 = posicion2[1];
        int distanciaFilas = Math.abs(fila1 - fila2);
        int distanciaColumnas = Math.abs(columna1 - columna2);
        return Math.sqrt(Math.pow(distanciaFilas, 2) + Math.pow(distanciaColumnas, 2));
    }

    /**
     * Devuelve la posicion más cercana de una cruceta a una posicion dada
     * 
     * @param posicion 
     * @param cruceta
     * @return regresa la posicion más cercana
     */
    public static int[] posicionMasCerca(int[] posicion, ArrayList<int[]> cruceta) {
        int[] posicionFinal = cruceta.get(0);
        for (int i = 1; i < cruceta.size(); i++) {
            if (distancia(posicion, cruceta.get(i)) < distancia(posicion, posicionFinal)) {
                posicionFinal = cruceta.get(i);
            }
        }
        return posicionFinal;
    }
    public static int[] posicionMasLejos(int[] posicion, ArrayList<int[]> cruceta) {
        int[] posicionFinal = cruceta.get(0);
        for (int i = 1; i < cruceta.size(); i++) {
            if (distancia(posicion, cruceta.get(i)) > distancia(posicion, posicionFinal)) {
                posicionFinal = cruceta.get(i);
            }
        }
        return posicionFinal;
    }

    /**
     * Mueve una posicion a otra posicion
     * 
     * @param posicionOriginal
     * @param movimiento
     * @return regresa la nueva posicion
     */
    public static int[] mover(int[] posicionOriginal, int[] movimiento) {
        return new int[] { posicionOriginal[0] + movimiento[0], posicionOriginal[1] + movimiento[1] };
    }
}
