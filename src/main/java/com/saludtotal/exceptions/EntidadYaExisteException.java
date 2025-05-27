package com.saludtotal.exceptions;

public class EntidadYaExisteException extends RuntimeException {
    public EntidadYaExisteException(String mensaje) {
        super(mensaje);
    }
}
