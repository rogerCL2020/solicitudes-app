
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  listarSolicitudes,
  exportarSolicitudes,
} from '../services/solicitudService';

export default function ListadoSolicitudes() {
  const [solicitudes, setSolicitudes] = useState([]);
  const [tipoSolicitud, setTipoSolicitud] = useState('');
  const [fechaDesde, setFechaDesde] = useState('');
  const [fechaHasta, setFechaHasta] = useState('');
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');

  const [orden, setOrden] = useState({
  campo: 'fechaEnvio',
  direccion: 'desc',
  });

  const [exportando, setExportando] = useState(false);

  async function cargarSolicitudes(filtros = {}) {
    setCargando(true);
    setError('');

    try {
      const datos = await listarSolicitudes(filtros);
      setSolicitudes(datos);
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  }

  useEffect(() => {
    cargarSolicitudes();
  }, []);

  function buscar(event) {
    event.preventDefault();

    if (fechaDesde && fechaHasta && fechaDesde > fechaHasta) {
      setError('La fecha desde no puede ser posterior a la fecha hasta.');
      return;
    }

    cargarSolicitudes({ tipoSolicitud, fechaDesde, fechaHasta });
  }

  function limpiarFiltros() {
    setTipoSolicitud('');
    setFechaDesde('');
    setFechaHasta('');
    cargarSolicitudes();
  }



    function cambiarOrden(campo) {
    setOrden((actual) => ({
        campo,
        direccion:
        actual.campo === campo && actual.direccion === 'asc'
            ? 'desc'
            : 'asc',
    }));
    }

    const solicitudesOrdenadas = [...solicitudes].sort((a, b) => {
    const valorA = a[orden.campo] ?? '';
    const valorB = b[orden.campo] ?? '';

    const comparacion = String(valorA).localeCompare(
        String(valorB),
        'es',
        { numeric: true, sensitivity: 'base' },
    );

    return orden.direccion === 'asc' ? comparacion : -comparacion;
    });



    async function exportarCsv() {
    setExportando(true);
    setError('');

    try {
        const archivo = await exportarSolicitudes({
        tipoSolicitud,
        fechaDesde,
        fechaHasta,
        });

        const url = URL.createObjectURL(archivo);
        const enlace = document.createElement('a');

        enlace.href = url;
        enlace.download = 'solicitudes.csv';
        document.body.appendChild(enlace);
        enlace.click();
        enlace.remove();

        URL.revokeObjectURL(url);
    } catch (err) {
        setError(err.message);
    } finally {
        setExportando(false);
    }
    }



  return (
    <main className="page-container">
      <div className="page-heading">
        <div>
          <h2>Solicitudes</h2>
          <p>
            Consulta, filtra y administra las solicitudes registradas.
          </p>
        </div>

        <Link className="btn btn-primary" to="/solicitudes/nueva">
          + Nueva solicitud
        </Link>
      </div>

      <section className="card">
        <h3>Filtros de búsqueda</h3>

        <form onSubmit={buscar}>
          <div className="filters">
            <div className="field">
              <label htmlFor="tipoSolicitud">
                Tipo de solicitud
              </label>
              <select
                id="tipoSolicitud"
                value={tipoSolicitud}
                onChange={(event) => setTipoSolicitud(event.target.value)}
              >
                <option value="">Todos</option>
                <option value="RECLAMO">Reclamo</option>
                <option value="CONSULTA">Consulta</option>
                <option value="SOLICITUD">Solicitud</option>
              </select>
            </div>

            <div className="field">
              <label htmlFor="fechaDesde">Fecha desde</label>
              <input
                id="fechaDesde"
                type="date"
                value={fechaDesde}
                onChange={(event) => setFechaDesde(event.target.value)}
              />
            </div>

            <div className="field">
              <label htmlFor="fechaHasta">Fecha hasta</label>
              <input
                id="fechaHasta"
                type="date"
                value={fechaHasta}
                onChange={(event) => setFechaHasta(event.target.value)}
              />
            </div>
          </div>

          <div className="actions">
            <button className="btn btn-primary" type="submit">
              Buscar
            </button>

            <button
              className="btn btn-secondary"
              type="button"
              onClick={limpiarFiltros}
            >
              Limpiar filtros
            </button>

            <button
              className="btn btn-secondary"
              type="button"
              onClick={exportarCsv}
              disabled={exportando}
            >
              {exportando ? 'Exportando...' : 'Exportar CSV'}
            </button>
          </div>
        </form>
      </section>

      {error && (
        <div className="error-message" role="alert">
          {error}
        </div>
      )}

      <section className="card">
        <div className="section-heading">
          <div>
            <h3>Resultados</h3>
            <p className="muted">
              {solicitudes.length} solicitud(es) encontrada(s)
            </p>
          </div>
        </div>

        {cargando ? (
          <p className="muted">Cargando solicitudes...</p>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  {[
                    ['codigo', 'Código'],
                    ['marca', 'Marca'],
                    ['tipoSolicitud', 'Tipo'],
                    ['fechaEnvio', 'Fecha de envío'],
                    ['nombreContacto', 'Contacto'],
                    ['numeroContacto', 'Número'],
                  ].map(([campo, titulo]) => (
                    <th key={campo}>
                      <button
                        className="sort-button"
                        type="button"
                        onClick={() => cambiarOrden(campo)}
                      >
                        {titulo}
                        {orden.campo === campo
                          ? orden.direccion === 'asc'
                            ? ' ▲'
                            : ' ▼'
                          : ''}
                      </button>
                    </th>
                  ))}
                </tr>
              </thead>

              <tbody>
                {solicitudesOrdenadas.map((solicitud) => (
                  <tr key={solicitud.id}>
                    <td>
                      <Link
                        className="code-link"
                        to={`/solicitudes/${solicitud.codigo}`}
                      >
                        {solicitud.codigo}
                      </Link>
                    </td>
                    <td>{solicitud.marca}</td>
                    <td>
                      <span className="type-label">
                        {solicitud.tipoSolicitud}
                      </span>
                    </td>
                    <td>{solicitud.fechaEnvio}</td>
                    <td>{solicitud.nombreContacto}</td>
                    <td>{solicitud.numeroContacto}</td>
                  </tr>
                ))}
              </tbody>
            </table>

            {solicitudes.length === 0 && (
              <p className="empty-state">
                No hay solicitudes para los filtros seleccionados.
              </p>
            )}
          </div>
        )}
      </section>
    </main>
  );
}

