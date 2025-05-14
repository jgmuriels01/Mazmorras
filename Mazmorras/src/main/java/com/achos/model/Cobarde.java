package com.achos.model;

import com.achos.enums.TipoPersonaje;

public class Cobarde extends Enemigo{

    public Cobarde(String nombre, int velocidad, int fuerza, TipoPersonaje tipoPersonaje, int percepcion) {
        super(nombre, velocidad, fuerza, tipoPersonaje, percepcion);
    }
    
}
