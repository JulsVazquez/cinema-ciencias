# Cinema Ciencias

API REST para la gestión de un cine de la Facultad de Ciencias (UNAM): salas (`theaters`),
funciones (`movie_screenings`), tickets y reservaciones.

> **Este servicio ya no gestiona usuarios ni valida JWT.** Los usuarios, las contraseñas y la
> emisión del JWT viven en el **Auth Service**; el **Gateway** valida el token y reenvía la
> identidad en headers (patrón *Trust the Gateway*). Este servicio solo lee esos headers.
> Los tickets y reservaciones guardan el `userId` que emitió el Auth Service.

## Requisitos

- **JDK 21+** (el proyecto compila a Java 21)
- **Maven**: usar el wrapper incluido (`./mvnw`), no hace falta Maven global
- **Docker** para la base de datos (`docker compose up -d postgres-db` desde la raíz del repo).
  No hace falta un PostgreSQL local: el contenedor crea el rol y la base que usa el
  `application.yml`.
- El **Config Server** (8888) arriba: sirve `cinema.security.gatewaySecret` (ver abajo).

## Puesta en marcha

### 1. Base de datos

Desde la raíz del repo:

```bash
docker compose up -d --wait postgres-db
```

Crea la base `cinema_ciencias` y el rol del `application.yml` en el primer arranque. Si el
puerto 5432 del host está ocupado: `CINEMA_DB_PORT=5434 docker compose up -d --wait postgres-db` y
arrancá la app con `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5434/cinema_ciencias`.

### 2. Levantar la app

```bash
./mvnw spring-boot:run
```

La API queda en **http://localhost:8089** (puerto configurado en `application.yml`), pero
**no se usa directo**: las peticiones entran por el Gateway (9000).

Con `spring.jpa.hibernate.ddl-auto=update`, Hibernate crea el esquema automáticamente
(`theaters`, `movie_screenings`, `tickets`, `reservations`, `locations`).

Kafka: `TicketService` publica en `created-tickets` al crear un ticket. La app arranca sin broker
en `localhost:9092`, pero `POST /ticket` lo necesita.

### 3. Correr los tests

```bash
./mvnw test -Dtest='!CinemaCienciasApplicationTests'
```

Se excluye `CinemaCienciasApplicationTests` porque es un test de contexto completo que
requiere la base levantada. El resto de la suite corre con mocks y no necesita Postgres.

## Seguridad: *Trust the Gateway*

El filtro `GatewayAuthenticationFilter` autentica la petición a partir de estos headers:

| Header | Contenido |
|---|---|
| `X-User-Id` | `id` del usuario en el Auth Service |
| `X-User-Name` | username |
| `X-User-Roles` | roles separados por coma (`ROLE_REGULAR_USER,...`) |
| `X-Gateway-Secret` | secreto compartido con el Gateway (`cinema.security.gatewaySecret`) |

Los headers `X-User-*` **solo se aceptan si `X-Gateway-Secret` coincide** (comparación en tiempo
constante). Sin el secreto la petición queda anónima y recibe `401`, aunque traiga un JWT válido:
este servicio ya no sabe validar JWT. Toda ruta exige autenticación (excepto `/error`).

## Endpoints

