package com.dinet.solicitudes.domain.port.in;

import com.dinet.solicitudes.domain.model.Solicitud;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface ListarSolicitudesUseCase {

    Flux<Solicitud> listar(
            String tipoSolicitud,
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );
}
