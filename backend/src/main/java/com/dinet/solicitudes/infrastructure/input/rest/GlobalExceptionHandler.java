package com.dinet.solicitudes.infrastructure.input.rest;

import com.dinet.solicitudes.application.exception.SolicitudNoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SolicitudNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> manejarNoEncontrada(
            SolicitudNoEncontradaException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(
            WebExchangeBindException ex) {

        String mensaje = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Los datos enviados no son válidos");

        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", mensaje));
    }

    @ExceptionHandler(org.springframework.core.codec.DecodingException.class)
    public ResponseEntity<Map<String, String>> manejarErrorFormatoJson(
            org.springframework.core.codec.DecodingException ex) {

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "mensaje",
                        "El formato de la fecha es inválido. Use yyyy-MM-dd, por ejemplo 2026-10-21."
                ));
    }
}