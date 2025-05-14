package com.achos.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

import com.achos.enums.TipoPersonaje;
import com.achos.model.Cobarde;
import com.achos.model.Enemigo;
import com.achos.model.Heroe;
import com.achos.model.Personaje;

/**
 * Clase LectorPersonajes
 * 
 * Esta clase se encarga de leer un archivo JSON que representa personajes y
 * convertirlo en un conjunto de personajes.
 * Cada personaje puede ser un héroe o un enemigo, representados por las clases
 * Heroe y Enemigo respectivamente.
 */
public class LectorPersonajes {
    /**
     * Método que lee un archivo JSON y convierte su contenido en un conjunto de
     * personajes.
     * 
     * @param path Ruta del archivo JSON a leer.
     * @return TreeSet<Personaje> Conjunto de personajes leídos del archivo.
     */
    public static TreeSet<Personaje> leerPersonajes(String path) {
        File fichero = new File(path);
        TreeSet<Personaje> personajes = new TreeSet<>();

        String nombre = "";
        int velocidad = 0;
        int fuerza = 0;
        TipoPersonaje tipoPersonaje = null;
        int percepcion = 0;

        boolean construirPersonaje = false;

        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.startsWith("{")) {
                    construirPersonaje = true;
                }

                if (linea.startsWith("}")) {
                    construirPersonaje = false;
                    if (tipoPersonaje == TipoPersonaje.PABLO) {
                        personajes.add(new Heroe(nombre, velocidad, fuerza, tipoPersonaje));
                    } else if (tipoPersonaje == TipoPersonaje.COBARDE) {
                        personajes.add(new Cobarde(nombre, velocidad, fuerza, tipoPersonaje, percepcion));
                    } else {
                        personajes.add(new Enemigo(nombre, velocidad, fuerza, tipoPersonaje, percepcion));
                    }
                }

                if (construirPersonaje) {
                    String[] palabras = linea.split(":");
                    if (palabras.length < 2)
                        continue;

                    String clave = palabras[0].toLowerCase().trim().replace("\"", "");
                    String valor = palabras[1].trim().replace("\"", "").replace(",", "");

                    switch (clave) {
                        case "nombre":
                            nombre = valor;
                            switch (valor.toLowerCase()) {
                                case "pablo":
                                    tipoPersonaje = TipoPersonaje.PABLO;
                                    break;
                                case "manu":
                                    tipoPersonaje = TipoPersonaje.MANU;
                                    break;
                                case "gloria":
                                    tipoPersonaje = TipoPersonaje.GLORIA;
                                    break;
                                case "gabino":
                                    tipoPersonaje = TipoPersonaje.GABINO;
                                    break;
                                case "cobarde":
                                    tipoPersonaje = TipoPersonaje.COBARDE;
                            }
                            break;
                        case "velocidad":
                            velocidad = Integer.parseInt(valor);
                            break;
                        case "fuerza":
                            fuerza = Integer.parseInt(valor);
                            break;
                        case "percepcion":
                            percepcion = Integer.parseInt(valor);
                            break;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return personajes;
    }

    /**
     * Método principal para probar la lectura de personajes desde un archivo JSON.
     * 
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        String ruta = "Mazmorras/src/main/resources/com/achos/data/personajes.json";

        TreeSet<Personaje> personajes = LectorPersonajes.leerPersonajes(ruta);

        for (Personaje p : personajes) {
            System.out.println(p.toString());
        }
    }
}
