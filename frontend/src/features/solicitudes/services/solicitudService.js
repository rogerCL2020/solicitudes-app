
import {
  get,
  post,
  download,
  SOLICITUDES_URL,
} from '../../../services/httpClient';

function construirParametros(filtros = {}) {
  const params = new URLSearchParams();

  if (filtros.tipoSolicitud) {
    params.set('tipoSolicitud', filtros.tipoSolicitud);
  }

  if (filtros.fechaDesde) {
    params.set('fechaDesde', filtros.fechaDesde);
  }

  if (filtros.fechaHasta) {
    params.set('fechaHasta', filtros.fechaHasta);
  }

  return params.toString();
}

function construirUrl(filtros = {}, endpoint = SOLICITUDES_URL) {
  const query = construirParametros(filtros);
  return query ? `${endpoint}?${query}` : endpoint;
}

export function listarSolicitudes(filtros = {}) {
  return get(construirUrl(filtros));
}

export function registrarSolicitud(datos) {
  return post(SOLICITUDES_URL, datos);
}

export function obtenerSolicitud(codigo) {
  return get(`${SOLICITUDES_URL}/${encodeURIComponent(codigo)}`);
}

export function exportarSolicitudes(filtros = {}) {
  return download(
    construirUrl(filtros, `${SOLICITUDES_URL}/exportar`)
  );
}
