package com.saludtotal.exceptions;

public class TurnoSuperpuestoException extends RuntimeException {
    public TurnoSuperpuestoException(String mensaje) {
        super(mensaje);
    }
}