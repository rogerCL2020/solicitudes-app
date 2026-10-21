package com.dinet.solicitudes.domain.port.out;


import com.dinet.solicitudes.domain.model.Solicitud;
import com.dinet.solicitudes.domain.model.SolicitudContacto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface SolicitudRepositoryPort {

    Mono<Solicitud> guardarSolicitud(Solicitud solicitud);

    Mono<SolicitudContacto> guardarContacto(
            SolicitudContacto contacto
    );

    Flux<SolicitudContacto> buscarContactosPorSolicitudId(
            Long solicitudId
    );

    Mono<Solicitud> buscarPorCodigo(String codigo);

    Flux<Solicitud> listarSolicitudes(
            String tipoSolicitud,
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );
}

