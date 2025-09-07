# ✈️ Aerovía API — Monolito Spring Boot (Java 17, Maven, MySQL)

Bienvenido/a. Este proyecto es un **API REST monolítico** organizado por **subdominios** bajo `src/main/java/com/tuorg/aerovia/modules`, y usa **capas** claras (API → Service → Repository → Domain).

## 🧱 Stack
- **Java 17**
- **Spring Boot 3.5.5**
- **Maven**
- **MySQL** con JPA/Hibernate
- Validación **Jakarta**
- **Flyway** o **Liquibase** (elige uno para migraciones)
- **JUnit 5** / Mockito / (opcional) Testcontainers
- (Opcional) **MapStruct** para mappers

---

## 📂 Estructura del repo (base acordada)

```

aerovia-api/
├─ pom.xml
├─ README.md
├─ .gitignore
├─ .editorconfig
├─ docs/
│  ├─ arquitectura.md
│  ├─ decisiones-ADR/
│  └─ api-contracts/openapi.yaml
├─ config/
│  ├─ application.yml
│  ├─ application-dev.yml
│  ├─ application-prod.yml
│  └─ liquibase/ | flyway/
│     └─ db.changelog-\*.yaml | db/migration/V1\_\_init.sql
├─ docker/
│  ├─ mysql/docker-compose.yml
│  └─ app/Dockerfile
└─ src/
├─ main/
│  ├─ java/com/tuorg/aerovia/
│  │  ├─ AeroviaApplication.java
│  │  ├─ config/                 # CORS, Jackson, OpenAPI, seguridad básica
│  │  ├─ common/
│  │  │  ├─ exception/           # GlobalExceptionHandler, errores de dominio
│  │  │  ├─ mapper/              # (opcional) MapStruct
│  │  │  ├─ validation/          # validadores (IATA, etc.)
│  │  │  └─ util/
│  │  │     └─ correlativo/      # util para correlativos
│  │  └─ modules/                # 1 carpeta por subdominio
│  │     ├─ usuarios/            # api/ dto/ domain/ service/ repository/
│  │     ├─ aerolineas/          # api/ dto/ domain/ service/ repository/
│  │     ├─ aeropuertos/         # Aeropuerto, Ruta
│  │     ├─ flota/               # Avion, AsientoAvion, Clase
│  │     ├─ tripulacion/         # Tripulante, OperacionTripulacion
│  │     ├─ vuelos/              # Vuelo (catálogo)
│  │     ├─ operaciones/         # OperacionVuelo
│  │     ├─ tarifas/             # Tarifa, TarifaOperacion
│  │     ├─ pasajeros/           # Pasajero
│  │     ├─ reservas/            # Reserva, ReservaAsiento, Cancelacion
│  │     └─ pagos/               # Pago
│  └─ resources/
│     ├─ messages.properties
│     └─ logback-spring.xml
└─ test/
├─ java/com/tuorg/aerovia/
│  ├─ common/
│  ├─ modules/... (mirroring)
│  └─ integration/
└─ resources/application-test.yml

```

---

## 🧭 Capas (qué hace cada una)

- **api/** (controllers REST): recibe HTTP, valida DTOs, llama a servicios. **No** tiene reglas de negocio.
- **service/** (lógica de aplicación): orquesta casos de uso, transacciones, idempotencia, correlativos.
- **repository/** (datos): acceso a MySQL con JPA/JPQL/SQL. **No** contiene reglas de negocio.
- **domain/** (modelo): entidades, enums e invariantes (reglas intrínsecas del modelo).
- **common/**: utilidades transversales (excepciones, validaciones, mappers, correlativos).
- **config/**: configuración de CORS, Jackson, OpenAPI/Swagger y seguridad básica.

**Dependencias permitidas**: `api → service → repository → domain`.  
`common/` y `config/` solo como apoyo (evitar ciclos).

---

## 🧩 Convención por módulo (`src/main/java/com/tuorg/aerovia/modules/<modulo>/`)
```

<modulo>/
├─ api/         # Controllers (HTTP/REST)
├─ dto/         # Request/Response (objetos de intercambio)
├─ domain/      # Entidades JPA + enums + invariantes
├─ service/     # Casos de uso y transacciones
└─ repository/  # Spring Data JPA

```

**Ejemplos de módulos**: `usuarios/`, `aerolineas/`, `aeropuertos/`, `flota/`, `tripulacion/`, `vuelos/`, `operaciones/`, `tarifas/`, `pasajeros/`, `reservas/`, `pagos/`.

---

## 🔢 Correlativos (common/util/correlativo/)
- Útil para generar códigos legibles, p. ej.: `codigoReserva = RES-YYYYMMDD-000123`.
- **Estrategia**: tabla `correlativo`/`secuencias` con PK `(tipo, scope_hash)` y `valor_actual`.
- Incremento atómico con `SELECT ... FOR UPDATE` (evita choques en concurrencia).
- Tipos sugeridos: `RESERVA`, `PAGO`, `RECLAMO`, etc.  
- Se invoca desde la capa **service** del módulo que lo necesite (antes de persistir).

---

## 🔁 Flujo ejemplo (POST /reservas)
1. `reservas/api` recibe `ReservaRequest` (DTO) y valida.
2. `reservas/service`:
   - Verifica `OperacionVuelo` activa, asientos y cupos (`TarifaOperacion`).
   - Pide correlativo a `common/util/correlativo`.
   - Persiste `Reserva` + `ReservaAsiento` y actualiza cupos (transacción).
3. Devuelve `ReservaResponse` con `codigoReserva`, `estado`, `total`.

---

## 🗃️ Migraciones y configuración
- Usa **Flyway** (`src/main/resources/db/migration/V1__init.sql`) o **Liquibase** (`config/liquibase/…`).
- Perfiles: `application.yml` (base), `application-dev.yml`, `application-prod.yml`.
- Secretos (DB URL, usuario, password) por **variables de entorno**.

---

## 🧪 Testing (mínimo saludable)
- **Unit**: servicios y mappers.
- **Slice**: `@WebMvcTest` (api), `@DataJpaTest` (repository).
- **Integración**: contexto completo + (opcional) **Testcontainers** con MySQL.

---

## ➕ Agregar un módulo nuevo (checklist)
1. Crear `modules/<nuevo>/{domain,repository,dto,service,api}`.
2. Añadir migración: `Vn__<cambio>.sql`.
3. Exponer endpoints en `docs/api-contracts/openapi.yaml`.
4. Tests: unit + slice (+ integración si aplica).
5. Actualizar `docs/arquitectura.md` y, si procede, ADR en `docs/decisiones-ADR/`.

---

## 📚 Glosario ultra-junior
- **DTO**: “caja” para datos que entran/salen por la API (no es la entidad de BD).
- **Entidad**: clase que se guarda en la BD (tablas).
- **Repository**: “puerta” para hablar con la BD (CRUD).
- **Service**: “cerebro” que decide el paso a paso de un caso de uso.
- **Controller**: “recepcionista” que atiende llamadas HTTP y pasa el pedido al Service.
```