package com.dinet.solicitudes.domain.port.in;

import com.dinet.solicitudes.domain.model.SolicitudDetalle;
import reactor.core.publisher.Mono;

public interface ObtenerSolicitudUseCase {

    Mono<SolicitudDetalle> obtenerPorCodigo(String codigo);
}