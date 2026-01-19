package com.turnos.sistema_turnos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TurnoDuplicadoException.class)
    public ResponseEntity<?> manejarTurnoDuplicado(TurnoDuplicadoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(HorarioNoDisponibleException.class)
    public ResponseEntity<Map<String, String>> manejarHorarioNoDisponible(
            HorarioNoDisponibleException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", ex.getMessage()
                ));

    }
    @ExceptionHandler(DuracionInvalidaException.class)
    public ResponseEntity<Map<String,String>>manejarDuracionInvalida(
            DuracionInvalidaException ex
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error",ex.getMessage()
        ));
    }
    @ExceptionHandler(TurnoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarTurnoNoEncontrado(
            TurnoNoEncontradoException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", ex.getMessage()
                ));
    }

}
