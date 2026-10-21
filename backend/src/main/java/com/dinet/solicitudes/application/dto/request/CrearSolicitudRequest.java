package com.dinet.solicitudes.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CrearSolicitudRequest(
        @NotBlank
        @Size(max = 100)
        String marca,

        @NotBlank
        @Size(max = 50)
        String tipoSolicitud,

        @NotNull
        LocalDate fechaEnvio,

        @NotBlank
        @Size(max = 150)
        String nombreContacto,

        @NotBlank
        @Pattern(regexp = "\\d{7,20}", message = "El número debe contener entre 7 y 20 dígitos")
        String numeroContacto,

        List<@Valid ContactoRequest> contactos
) {
    public record ContactoRequest(
            @NotBlank
            @Size(max = 150)
            String nombreContacto,

            @NotBlank
            @Pattern(regexp = "\\d{7,20}", message = "El número debe contener entre 7 y 20 dígitos")
            String numeroContacto
    ) {}
}