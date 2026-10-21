INSERT INTO solicitud (
    codigo, marca, tipo_solicitud, fecha_envio,
    nombre_contacto, numero_contacto
) VALUES
    ('SOL-2026-000001', 'Toyota', 'RECLAMO', '2026-10-15',
     'Ana Torres', '987654321'),
    ('SOL-2026-000002', 'Hyundai', 'CONSULTA', '2026-10-18',
     'Luis Ramos', '912345678')
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO solicitud_contacto (
    solicitud_id, nombre_contacto, numero_contacto
)
SELECT s.id, v.nombre_contacto, v.numero_contacto
FROM solicitud s
JOIN (VALUES
    ('SOL-2026-000001', 'Mario Salas', '956123456'),
    ('SOL-2026-000001', 'Lucia Vega', '945123456'),
    ('SOL-2026-000002', 'Pedro Ruiz', '934123456')
) AS v(codigo, nombre_contacto, numero_contacto)
    ON s.codigo = v.codigo
WHERE NOT EXISTS (
    SELECT 1
    FROM solicitud_contacto sc
    WHERE sc.solicitud_id = s.id
      AND sc.nombre_contacto = v.nombre_contacto
      AND sc.numero_contacto = v.numero_contacto
);
