package com.taxitel.backend.exception;

// Al extender RuntimeException, le decimos a Java que este es un error que detendrá el flujo normal
public class RutaNoEncontradaException extends RuntimeException {

    // El constructor recibe el mensaje exacto de qué ruta falló
    public RutaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}