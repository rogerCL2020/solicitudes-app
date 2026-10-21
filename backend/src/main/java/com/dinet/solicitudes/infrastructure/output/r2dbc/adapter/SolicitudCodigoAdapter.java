package com.dinet.solicitudes.infrastructure.output.r2dbc.adapter;

import com.dinet.solicitudes.domain.port.out.SolicitudCodigoPort;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SolicitudCodigoAdapter implements SolicitudCodigoPort {

    private final DatabaseClient databaseClient;

    public SolicitudCodigoAdapter(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<String> generarCodigo() {
        return databaseClient.sql(
                        "SELECT nextval('solicitud_codigo_seq')")
                .map((row, metadata) -> row.get(0, Long.class))
                .one()
                .map(numero -> "SOL-2026-%06d".formatted(numero));
    }
}