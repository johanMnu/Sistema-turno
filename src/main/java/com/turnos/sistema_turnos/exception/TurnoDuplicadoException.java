package com.turnos.sistema_turnos.exception;

public class TurnoDuplicadoException extends RuntimeException{
    public TurnoDuplicadoException(String mensaje){
        super(mensaje);
    }
}
