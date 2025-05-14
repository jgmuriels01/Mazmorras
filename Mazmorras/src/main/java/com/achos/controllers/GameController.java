package com.achos.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import com.achos.SceneID;
import com.achos.SceneManager;
import com.achos.enums.TipoCelda;
import com.achos.interfaces.Observer;
import com.achos.model.Celda;
import com.achos.model.Enemigo;
import com.achos.model.Heroe;
import com.achos.model.Partida;
import com.achos.model.Personaje;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Controlador de la pantalla de juego
 */
public class GameController implements Observer {

    @FXML
    private StackPane mainStackPane;

    @FXML
    private ImageView background;

    @FXML
    private VBox infoBox;

    @FXML
    private AnchorPane juego;

    @FXML
    private AnchorPane info;

    @FXML
    private SplitPane pantalla;

    private final int tileSize = 40;

    // Usamos las rutas relativas para las imágenes
    private final String URL_SUELO = "/com/achos/images/suelo.png";
    private final String URL_PARED = "/com/achos/images/paredes.png";

    private Partida partida = Partida.getInstance(); // Obtener la partida actual

    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {

        partida.subscribe(this);
        generarMapa(); // Generar el mapa al iniciar la pantalla
        System.out.println("inicializando mapa");
        generarInfoPersonajes();

        juego.setOnMouseClicked(e -> juego.requestFocus());
        juego.setOnKeyPressed(e -> teclaToMovimiento(e));
    }

    /**
     * Genera el mapa a partir de la partida
     */
    private void generarMapa() {
        ArrayList<ArrayList<Celda>> celdas = partida.getMapa().getCeldas(); // Obtener las celdas del mapa

        juego.getChildren().clear(); // Limpiar el juego antes de añadir el nuevo mapa

        // Rellenar grid panel
        GridPane gridPane = new GridPane(); // Crear un GridPane para el mapa
        gridPane.setHgap(0); // Espacio horizontal entre celdas
        gridPane.setVgap(0); // Espacio vertical entre celdas
        // Rellenar grid panel
        for (int fila = 0; fila < celdas.size(); fila++) { // Recorrer las filas
            for (int columna = 0; columna < celdas.get(fila).size(); columna++) { // Recorrer las filas y columnas
                StackPane stackPane = crearCeldaVisual(celdas.get(fila).get(columna)); // Crear la celda visual
                gridPane.add(stackPane, columna, fila); // Añadir la celda al GridPane
            }
        }

        juego.getChildren().add(gridPane); // Añadir el GridPane al juego
    }

    /**
     * Crea una celda visual a partir de la celda del mapa
     * 
     * @param celda
     * @return
     */
    private StackPane crearCeldaVisual(Celda celda) {
        StackPane stackPane = new StackPane(); // Crear un StackPane para la celda
        stackPane.setPrefSize(tileSize, tileSize); // Establecer el tamaño preferido
        stackPane.setAlignment(Pos.CENTER); // Centrar el contenido

        ImageView fondo = new ImageView(); // Crear un ImageView para el fondo
        fondo.setFitWidth(tileSize); // Establecer el ancho del fondo
        fondo.setFitHeight(tileSize); // Establecer la altura del fondo

        if (celda.getTipoCelda() == TipoCelda.PARED) { // Si es una pared
            fondo.setImage(new Image(getClass().getResource("/com/achos/images/Paredes.png").toExternalForm()));
        } else if (celda.getTipoCelda() == TipoCelda.SUELO) { // Si es un suelo
            fondo.setImage(new Image(getClass().getResource("/com/achos/images/suelo.png").toExternalForm()));
        }
        stackPane.getChildren().add(fondo); // Añadir el fondo al StackPane

        Personaje p = celda.getOcupadoPor(); // Obtener el personaje que ocupa la celda
        // Si hay un personaje en la celda, añadir su imagen
        if (p != null && p.getVida() > 0) {
            ImageView personajeView = new ImageView(); // Crear un ImageView para el personaje
            personajeView.setFitWidth(tileSize); // Establecer el ancho del personaje
            personajeView.setFitHeight(tileSize); // Establecer la altura del personaje
            personajeView.setImage(new Image(getClass().getResource(obtenerImagenPersonaje(p)).toExternalForm())); // Usamos
                                                                                                                   // la
                                                                                                                   // ruta
                                                                                                                   // relativa
            stackPane.getChildren().add(personajeView); // Añadir el personaje al StackPane
        }

        return stackPane; // Devolver el StackPane con la celda
    }

