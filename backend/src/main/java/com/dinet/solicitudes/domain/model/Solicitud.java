package com.dinet.solicitudes.domain.model;

import java.time.LocalDate;

public record Solicitud(
        Long id,
        String codigo,
        String marca,
        String tipoSolicitud,
        LocalDate fechaEnvio,
        String nombreContacto,
        String numeroContacto
) {
}