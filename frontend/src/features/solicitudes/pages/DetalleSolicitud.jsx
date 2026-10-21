
import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { obtenerSolicitud } from '../services/solicitudService';

export default function DetalleSolicitud() {
  const { codigo } = useParams();
  const [solicitud, setSolicitud] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
  setCargando(true);
  setError('');
  setSolicitud(null);

  obtenerSolicitud(codigo)
    .then(setSolicitud)
    .catch((err) => setError(err.message))
    .finally(() => setCargando(false));
    }, [codigo]);

  if (cargando) {
    return (
      <main className="page-container">
        <div className="card">
          <p className="muted">Cargando detalle de la solicitud...</p>
        </div>
      </main>
    );
  }

  if (error || !solicitud) {
    return (
      <main className="page-container">
        <div className="error-message" role="alert">
          {error || 'No se encontró la solicitud.'}
        </div>

        <Link className="btn btn-secondary" to="/solicitudes">
          Volver al listado
        </Link>
      </main>
    );
  }

  return (
    <main className="page-container">
      <div className="page-heading">
        <div>
          <p className="eyebrow">GESTIÓN DE SOLICITUDES</p>
          <h2>Detalle de solicitud</h2>
          <p>Consulta la información y los contactos registrados.</p>
        </div>

        <Link className="btn btn-secondary" to="/solicitudes">
          ← Volver al listado
        </Link>
      </div>

      <section className="card detail-summary">
        <div>
          <p className="muted">Código de solicitud</p>
          <h3 className="detail-code">{solicitud.codigo}</h3>
        </div>

        <span className="type-label">
          {solicitud.tipoSolicitud}
        </span>
      </section>

      <section className="card">
        <h3>Información general</h3>

        <div className="detail-grid">
          <div className="detail-field">
            <span className="detail-label">Marca del vehículo</span>
            <span className="detail-value">{solicitud.marca}</span>
          </div>

          <div className="detail-field">
            <span className="detail-label">Tipo de solicitud</span>
            <span className="detail-value">
              {solicitud.tipoSolicitud}
            </span>
          </div>

          <div className="detail-field">
            <span className="detail-label">Fecha de envío</span>
            <span className="detail-value">{solicitud.fechaEnvio}</span>
          </div>
        </div>
      </section>

      <section className="card">
        <h3>Contacto principal</h3>

        <div className="detail-grid">
          <div className="detail-field">
            <span className="detail-label">Nombre completo</span>
            <span className="detail-value">
              {solicitud.nombreContacto}
            </span>
          </div>

          <div className="detail-field">
            <span className="detail-label">Número de teléfono</span>
            <span className="detail-value">
              {solicitud.numeroContacto}
            </span>
          </div>
        </div>
      </section>

      <section className="card">
        <div className="section-heading">
          <div>
            <h3>Contactos adicionales</h3>
            <p className="muted">
              Personas adicionales asociadas a esta solicitud.
            </p>
          </div>

          <span className="contact-count">
            {solicitud.contactos?.length ?? 0}
          </span>
        </div>

        {solicitud.contactos?.length > 0 ? (
          <div className="contacts-list">
            {solicitud.contactos.map((contacto) => (
              <article className="contact-card" key={contacto.id}>
                <h4>{contacto.nombreContacto}</h4>
                <p className="muted">
                  Teléfono: {contacto.numeroContacto}
                </p>
              </article>
            ))}
          </div>
        ) : (
          <p className="empty-state">
            Esta solicitud no tiene contactos adicionales.
          </p>
        )}
      </section>

      <div className="actions form-actions">
        <Link className="btn btn-secondary" to="/solicitudes">
          Volver al listado
        </Link>
      </div>
    </main>
  );
}
