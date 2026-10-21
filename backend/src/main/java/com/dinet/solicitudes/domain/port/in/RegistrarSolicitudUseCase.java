package com.dinet.solicitudes.domain.port.in;

import com.dinet.solicitudes.domain.model.Solicitud;
import com.dinet.solicitudes.domain.model.SolicitudContacto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RegistrarSolicitudUseCase {

    Mono<Solicitud> registrar(
            Solicitud solicitud,
            List<SolicitudContacto> contactos
    );

}