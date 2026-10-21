package com.dinet.solicitudes.infrastructure.input.rest;


import com.dinet.solicitudes.domain.model.Solicitud;
import com.dinet.solicitudes.domain.model.SolicitudContacto;
import com.dinet.solicitudes.domain.model.SolicitudDetalle;
import com.dinet.solicitudes.domain.port.in.ListarSolicitudesUseCase;
import com.dinet.solicitudes.domain.port.in.ObtenerSolicitudUseCase;
import com.dinet.solicitudes.domain.port.in.RegistrarSolicitudUseCase;
import com.dinet.solicitudes.application.dto.request.CrearSolicitudRequest;
import com.dinet.solicitudes.infrastructure.output.csv.SolicitudCsvExporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final RegistrarSolicitudUseCase registrarSolicitud;
    private final ListarSolicitudesUseCase listarSolicitudes;
    private final ObtenerSolicitudUseCase obtenerSolicitud;
    private final SolicitudCsvExporter csvExporter;

    public SolicitudController(RegistrarSolicitudUseCase registrarSolicitud,
                               ListarSolicitudesUseCase listarSolicitudes,
                               ObtenerSolicitudUseCase obtenerSolicitud,
                               SolicitudCsvExporter csvExporter) {

        this.registrarSolicitud = registrarSolicitud;
        this.listarSolicitudes = listarSolicitudes;
        this.obtenerSolicitud = obtenerSolicitud;
        this.csvExporter = csvExporter;
    }

    @Operation(summary = "Registrar una solicitud",
            description = "Registra una solicitud con sus contactos y genera su código automáticamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud creada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Solicitud> registrar(
            @Valid @RequestBody CrearSolicitudRequest request) {

        List<SolicitudContacto> contactos =
                request.contactos() == null
                        ? List.of()
                        : request.contactos().stream()
                        .map(c -> new SolicitudContacto(
                                null,
                                null,
                                c.nombreContacto(),
                                c.numeroContacto()
                        ))
                        .toList();

        Solicitud solicitud = new Solicitud(
                null,
                null,
                request.marca(),
                request.tipoSolicitud(),
                request.fechaEnvio(),
                request.nombreContacto(),
                request.numeroContacto()
        );

        return registrarSolicitud.registrar(solicitud, contactos);
    }

    @Operation(
            summary = "Listar solicitudes",
            description = "Lista solicitudes y permite filtrar por tipo y rango de fechas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de filtro inválidos")
    })
    @GetMapping
    public Flux<Solicitud> listar(
            @RequestParam(required = false) String tipoSolicitud,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaDesde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaHasta) {

        return listarSolicitudes.listar(
                tipoSolicitud,
                fechaDesde,
                fechaHasta
        );
    }

    @Operation(
            summary = "Obtener solicitud por código",
            description = "Consulta los datos de una solicitud y sus contactos asociados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
            @ApiResponse(responseCode = "404", description = "No existe una solicitud con ese código")
    })
    @GetMapping("/{codigo}")
    public Mono<SolicitudDetalle> obtenerPorCodigo(
            @Parameter(description = "Código generado de la solicitud",
                    example = "SOL-2026-000001")
            @PathVariable String codigo) {

        return obtenerSolicitud.obtenerPorCodigo(codigo);
    }

    @Operation(summary = "Exportar solicitudes a CSV",
            description = "Descarga las solicitudes aplicando los filtros del listado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CSV generado"),
            @ApiResponse(responseCode = "400", description = "Filtros inválidos")
    })
    @GetMapping(value = "/exportar", produces = "text/csv")
    public Mono<ResponseEntity<byte[]>> exportarCsv(
            @RequestParam(required = false) String tipoSolicitud,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaDesde,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaHasta) {

        return listarSolicitudes.listar(tipoSolicitud, fechaDesde, fechaHasta)
                .collectList()
                .map(csvExporter::exportar)
                .map(csv -> ResponseEntity.ok()
                        .contentType(new MediaType(
                                "text", "csv", StandardCharsets.UTF_8))
                        .header(
                                HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"solicitudes.csv\"")
                        .body(csv.getBytes(StandardCharsets.UTF_8)));
    }
}