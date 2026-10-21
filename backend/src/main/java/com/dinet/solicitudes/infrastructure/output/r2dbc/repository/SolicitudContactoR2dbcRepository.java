package com.dinet.solicitudes.infrastructure.output.r2dbc.repository;

import com.dinet.solicitudes.infrastructure.output.r2dbc.entity.SolicitudContactoR2dbcEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SolicitudContactoR2dbcRepository
        extends ReactiveCrudRepository<SolicitudContactoR2dbcEntity, Long> {

    Flux<SolicitudContactoR2dbcEntity> findBySolicitudId(Long solicitudId);
}