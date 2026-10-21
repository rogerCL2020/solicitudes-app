package com.dinet.solicitudes.infrastructure.output.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("solicitud_contacto")
public record SolicitudContactoR2dbcEntity(
        @Id Long id,
        Long solicitudId,
        String nombreContacto,
        String numeroContacto
) {}