package com.achos.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

import com.achos.enums.TipoPersonaje;
import com.achos.interfaces.Observer;
import com.achos.utilities.LectorPersonajes;
import com.achos.utilities.Posicion;

/**
 * Clase Partida
 * 
 * Esta clase representa una partida del juego. Se encarga de gestionar los
 * personajes, el mapa y las interacciones entre ellos.
 * 
 */
public class Partida {
    private static Partida instance;
    private static int nivelPartida = 1;
    private TreeSet<Personaje> personajes;
    private String pathPersonajes = "Mazmorras/src/main/resources/com/achos/data/personajes.json";
    private Heroe heroe;
    private static boolean partidaNueva = true;
    private static int fuerza = 0;
    private static int velocidad = 0;
    private int[][] spawn = new int[][] { { 13, 1 }, { 5, 13 }, { 8, 1 }, { 1, 13 }, { 13, 6 } };
    private String nombreMapa;
    private Mapa mapa;
    private boolean gameOver;
    private boolean victory;
    private int recompensa = 1;
    private Random random = new Random();

    private ArrayList<Observer> observers = new ArrayList<>();

    /**
     * Constructor privado.
     * 
     * @param observer
     */
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        observers.forEach(i -> i.onChange());
    }

    public void resetear() {
        instance = null;
    }

    private Partida() {
        gameOver = false;
        victory = false;
        personajes = LectorPersonajes.leerPersonajes(pathPersonajes);
        buscarHeroe();
        if (!partidaNueva) {
            heroe.setFuerza(fuerza);
            heroe.setVelocidad(velocidad);
        }
        nombreMapa = creadorNombre();
        mapa = new Mapa(nombreMapa);
        if (mapa.getCeldas() == null || mapa.getCeldas().isEmpty()) {
            throw new IllegalStateException("El mapa no se cargó correctamente.");
        }
        personajesToSpawn();
        subirNivelPersonajes();

    }

    private String creadorNombre() {
        return "mapa" + nivelPartida;
    }

    public static Partida getInstance() {
        if (instance == null) {
            instance = new Partida();
        }
        return instance;
    }

    /* Getters and setters */
    public TreeSet<Personaje> getPersonajes() {
        return personajes;
    }

    public void setPersonajes(TreeSet<Personaje> personajes) {
        this.personajes = personajes;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public int[][] getSpawn() {
        return spawn;
    }

    /* Encuentra y guarda el Heroe de la lista de personajes */
    public void buscarHeroe() {
        for (Personaje personaje : personajes) {
            if (personaje instanceof Heroe) {
                this.heroe = (Heroe) personaje;
            }
        }
    }

    public Heroe getHeroe() {
        return heroe;
    }

    public static void setFuerza(int fuerza) {
        Partida.fuerza = fuerza;
    }

    public static void setVelocidad(int velocidad) {
        Partida.velocidad = velocidad;
    }

    public static void setPartidaNueva(boolean partidaNueva) {
        Partida.partidaNueva = partidaNueva;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        notifyObservers();
    }

    public boolean getVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
        notifyObservers();
    }

    public int getNivelPartida() {
        return nivelPartida;
    }

    public void subirNivelPartida() {
        if (nivelPartida >= 3) {
            nivelPartida = 1;
        } else {
            nivelPartida++;
        }
    }

    public void resetearNivelPartida() {
        nivelPartida = 1;
    }

    public void subirNivelPersonajes() {
        for (Personaje personaje : personajes) {
            if (personaje instanceof Enemigo) {
                personaje.setFuerza(personaje.getFuerza() + getNivelPartida());
            }
        }
    }

    /* Asignar personajes a sus celdas spawn */
    public void personajesToSpawn() {
        buscarCelda(spawn[0]).setOcupadoPor(buscarPersonaje(TipoPersonaje.PABLO));
        buscarPersonaje(TipoPersonaje.PABLO).setPosicion(spawn[0]);
        for (int i = 1; i < spawn.length; i++) {
            for (Personaje personaje : personajes) {
                if (personaje instanceof Enemigo) {
                    switch (i) {
                        case 1:
                            if (personaje.getTipoPersonaje() == TipoPersonaje.MANU) {
                                buscarCelda(spawn[i]).setOcupadoPor(personaje);
                                personaje.setPosicion(spawn[i]);
                            }
                            break;
                        case 2:
                            if (personaje.getTipoPersonaje() == TipoPersonaje.GLORIA) {
                                buscarCelda(spawn[i]).setOcupadoPor(personaje);
                                personaje.setPosicion(spawn[i]);
                            }
                            break;
                        case 3:
                            if (personaje.getTipoPersonaje() == TipoPersonaje.GABINO) {
                                buscarCelda(spawn[i]).setOcupadoPor(personaje);
                                personaje.setPosicion(spawn[i]);
                            }
                            break;
                        case 4:
                            if (personaje.getTipoPersonaje() == TipoPersonaje.COBARDE) {
                                buscarCelda(spawn[i]).setOcupadoPor(personaje);
                                personaje.setPosicion(spawn[i]);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        /*
         * buscarCelda(spawn[1]).setOcupadoPor(buscarPersonaje(TipoPersonaje.MANU));
         * buscarPersonaje(TipoPersonaje.MANU).setPosicion(spawn[1]);
         * buscarCelda(spawn[2]).setOcupadoPor(buscarPersonaje(TipoPersonaje.GLORIA));
         * buscarPersonaje(TipoPersonaje.GLORIA).setPosicion(spawn[2]);
         * buscarCelda(spawn[3]).setOcupadoPor(buscarPersonaje(TipoPersonaje.GABINO));
         * buscarPersonaje(TipoPersonaje.GABINO).setPosicion(spawn[3]);
         */
    }

    /**
     * Buscar personaje por tipo. Sirve solo para buscar a un unico Heroe. Si buscas
     * a un Enemigo, te devolverá el primer Enemigo encontrado.
     * 
     * @param tipoPersonaje
     * @return de tipo Personaje
     */
    public Personaje buscarPersonaje(TipoPersonaje tipoPersonaje) {
        Personaje personajeEncontrado = null;
        for (Personaje personaje : personajes) {
            if (personaje.getTipoPersonaje() == tipoPersonaje) {
                personajeEncontrado = personaje;
            }
        }
        return personajeEncontrado;
    }

    /**
     * Busca una celda en el mapa por su posicion. Si la posicion no es valida,
     * devuelve
     * null.
     * 
     * @param posicion
     * @return de tipo Celda
     */
    public Celda buscarCelda(int[] posicion) {
        Celda celdaEncontrada = null;
        if (Posicion.dentroLimites(posicion, mapa)) {
            celdaEncontrada = mapa.getCeldas().get(posicion[0]).get(posicion[1]);
        }
        return celdaEncontrada;
    }

    /**
     * Aplica movimiento por orden de velocidad a todos los personajes
     * 
     * @param posicion
     */
    public void moverPersonajes(int[] posicion) {
        ArrayList<Personaje> personajesCopia = new ArrayList<>(personajes);
        for (int i = 0; i < personajesCopia.size(); i++) {
            if (personajesCopia.get(i) instanceof Heroe) {
                moverHeroe(posicion);
            } else if (personajesCopia.get(i) instanceof Cobarde) {
                Cobarde cobarde = (Cobarde) personajesCopia.get(i);
                moverCobarde(cobarde);
            } else {

                Enemigo enemigo = (Enemigo) personajesCopia.get(i);
                moverEnemigo(enemigo);
            }
        }
        personajes = new TreeSet<>(personajesCopia);
        notifyObservers();

    }

    /**
     * Mueve el heroe a la nueva posicion. Si la nueva posicion es un enemigo, el
     * enemigo pierde vida.
     * 
     * @param movimiento
     */
    public void moverHeroe(int[] movimiento) {
        int[] nuevaPosicion = Posicion.mover(heroe.getPosicion(), movimiento);
        if (Posicion.dentroLimites(nuevaPosicion, mapa)) {
            if (Posicion.noPared(nuevaPosicion, mapa)) {
                if (buscarCelda(nuevaPosicion).getOcupadoPor() instanceof Enemigo) {
                    buscarCelda(nuevaPosicion).getOcupadoPor().perderVida(heroe.atacar());
                    System.out.println("Heroe ataca a " + buscarCelda(nuevaPosicion).getOcupadoPor().getNombre());
                    if (!(buscarCelda(nuevaPosicion).getOcupadoPor().getVida() > 0)) {
                        buscarCelda(nuevaPosicion).setOcupadoPor(null);
                        heroe.setVida(heroe.getVida() + recompensa);
                        System.out.println("Heroe recupera " + recompensa + " puntos de vida");
                        if (victoria()) {
                            setVictory(true);
                        }
                    }
                } else {
                    buscarCelda(heroe.getPosicion()).setOcupadoPor(null);
                    heroe.setPosicion(nuevaPosicion);
                    buscarCelda(heroe.getPosicion()).setOcupadoPor(heroe);
                    System.out.println("Heroe se mueve a " + heroe.getPosicion()[0] + "," + heroe.getPosicion()[1]);
                }
            }

        }
    }

    /**
     * Mueve el enemigo a la nueva posicion. Si la nueva posicion es el heroe, el
     * heroe pierde vida.
     * 
     * @param enemigo
     */
    public void moverEnemigo(Enemigo enemigo) {
        if (enemigo.getVida() > 0) {
            if (Posicion.distancia(heroe.getPosicion(), enemigo.getPosicion()) <= enemigo.getPercepcion()) {
                System.out.print(enemigo.getNombre() + " ve al Heroe. ");
                ArrayList<int[]> cruceta = Posicion.crearCruceta(enemigo.getPosicion());
                Posicion.limpiarFueraLimites(cruceta, mapa);
                Posicion.limpiarMuro(cruceta, mapa);
                int[] posicionMasCerca = Posicion.posicionMasCerca(heroe.getPosicion(), cruceta);
                if (buscarCelda(posicionMasCerca).getOcupadoPor() != null) {
                    if (Arrays.equals(posicionMasCerca, heroe.getPosicion())) {
                        heroe.perderVida(enemigo.atacar());
                        System.out.println(enemigo.getNombre() + " ataca a Heroe");
                        if (gameOver()) {
                            setGameOver(true);
                        }
                    }
                } else {
                    buscarCelda(enemigo.getPosicion()).setOcupadoPor(null);
                    enemigo.setPosicion(posicionMasCerca);
                    buscarCelda(enemigo.getPosicion()).setOcupadoPor(enemigo);
                    System.out.println(enemigo.getNombre() + " se mueve a " + enemigo.getPosicion()[0] + ","
                            + enemigo.getPosicion()[1]);
                }
            } else {
                System.out.print(enemigo.getNombre() + " no ve al Heroe. ");
                ArrayList<int[]> cruceta = Posicion.crearCruceta(enemigo.getPosicion());
                Posicion.limpiarFueraLimites(cruceta, mapa);
                Posicion.limpiarMuro(cruceta, mapa);
                int numPosicion = random.nextInt(cruceta.size());
                if (buscarCelda(cruceta.get(numPosicion)).getOcupadoPor() == null) {
                    buscarCelda(enemigo.getPosicion()).setOcupadoPor(null);
                    enemigo.setPosicion(cruceta.get(numPosicion));
                    buscarCelda(enemigo.getPosicion()).setOcupadoPor(enemigo);
                    System.out.println(enemigo.getNombre() + " se mueve a " + enemigo.getPosicion()[0] + ","
                            + enemigo.getPosicion()[1]);
                }

            }

        }

    }

    public void moverCobarde(Cobarde cobarde) {
        if (cobarde.getVida() > 0) {
            if (Posicion.distancia(heroe.getPosicion(), cobarde.getPosicion()) <= cobarde.getPercepcion()) {
                System.out.print(cobarde.getNombre() + " ve al Heroe. ");
                ArrayList<int[]> cruceta = Posicion.crearCruceta(cobarde.getPosicion());
                Posicion.limpiarFueraLimites(cruceta, mapa);
                Posicion.limpiarMuro(cruceta, mapa);
                int[] posicionMasLejos = Posicion.posicionMasLejos(heroe.getPosicion(), cruceta);
                if (buscarCelda(posicionMasLejos).getOcupadoPor() != null) {
                    if (Arrays.equals(posicionMasLejos, heroe.getPosicion())) {
                        heroe.perderVida(cobarde.atacar());
                        System.out.println(cobarde.getNombre() + " ataca a Heroe");
                        if (gameOver()) {
                            setGameOver(true);
                        }
                    }
                } else {
                    buscarCelda(cobarde.getPosicion()).setOcupadoPor(null);
                    cobarde.setPosicion(posicionMasLejos);
                    buscarCelda(cobarde.getPosicion()).setOcupadoPor(cobarde);
                    System.out.println(cobarde.getNombre() + " se mueve a " + cobarde.getPosicion()[0] + ","
                            + cobarde.getPosicion()[1]);
                }
            } else {
                System.out.print(cobarde.getNombre() + " no ve al Heroe. ");
                ArrayList<int[]> cruceta = Posicion.crearCruceta(cobarde.getPosicion());
                Posicion.limpiarFueraLimites(cruceta, mapa);
                Posicion.limpiarMuro(cruceta, mapa);
                int numPosicion = random.nextInt(cruceta.size());
                if (buscarCelda(cruceta.get(numPosicion)).getOcupadoPor() == null) {
                    buscarCelda(cobarde.getPosicion()).setOcupadoPor(null);
                    cobarde.setPosicion(cruceta.get(numPosicion));
                    buscarCelda(cobarde.getPosicion()).setOcupadoPor(cobarde);
                    System.out.println(cobarde.getNombre() + " se mueve a " + cobarde.getPosicion()[0] + ","
                            + cobarde.getPosicion()[1]);
                }

            }

        }

    }

    /**
     * Game Over si el heroe tiene vida menor o igual a cero
     * 
     * @return true si el heroe tiene vida menor o igual a cero
     */
    public boolean gameOver() {
        if (heroe.getVida() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Victoria es true por defecto a menos que algun personaje enemigo tenga vida
     * mayor de 0
     */
    public boolean victoria() {
        for (Personaje personaje : personajes) {
            if (personaje instanceof Enemigo && personaje.getVida() > 0) {
                return false;
            }
        }
        return true;
    }

    private void testeo() {
        Scanner sc = new Scanner(System.in);
        String mov;
        boolean flag = true;
        int[] posicion = new int[2];
        System.out.println(getMapa().toString());
        System.out.println(this.getPersonajes().toString());
        while (flag) {
            System.out.print("Movimiento: ");
            mov = sc.nextLine();
            switch (mov) {
                case "a":
                    posicion = new int[] { 0, -1 };
                    break;
                case "w":
                    posicion = new int[] { -1, 0 };
                    break;
                case "s":
                    posicion = new int[] { 1, 0 };
                    break;
                case "d":
                    posicion = new int[] { 0, 1 };
                    break;

                default:
                    flag = false;
                    break;
            }
            moverPersonajes(posicion);
            System.out.println(getMapa().toString());
            System.out.println(this.getPersonajes().toString());
        }
        sc.close();
    }

    /**
     * Main para testeo
     * 
     * @param args
     */
    public static void main(String[] args) {
        Partida partida = new Partida();
        partida.testeo();

    }
}
