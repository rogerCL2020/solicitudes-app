package com.dinet.solicitudes.domain.model;

import java.time.LocalDate;
import java.util.List;

public record SolicitudDetalle(
        Long id,
        String codigo,
        String marca,
        String tipoSolicitud,
        LocalDate fechaEnvio,
        String nombreContacto,
        String numeroContacto,
        List<SolicitudContacto> contactos
) {}