package com.dinet.solicitudes.domain.model;

public record SolicitudContacto(
        Long id,
        Long solicitudId,
        String nombreContacto,
        String numeroContacto
) {
}