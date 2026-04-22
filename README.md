# Sistema de Turnos

Aplicación fullstack para la gestión de turnos, desarrollada con Java y Spring Boot en el backend y HTML/CSS/JavaScript en el frontend.

## Demo

🔗 [Ver aplicación en vivo](https://johanmnu.github.io/Sistema-turno/)

---

## Descripción

El sistema permite reservar, visualizar y cancelar turnos aplicando reglas de negocio para evitar conflictos de horario. Cada turno tiene una duración configurable y el sistema valida automáticamente que no se superpongan con turnos existentes del mismo día.

---

## Funcionalidades

- Reservar un turno indicando nombre del cliente, fecha, hora y duración
- Listar todos los turnos registrados
- Cancelar un turno por ID
- Validación de solapamiento: el sistema calcula el rango horario de cada turno y rechaza nuevas reservas que se superpongan con una existente
- Validación de fecha: no se permiten turnos en fechas pasadas
- Validación de duración: la duración debe ser mayor a 0 minutos
- Manejo de errores centralizado con respuestas HTTP descriptivas

---

## Tecnologías

**Backend**
- Java 17
- Spring Boot 3
- Spring Data JPA / Hibernate
- MySQL
- Maven

**Frontend**
- HTML5
- CSS3
- JavaScript (Fetch API)

---

## Arquitectura

```
src/
└── main/java/com/turnos/sistema_turnos/
    ├── controller/     # TurnoController — endpoints REST
    ├── service/        # TurnoService — lógica de negocio y validaciones
    ├── repository/     # TurnoRepository — acceso a datos con JPA
    ├── model/          # Turno — entidad persistida
    └── exception/      # Excepciones personalizadas y GlobalExceptionHandler
```

---

## Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/turnos` | Listar todos los turnos |
| POST | `/turnos` | Crear un nuevo turno |
| DELETE | `/turnos/{id}` | Eliminar un turno por ID |

### Ejemplo de body para POST `/turnos`

```json
{
  "nombreCliente": "Ana García",
  "fecha": "2025-06-15",
  "hora": "10:00",
  "duracionMinutos": 30
}
```

### Respuestas de error

| Código | Situación |
|--------|-----------|
| 400 | Duración inválida (≤ 0) |
| 404 | Turno no encontrado |
| 409 | Turno duplicado o solapamiento de horario |

---

## Cómo ejecutar localmente

### Requisitos

- Java 17+
- Maven
- MySQL

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/johanMnu/Sistema-turno.git
cd Sistema-turno

# 2. Configurar la base de datos en src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/sistema_turnos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update

# 3. Ejecutar el backend
./mvnw spring-boot:run

# 4. Abrir el frontend
# Abrir index.html en el navegador o acceder a la demo en vivo
```

---

## Lógica de solapamiento

El sistema no solo detecta turnos duplicados (misma fecha y hora exacta), sino que también calcula si un nuevo turno se superpone con uno existente según su duración. Por ejemplo, si hay un turno a las 10:00 de 60 minutos, no se puede reservar otro a las 10:30 aunque la hora exacta sea diferente.

```
Turno existente:  [10:00 ─────────── 11:00]
Intento nuevo:         [10:30 ──── 11:00]  ❌ rechazado
Intento nuevo:              [11:00 ──── 11:30]  ✅ aceptado
```
