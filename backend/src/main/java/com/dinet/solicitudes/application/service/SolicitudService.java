package com.dinet.solicitudes.application.service;

import com.dinet.solicitudes.application.exception.SolicitudNoEncontradaException;
import com.dinet.solicitudes.domain.model.Solicitud;
import com.dinet.solicitudes.domain.model.SolicitudContacto;
import com.dinet.solicitudes.domain.model.SolicitudDetalle;
import com.dinet.solicitudes.domain.port.in.ListarSolicitudesUseCase;
import com.dinet.solicitudes.domain.port.in.ObtenerSolicitudUseCase;
import com.dinet.solicitudes.domain.port.in.RegistrarSolicitudUseCase;
import com.dinet.solicitudes.domain.port.out.SolicitudCodigoPort;
import com.dinet.solicitudes.domain.port.out.SolicitudRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
public class SolicitudService implements
        RegistrarSolicitudUseCase,
        ListarSolicitudesUseCase,
        ObtenerSolicitudUseCase {

    private final SolicitudRepositoryPort repository;
    private final SolicitudCodigoPort codigoPort;


    public SolicitudService(SolicitudRepositoryPort repository,SolicitudCodigoPort codigoPort) {
        this.repository = repository;
        this.codigoPort = codigoPort;
    }

    @Override
    @Transactional
    public Mono<Solicitud> registrar(
            Solicitud solicitud,
            List<SolicitudContacto> contactos) {

        return codigoPort.generarCodigo()
                .flatMap(codigo -> {
                    Solicitud nueva = new Solicitud(
                            null,
                            codigo,
                            solicitud.marca(),
                            solicitud.tipoSolicitud(),
                            solicitud.fechaEnvio(),
                            solicitud.nombreContacto(),
                            solicitud.numeroContacto()
                    );

                    return repository.guardarSolicitud(nueva)
                            .flatMap(guardada ->
                                    Flux.fromIterable(contactos)
                                            .concatMap(contacto ->
                                                    repository.guardarContacto(
                                                            new SolicitudContacto(
                                                                    null,
                                                                    guardada.id(),
                                                                    contacto.nombreContacto(),
                                                                    contacto.numeroContacto()
                                                            )
                                                    )
                                            )
                                            .then(Mono.just(guardada))
                            );
                });
    }

    @Override
    public Flux<Solicitud> listar(
            String tipoSolicitud,
            LocalDate fechaDesde,
            LocalDate fechaHasta) {

        return repository.listarSolicitudes(
                tipoSolicitud,
                fechaDesde,
                fechaHasta
        );
    }

    @Override
    public Mono<SolicitudDetalle> obtenerPorCodigo(String codigo) {
        return repository.buscarPorCodigo(codigo)
                .switchIfEmpty(Mono.error(
                        new SolicitudNoEncontradaException(codigo)
                ))
                .flatMap(solicitud ->
                        repository.buscarContactosPorSolicitudId(solicitud.id())
                                .collectList()
                                .map(contactos -> new SolicitudDetalle(
                                        solicitud.id(),
                                        solicitud.codigo(),
                                        solicitud.marca(),
                                        solicitud.tipoSolicitud(),
                                        solicitud.fechaEnvio(),
                                        solicitud.nombreContacto(),
                                        solicitud.numeroContacto(),
                                        contactos
                                ))
                );
    }
}