Base: `/api/v1/cinema`. Se invocan a través del Gateway (`http://localhost:9000`).

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/theater` | Crear sala |
| GET | `/theater` | Listar salas |
| GET | `/theater/{id}` | Obtener sala |
| POST | `/movie-screening` | Crear función |
| GET | `/movie-screening` | Listar funciones |
| GET | `/movie-screening/{id}` | Obtener función |
| GET | `/movie-screening/movie/{movieName}` | Buscar por película |
| POST | `/ticket` | Crear ticket (`movieScreeningId`, `userId`) |
| GET | `/ticket/{id}` | Obtener ticket |
| GET | `/ticket/movie-screening/{movieScreeningId}` | Tickets de una función |
| GET | `/ticket/user/{userId}` | Tickets de un usuario |
| POST | `/reservation` | Crear reservación |
| GET | `/reservation/user/{userId}` | Reservaciones de un usuario |
| DELETE | `/reservation/{reservationId}` | Cancelar reservación (204) |

Ya **no existen** `/auth/**` ni `/user/**` aquí: viven en el Auth Service.

Probando el flujo completo (todo por el Gateway):

```bash
# 1. Registrar y loguear en el Auth Service (a través del Gateway)
curl -X POST http://localhost:9000/api/v1/cinema/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"pepe","email":"pepe@unam.mx","password":"secreto123"}'
RESP=$(curl -s -X POST http://localhost:9000/api/v1/cinema/auth/login \
  -H 'Content-Type: application/json' -d '{"username":"pepe","password":"secreto123"}')
TOKEN=$(jq -r .token <<<"$RESP")

# 2. Endpoints del cine (el Gateway valida el token y llama a este servicio por Eureka)
curl http://localhost:9000/api/v1/cinema/theater -H "Authorization: Bearer $TOKEN"
```

### Errores

Los errores se devuelven en JSON (`status`, `error`, `message` y `fieldErrors` para
validación):

- `400` — validación de campos o cuerpo malformado
- `401` — sin identidad (sin token en el Gateway, o llamada directa sin el secreto del Gateway)
- `404` — entidad no encontrada
- `201`, `200`, `204` — creación, lectura y borrado exitosos (los DELETE responden `204`)

## Migración de datos desde el esquema viejo

El refactor `Function → MovieScreening` y `Room → Theater` renombró tablas y columnas
(`functions` → `movie_screenings`, `rooms` → `theaters`, `room_id` → `theater_id`,
`function_id` → `movie_screening_id`).

- **Base nueva**: no hace falta nada; `ddl-auto=update` crea el esquema nuevo.
- **Base existente con data del esquema viejo**: aplicar `scripts/migrate_rename.sql`
  ANTES de levantar la app nueva (Hibernate no renombra tablas/columnas existentes).

Además, al mover los usuarios al Auth Service, `tickets.user_id` dejó de ser una FK:

- **Base existente**: eliminar la restricción vieja (`ALTER TABLE tickets DROP CONSTRAINT <fk>;`) y
  la tabla `users`, o empezar con una base nueva. Hibernate no borra constraints existentes y
  rechazaría los IDs del Auth Service.

## Integración con el ecosistema de microservicios

Este servicio es el **microservicio de negocio** de una arquitectura de microservicios
(UNAM, Facultad de Ciencias — proyecto de tesis). Los servicios hermanos son:

| Servicio | Puerto | Rol |
|---|---|---|
| `cinema_ciencias_registry_service` | 8761 | Eureka — servicio de registro/descubrimiento |
| `cinema_ciencias_config_service` | 8888 | Spring Cloud Config — configuración centralizada |
| `cinema_ciencias_gateway_service` | 9000 | API Gateway — valida el JWT y es la puerta de entrada única |
| `cinema_ciencias_auth_service` | 8082 | Usuarios, contraseñas y emisión del JWT |
| `audit-service` | 8081 | Auditoría de eventos (MariaDB) |
| **este servicio (`cinema-ciencias`)** | **8089** | Lógica de negocio del cine |

**Integraciones activas:**

- **Eureka** (8761): se registra como `CINEMA-CIENCIAS`; el Gateway lo localiza con
  `lb://cinema-ciencias`.
- **Config Server** (8888): obtiene `cinema.security.gatewaySecret` de `shared-config/application.yml`.
- **Audit Service** (8081): al crear un ticket, `AuditClient` hace `POST http://audit-service/api/logs`
  (el nombre se resuelve por Eureka con un `RestClient` `@LoadBalanced`). Si el audit service no está
  arriba, el error se captura y se loguea (no rompe el flujo).
- **Kafka** (9092): `TicketService` publica eventos al topic `created-tickets` y `TicketConsumer` los consume.

Para levantar **toda** la aplicación, usá el `README.md` de la raíz del proyecto
(`titulacion/`), que documenta el orden de arranque completo.
