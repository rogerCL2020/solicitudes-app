package com.dinet.solicitudes.infrastructure.output.r2dbc.repository;

import com.dinet.solicitudes.infrastructure.output.r2dbc.entity.SolicitudR2dbcEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface SolicitudR2dbcRepository
        extends ReactiveCrudRepository<SolicitudR2dbcEntity, Long> {

    Mono<SolicitudR2dbcEntity> findByCodigo(String codigo);

    @Query("""
    SELECT *
    FROM solicitud
    WHERE (:tipoSolicitud IS NULL
           OR tipo_solicitud = :tipoSolicitud)
      AND (:fechaDesde IS NULL
           OR fecha_envio >= :fechaDesde)
      AND (:fechaHasta IS NULL
           OR fecha_envio <= :fechaHasta)
    ORDER BY fecha_envio DESC, id DESC
    """)
    Flux<SolicitudR2dbcEntity> listarFiltradas(
            String tipoSolicitud,
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );
}