    /**
     * Devuelve la URL de la imagen del personaje
     * 
     * @param p
     * @return
     */
    private String obtenerImagenPersonaje(Personaje p) {
        if (p instanceof Heroe) {
            return "/com/achos/images/pablo-cenital.png";
        } else if (p instanceof Enemigo) {
            switch (p.getTipoPersonaje()) {
                case GABINO:
                    return "/com/achos/images/gabino-cenital.png";
                case MANU:
                    return "/com/achos/images/manu-cenital.png";
                case GLORIA:
                    return "/com/achos/images/gloria-cenital.png";
                case COBARDE:
                    return "/com/achos/images/cobarde.png";
                default:
                    return null;
            }
        }
        return null; // Si no es un personaje conocido
    }

    /**
     * Devuelve la URL de la imagen FRONTAL del personaje
     * 
     * @param p
     * @return
     */
    private String obtenerImagenFrontalPersonaje(Personaje p) {
        if (p instanceof Heroe) {
            return "/com/achos/images/pablo-frontal.png";
        } else if (p instanceof Enemigo) {
            switch (p.getTipoPersonaje()) {
                case GABINO:
                    return "/com/achos/images/gabino-frontal.png";
                case MANU:
                    return "/com/achos/images/manu-frontal.png";
                case GLORIA:
                    return "/com/achos/images/gloria-frontal.png";
                case COBARDE:
                    return "/com/achos/images/cobarde.png";
                default:
                    return null;
            }
        }
        return null; // Si no es un personaje conocido
    }

    /**
     * Mueve el personaje a la celda correspondiente
     * 
     * @param tecla
     */
    public void teclaToMovimiento(KeyEvent tecla) {
        int[] movimiento;
        KeyCode teclaTocodigo = tecla.getCode();
        System.out.print("\nTecla pulsada: ");
        switch (teclaTocodigo) {
            case A:
            case LEFT:
                movimiento = new int[] { 0, -1 };
                System.out.println("Izquierda");
                break;
            case W:
            case UP:
                movimiento = new int[] { -1, 0 };
                System.out.println("Arriba");
                break;
            case S:
            case DOWN:
                movimiento = new int[] { 1, 0 };
                System.out.println("Abajo");
                break;
            case D:
            case RIGHT:
                movimiento = new int[] { 0, 1 };
                System.out.println("Derecha");
                break;

            case G:
                movimiento = new int[] { 0, 0 }; // No se mueve
                System.out.println("Game Over forzado");
                forzarGameOver();
                break;

            case V:
                movimiento = new int[] { 0, 0 }; // No se mueve
                System.out.println("Victoria forzada");
                forzarVictoria();
                break;

            default:
                movimiento = new int[] { 0, 0 }; // No se mueve
                System.out.println("tecla no asignada");
                break;
        }
        partida.moverPersonajes(movimiento);
    }
    /**
     * Forzar el game over
     */
    private void forzarGameOver() {
        partida.getHeroe().setVida(0);
        partida.setGameOver(true);
    }
    /**
     * Forzar la victoria
     */
    private void forzarVictoria() {
        ArrayList<Personaje> personajesCopia = new ArrayList<>(partida.getPersonajes());
        for (Personaje personaje : personajesCopia) {
            if (personaje instanceof Enemigo) {
                personaje.setVida(0);
            }
        }
        partida.setPersonajes(new TreeSet<>(personajesCopia));
        partida.setVictory(true);
    }

    /**
     * refresca los cambios
     */
    @Override
    public void onChange() {
        System.out.println("Nivel partida: " + partida.getNivelPartida());
        readGameOver();
        readVictory();
        generarMapa();
        generarInfoPersonajes();
    }

