package com.ipareventos.excepciones;

public class InscripcionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InscripcionException(String mensaje) {
        super(mensaje);
    }
}