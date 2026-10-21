# Sistema de Solicitudes

Aplicación web para registrar, consultar y exportar solicitudes. El
proyecto está dividido en frontend, backend y base de datos, y puede
ejecutarse mediante Docker Compose.

## Tecnologías

-   **Frontend:** React, Vite y React Router.
-   **Backend:** Java 21, Spring Boot, Spring WebFlux y Gradle.
-   **Persistencia:** PostgreSQL y Spring Data R2DBC.
-   **Documentación API:** OpenAPI / Swagger UI.
-   **Contenedores:** Docker y Docker Compose.
-   **Arquitectura backend:** arquitectura hexagonal, separando dominio,
    casos de uso y adaptadores.

## Requisitos

Para ejecutar el proyecto con Docker Compose, necesitas:

-   Docker Engine.
-   Docker Compose v2 (`docker compose`).

No necesitas instalar Java, Gradle, Node.js ni PostgreSQL en tu máquina
para ejecutar la versión contenerizada.

## Ejecución rápida

Desde la raíz del repositorio, donde se encuentra `docker-compose.yml`:

``` bash
docker compose up --build
```

La primera ejecución construye las imágenes y puede tardar unos minutos.

Cuando los servicios estén listos, abre:

  Componente   URL
  ------------ ---------------------------------------
  Frontend     http://localhost:5173
  API REST     http://localhost:8080/api/solicitudes
  Swagger UI   http://localhost:8080/swagger-ui.html
  PostgreSQL   `localhost:5432`

Para detener los servicios:

``` bash
docker compose down
```

Los datos de PostgreSQL se conservan en el volumen de Docker al detener
los servicios. Para eliminarlos deliberadamente, sería necesario borrar
también el volumen; hazlo solo si no necesitas conservar los datos.

## Configuración

Docker Compose utiliza las variables del archivo `.env` en la raíz del
proyecto. Revisa `.env.example` como referencia y configura los valores
locales necesarios antes de ejecutar el proyecto.

Variables habituales:

``` dotenv
POSTGRES_DB=solicitudes_db
POSTGRES_USER=solicitudes_user
POSTGRES_PASSWORD=local_dev_password
POSTGRES_PORT=5432
```

La contraseña debe coincidir con la configurada para el usuario de
PostgreSQL. Si la base de datos ya fue inicializada en un volumen,
cambiar `POSTGRES_PASSWORD` en `.env` no actualiza automáticamente la
contraseña almacenada en PostgreSQL.

En el frontend, `VITE_API_URL` se configura para desarrollo local y
producción. En el contenedor frontend, Nginx sirve la aplicación y
redirige las peticiones bajo `/api/` al servicio backend.

> **Seguridad:** los valores anteriores son para desarrollo local. No
> uses credenciales de desarrollo en producción ni publiques secretos
> reales en el repositorio. Si `.env` contiene credenciales, asegúrate
> de que esté excluido de Git y publica únicamente `.env.example` con
> valores de ejemplo.

## Funcionalidades

-   Listado de solicitudes.
-   Registro de nuevas solicitudes.
-   Consulta del detalle por código.
-   Filtrado por tipo de solicitud y rango de fechas.
-   Ordenamiento del listado.
-   Exportación de solicitudes a CSV.
-   Registro de contactos asociados a una solicitud.

## API REST

La API utiliza la ruta base `/api/solicitudes`.

  -----------------------------------------------------------------------------
  Método                  Ruta                          Descripción
  ----------------------- ----------------------------- -----------------------
  `GET`                   `/api/solicitudes`            Lista solicitudes;
                                                        admite filtros según
                                                        los parámetros
                                                        implementados.

  `POST`                  `/api/solicitudes`            Registra una solicitud.

  `GET`                   `/api/solicitudes/{codigo}`   Consulta el detalle de
                                                        una solicitud por
                                                        código.

  `GET`                   `/api/solicitudes/exportar`   Exporta solicitudes en
                                                        formato CSV.
  -----------------------------------------------------------------------------

Consulta Swagger UI para ver los parámetros, modelos y respuestas
documentados por la implementación.

## Base de datos

Los scripts SQL se encuentran en el directorio `database/`:

-   `schema.sql`: definición del esquema.
-   `data.sql`: datos iniciales de prueba.

Docker ejecuta estos scripts automáticamente al inicializar un volumen
de PostgreSQL vacío. Si el volumen ya existe, los scripts de
inicialización no se vuelven a ejecutar automáticamente.

## Estructura general

``` text
.
├── backend/       # API Spring Boot / WebFlux
├── frontend/      # Aplicación React
├── database/      # Esquema y datos iniciales SQL
├── docker-compose.yml
├── .env.example
└── README.md
```

## Comandos útiles

Ver estado de los contenedores:

``` bash
docker compose ps
```

Ver logs de todos los servicios:

``` bash
docker compose logs -f
```

Ver logs del backend:

``` bash
docker compose logs -f backend
```

Ver logs de PostgreSQL:

``` bash
docker compose logs -f postgres
```

Reconstruir e iniciar los servicios:

``` bash
docker compose up -d --build
```

## Solución de problemas

### La API devuelve HTTP 500 y el log indica fallo de autenticación PostgreSQL

Revisa las variables `POSTGRES_USER` y `POSTGRES_PASSWORD` de `.env` y
las variables `SPRING_R2DBC_USERNAME` y `SPRING_R2DBC_PASSWORD` que
recibe el backend en `docker-compose.yml`.

Recuerda que actualizar la contraseña en `.env` no modifica la
contraseña de un usuario ya existente en PostgreSQL. Si necesitas
cambiarla, actualiza explícitamente la contraseña del usuario en la base
de datos y vuelve a recrear el contenedor backend.

### El puerto ya está en uso

Si los puertos `5173`, `8080` o `5432` están ocupados, detén el servicio
que los utiliza o ajusta los puertos publicados en `docker-compose.yml`
y las variables de entorno correspondientes.

### Los datos iniciales no aparecen

Los scripts `database/schema.sql` y `database/data.sql` se ejecutan
automáticamente solo cuando PostgreSQL inicializa un directorio de datos
vacío. Comprueba si el volumen ya existía y revisa los logs de
PostgreSQL antes de considerar cualquier reinicialización.

## Autoría

Proyecto de reto técnico para demostrar el desarrollo de una aplicación
web con React, Spring WebFlux, PostgreSQL, Gradle, Docker Compose y
arquitectura hexagonal.
