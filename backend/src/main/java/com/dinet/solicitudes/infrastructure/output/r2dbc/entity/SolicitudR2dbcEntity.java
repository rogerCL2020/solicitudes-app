package com.dinet.solicitudes.infrastructure.output.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("solicitud")
public record SolicitudR2dbcEntity(
        @Id Long id,
        String codigo,
        String marca,
        String tipoSolicitud,
        LocalDate fechaEnvio,
        String nombreContacto,
        String numeroContacto
) {}