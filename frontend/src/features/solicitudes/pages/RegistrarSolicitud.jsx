
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { registrarSolicitud } from '../services/solicitudService';

const formularioInicial = {
  marca: '',
  tipoSolicitud: '',
  fechaEnvio: '',
  nombreContacto: '',
  numeroContacto: '',
  contactos: [],
};

export default function RegistrarSolicitud() {
  const [formulario, setFormulario] = useState(formularioInicial);
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);
  const navigate = useNavigate();

  function actualizarCampo(event) {
    const { name, value } = event.target;

    setFormulario((actual) => ({
      ...actual,
      [name]: value,
    }));
  }


    function agregarContacto() {
    setFormulario((actual) => ({
        ...actual,
        contactos: [
        ...actual.contactos,
        { nombreContacto: '', numeroContacto: '' },
        ],
    }));
    }

    function actualizarContacto(index, campo, valor) {
    setFormulario((actual) => ({
        ...actual,
        contactos: actual.contactos.map((contacto, i) =>
        i === index ? { ...contacto, [campo]: valor } : contacto,
        ),
    }));
    }

    function eliminarContacto(index) {
    setFormulario((actual) => ({
        ...actual,
        contactos: actual.contactos.filter((_, i) => i !== index),
    }));
    }



    async function guardar(event) {
        event.preventDefault();
        setError('');
        setGuardando(true);

        try {
            const solicitud = await registrarSolicitud(formulario);
            navigate(`/solicitudes/${solicitud.codigo}`);
        } catch (err) {
            setError(err.message);
        } finally {
            setGuardando(false);
        }
    }


  
return (
  <main className="page-container">
    <div className="page-heading">
      <div>
        <h2>Nueva solicitud</h2>
        <p>Completa los datos para registrar una solicitud.</p>
      </div>

      <Link className="btn btn-secondary" to="/solicitudes">
        Volver al listado
      </Link>
    </div>

    <form onSubmit={guardar}>
      <section className="card">
        <h3>Información de la solicitud</h3>
        <p className="muted">
          Ingresa los datos principales de la solicitud.
        </p>

        <div className="form-grid">
          <div className="field">
            <label htmlFor="marca">Marca del vehículo</label>
            <input
              id="marca"
              name="marca"
              value={formulario.marca}
              onChange={actualizarCampo}
              required
              maxLength={100}
              placeholder="Ej. Toyota"
            />
          </div>

          <div className="field">
            <label htmlFor="tipoSolicitud">Tipo de solicitud</label>
            <select
              id="tipoSolicitud"
              name="tipoSolicitud"
              value={formulario.tipoSolicitud}
              onChange={actualizarCampo}
              required
            >
              <option value="">Selecciona un tipo</option>
              <option value="RECLAMO">Reclamo</option>
              <option value="CONSULTA">Consulta</option>
              <option value="SOLICITUD">Solicitud</option>
            </select>
          </div>

          <div className="field">
            <label htmlFor="fechaEnvio">Fecha de envío</label>
            <input
              id="fechaEnvio"
              name="fechaEnvio"
              type="date"
              value={formulario.fechaEnvio}
              onChange={actualizarCampo}
              required
            />
          </div>
        </div>
      </section>

      <section className="card">
        <h3>Contacto principal</h3>
        <p className="muted">
          Datos de la persona que registra la solicitud.
        </p>

        <div className="form-grid">
          <div className="field">
            <label htmlFor="nombreContacto">Nombre completo</label>
            <input
              id="nombreContacto"
              name="nombreContacto"
              value={formulario.nombreContacto}
              onChange={actualizarCampo}
              required
              maxLength={150}
              placeholder="Nombre del contacto"
            />
          </div>

          <div className="field">
            <label htmlFor="numeroContacto">Número de teléfono</label>
            <input
              id="numeroContacto"
              name="numeroContacto"
              type="text"
              inputMode="numeric"
              pattern="[0-9]{7,20}"
              maxLength={20}
              value={formulario.numeroContacto}
              onChange={(event) =>
                actualizarCampo({
                  target: {
                    name: 'numeroContacto',
                    value: event.target.value.replace(/\D/g, ''),
                  },
                })
              }
              required
              placeholder="Ej. 987654321"
            />
          </div>
        </div>
      </section>

      <section className="card">
        <div className="section-heading">
          <div>
            <h3>Contactos adicionales</h3>
            <p className="muted">
              Agrega otras personas relacionadas con la solicitud.
            </p>
          </div>

          <button
            className="btn btn-secondary"
            type="button"
            onClick={agregarContacto}
          >
            + Agregar contacto
          </button>
        </div>

        {formulario.contactos.length === 0 ? (
          <p className="empty-state">
            No hay contactos adicionales registrados.
          </p>
        ) : (
          <div className="contacts-list">
            {formulario.contactos.map((contacto, index) => (
              <div className="contact-card" key={index}>
                <div className="section-heading">
                  <h4>Contacto adicional {index + 1}</h4>

                  <button
                    className="btn btn-danger"
                    type="button"
                    onClick={() => eliminarContacto(index)}
                  >
                    Eliminar
                  </button>
                </div>

                <div className="form-grid">
                  <div className="field">
                    <label htmlFor={`contacto-nombre-${index}`}>
                      Nombre completo
                    </label>
                    <input
                      id={`contacto-nombre-${index}`}
                      value={contacto.nombreContacto}
                        onChange={(event) =>
                        actualizarContacto(index, 'nombreContacto', event.target.value)
                      }
                      required
                      maxLength={150}
                      placeholder="Nombre del contacto"
                    />
                  </div>

                  <div className="field">
                    <label htmlFor={`contacto-telefono-${index}`}>
                      Número de teléfono
                    </label>
                    <input
                      id={`contacto-telefono-${index}`}
                      type="text"
                      inputMode="numeric"
                      pattern="[0-9]{7,20}"
                      maxLength={20}
                      value={contacto.numeroContacto}
                        onChange={(event) =>
                        actualizarContacto(
                            index,
                            'numeroContacto',
                            event.target.value.replace(/\D/g, '')
                        )
                        }
                      required
                      placeholder="Ej. 987654321"
                    />
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </section>

      {error && (
        <div className="error-message" role="alert">
          {error}
        </div>
      )}

      <div className="actions form-actions">
        <button
          className="btn btn-primary"
          type="submit"
          disabled={guardando}
        >
          {guardando ? 'Guardando...' : 'Registrar solicitud'}
        </button>

        <Link className="btn btn-secondary" to="/solicitudes">
          Cancelar
        </Link>
      </div>
    </form>
  </main>
);
}
