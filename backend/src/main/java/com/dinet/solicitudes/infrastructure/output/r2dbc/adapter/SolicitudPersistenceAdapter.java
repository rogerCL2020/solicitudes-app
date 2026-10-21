package com.dinet.solicitudes.infrastructure.output.r2dbc.adapter;

import com.dinet.solicitudes.domain.model.Solicitud;
import com.dinet.solicitudes.domain.model.SolicitudContacto;
import com.dinet.solicitudes.domain.port.out.SolicitudRepositoryPort;
import com.dinet.solicitudes.infrastructure.output.r2dbc.entity.SolicitudContactoR2dbcEntity;
import com.dinet.solicitudes.infrastructure.output.r2dbc.repository.SolicitudContactoR2dbcRepository;
import com.dinet.solicitudes.infrastructure.output.r2dbc.entity.SolicitudR2dbcEntity;
import com.dinet.solicitudes.infrastructure.output.r2dbc.repository.SolicitudR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
public class SolicitudPersistenceAdapter
        implements SolicitudRepositoryPort {

    private final SolicitudR2dbcRepository solicitudRepository;
    private final SolicitudContactoR2dbcRepository contactoRepository;

    public SolicitudPersistenceAdapter(
            SolicitudR2dbcRepository solicitudRepository,
            SolicitudContactoR2dbcRepository contactoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.contactoRepository = contactoRepository;
    }

    @Override
    public Mono<Solicitud> guardarSolicitud(Solicitud solicitud) {
        var entity = new SolicitudR2dbcEntity(
                solicitud.id(),
                solicitud.codigo(),
                solicitud.marca(),
                solicitud.tipoSolicitud(),
                solicitud.fechaEnvio(),
                solicitud.nombreContacto(),
                solicitud.numeroContacto()
        );

        return solicitudRepository.save(entity)
                .map(this::toDomain);
    }

    @Override
    public Mono<SolicitudContacto> guardarContacto(
            SolicitudContacto contacto) {
        var entity = new SolicitudContactoR2dbcEntity(
                contacto.id(),
                contacto.solicitudId(),
                contacto.nombreContacto(),
                contacto.numeroContacto()
        );

        return contactoRepository.save(entity)
                .map(saved -> new SolicitudContacto(
                        saved.id(),
                        saved.solicitudId(),
                        saved.nombreContacto(),
                        saved.numeroContacto()
                ));
    }

    @Override
    public Flux<SolicitudContacto> buscarContactosPorSolicitudId(
            Long solicitudId) {
        return contactoRepository.findBySolicitudId(solicitudId)
                .map(entity -> new SolicitudContacto(
                        entity.id(),
                        entity.solicitudId(),
                        entity.nombreContacto(),
                        entity.numeroContacto()
                ));
    }

    @Override
    public Mono<Solicitud> buscarPorCodigo(String codigo) {
        return solicitudRepository.findByCodigo(codigo)
                .map(this::toDomain);
    }

    @Override
    public Flux<Solicitud> listarSolicitudes(String tipoSolicitud, LocalDate fechaDesde, LocalDate fechaHasta) {
        return solicitudRepository
                .listarFiltradas(tipoSolicitud, fechaDesde, fechaHasta)
                .map(this::toDomain);
    }

    private Solicitud toDomain(SolicitudR2dbcEntity entity) {
        return new Solicitud(
                entity.id(),
                entity.codigo(),
                entity.marca(),
                entity.tipoSolicitud(),
                entity.fechaEnvio(),
                entity.nombreContacto(),
                entity.numeroContacto()
        );
    }

}