    /**
     * Genera la información de los personajes en la interfaz
     */
    private void generarInfoPersonajes() {
        //infoBox es una VBox creada desde SceneBuilder
        infoBox.getChildren().clear();

        for (Personaje p : partida.getPersonajes()) {
            // HBox es el contenedor de cada personaje y está formado por
            // una imageview con la foto del personaje (FRONTAL) y una VBOX
            // con sus datos (datosBox)
            HBox personajeBox = new HBox(5); 
            personajeBox.setAlignment(Pos.CENTER);

            //Si es un heroe coge los stylos de heroe-box para ser rojo
            if (p instanceof Heroe) {
                personajeBox.getStyleClass().add("heroe-box");
                //Si NO es un heroe coge los stylos de personaje-box para ser azul
            } else
                personajeBox.getStyleClass().add("personaje-box");

            // Imagen del personaje
            ImageView img = new ImageView(
                    new Image(getClass().getResource(obtenerImagenFrontalPersonaje(p)).toExternalForm()));
            img.setFitWidth(40);
            img.setFitHeight(60);

            // VBox datos personaje
            VBox datosBox = new VBox(10);

            //Dentro de datosBox hay 3 HBox:

            // HBox 1: icono vida + barra de Vida
            ImageView vidaIcon = new ImageView(
                    new Image(getClass().getResource("/com/achos/images/vida.png").toExternalForm()));
            vidaIcon.setFitWidth(20);
            vidaIcon.setFitHeight(20);

            ProgressBar vidaBar = new ProgressBar(p.getVida() / 10.0);

            //Añade todo al HBOX1:
            HBox vidaBox = new HBox(5, vidaIcon, vidaBar);
            vidaBox.setAlignment(Pos.CENTER);

            // HBox 2: icono fuerza + barra de Fuerza
            ImageView fuerzaIcon = new ImageView(
                    new Image(getClass().getResource("/com/achos/images/fuerza.png").toExternalForm()));
            fuerzaIcon.setFitWidth(20);
            fuerzaIcon.setFitHeight(20);

            ProgressBar fuerzaBar = new ProgressBar(p.getFuerza() / 10.0);

            //Añade todo al HBOX2:
            HBox fuerzaBox = new HBox(5, fuerzaIcon, fuerzaBar);
            vidaBox.setAlignment(Pos.CENTER);

            // HBox 3: icono velocidad + barra de velocidad
            ImageView velocidadIcon = new ImageView(
                    new Image(getClass().getResource("/com/achos/images/velocidad.png").toExternalForm()));
            velocidadIcon.setFitWidth(20);
            velocidadIcon.setFitHeight(20);

            ProgressBar velocidadBar = new ProgressBar(p.getVelocidad() / 10.0);

            //Añade todo al HBOX3:
            HBox velocidadBox = new HBox(5, velocidadIcon, velocidadBar);
            velocidadBox.setAlignment(Pos.CENTER);

            // Opacidad si está muerto
            if (p.getVida() <= 0) {
                personajeBox.setOpacity(0.3);
            }

            // Añadir los 3 HBOX a datosBox:
            datosBox.getChildren().addAll(vidaBox, fuerzaBox, velocidadBox);

            // Añadir la imagen de personaje y datosBox a personajeBox(roja o azul)
            personajeBox.getChildren().addAll(img, datosBox);

            //Añadir todo al VBox creado desde Scene Builder con un spacing de 5
            infoBox.getChildren().add(personajeBox);
            infoBox.setSpacing(5);
        }
    }
    /**
     * Método que se encarga de cargar la escena de GameOver
     */
    private void readGameOver() {
        if (partida.getGameOver()) {
            try {
                SceneManager.getInstance().loadScene(SceneID.GAMEOVER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Método que se encarga de cargar la escena de Victoria
     */
    private void readVictory() {
        if (partida.getVictory()) {
            try {
                SceneManager.getInstance().loadScene(SceneID.VICTORY);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
