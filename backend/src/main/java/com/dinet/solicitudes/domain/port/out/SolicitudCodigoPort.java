package com.dinet.solicitudes.domain.port.out;

import reactor.core.publisher.Mono;

public interface SolicitudCodigoPort {
    Mono<String> generarCodigo();
}