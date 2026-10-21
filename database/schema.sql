CREATE SEQUENCE IF NOT EXISTS solicitud_codigo_seq
    START WITH 3
    INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS solicitud (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    marca VARCHAR(100) NOT NULL,
    tipo_solicitud VARCHAR(50) NOT NULL,
    fecha_envio DATE NOT NULL,
    nombre_contacto VARCHAR(150) NOT NULL,
    numero_contacto VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_solicitud_fecha_envio
    ON solicitud (fecha_envio);

CREATE INDEX IF NOT EXISTS idx_solicitud_tipo_solicitud
    ON solicitud (tipo_solicitud);

CREATE TABLE IF NOT EXISTS solicitud_contacto (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    solicitud_id BIGINT NOT NULL,
    nombre_contacto VARCHAR(150) NOT NULL,
    numero_contacto VARCHAR(20) NOT NULL,
    CONSTRAINT fk_solicitud_contacto_solicitud
        FOREIGN KEY (solicitud_id)
        REFERENCES solicitud (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_solicitud_contacto_solicitud_id
    ON solicitud_contacto (solicitud_id);
