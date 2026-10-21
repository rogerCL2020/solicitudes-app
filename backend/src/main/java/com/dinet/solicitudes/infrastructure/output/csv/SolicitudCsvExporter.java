package com.dinet.solicitudes.infrastructure.output.csv;

import com.dinet.solicitudes.domain.model.Solicitud;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SolicitudCsvExporter {

    public String exportar(List<Solicitud> solicitudes) {
        StringBuilder csv = new StringBuilder();

        csv.append('\uFEFF');
        csv.append(
                "codigo,marca,tipoSolicitud,fechaEnvio,"
                        + "nombreContacto,numeroContacto\n"
        );

        solicitudes.forEach(s -> csv
                .append(escapar(s.codigo())).append(',')
                .append(escapar(s.marca())).append(',')
                .append(escapar(s.tipoSolicitud())).append(',')
                .append(escapar(s.fechaEnvio().toString())).append(',')
                .append(escapar(s.nombreContacto())).append(',')
                .append(escapar(s.numeroContacto())).append('\n')
        );

        return csv.toString();
    }

    private String escapar(String valor) {
        if (valor == null) {
            return "";
        }

        return "\"" + valor.replace("\"", "\"\"") + "\"";
    }
}