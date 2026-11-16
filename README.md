# Sistema de Turnos

Una aplicación que hice para practicar desarrollo backend con Java y Spring Boot, trabajando con un problema concreto: que dos personas no puedan reservar un turno en horarios que se pisan.

El backend expone una API REST y el repositorio incluye una interfaz sencilla en HTML, CSS y JavaScript para probar las operaciones.

## Qué se puede hacer

- Registrar turnos con nombre, fecha, hora de inicio y duración.
- Consultar los turnos registrados.
- Eliminar turnos por identificador.
- Rechazar turnos en fechas pasadas, duraciones no válidas y reservas con horarios superpuestos.
- Recibir respuestas de error HTTP cuando una operación no es válida.

### Una regla de negocio que me interesó resolver

No alcanza con comprobar que dos turnos tengan la misma hora de inicio: también hay que considerar cuánto dura cada uno.

Por ejemplo, si existe un turno de **10:00 a 11:00**, no se puede reservar otro a las **10:30**. En cambio, un turno que empieza a las **11:00** no se superpone.

La comprobación está implementada en `TurnoService`: se buscan los turnos de la misma fecha y se comparan sus intervalos horarios.

## Tecnologías

- **Backend:** Java 17, Spring Boot 4.0.1, Spring Data JPA, Hibernate.
- **Base de datos:** MySQL.
- **Frontend:** HTML, CSS y JavaScript (Fetch API).
- **Herramientas:** Maven (incluye Maven Wrapper).

## Organización del backend

| Paquete | Responsabilidad |
| --- | --- |
| `controller` | Recibe solicitudes HTTP y expone los endpoints |
| `service` | Aplica las validaciones y reglas de negocio |
| `repository` | Acceso a la base de datos con Spring Data JPA |
| `model` | Entidad `Turno` |
| `exception` | Excepciones y respuestas de error |

## API

| Método | Ruta | Acción |
| --- | --- | --- |
| `GET` | `/turnos` | Listar turnos |
| `POST` | `/turnos` | Registrar un turno |
| `DELETE` | `/turnos/{id}` | Eliminar un turno |

Ejemplo para `POST /turnos`:

```json
{
  "nombreCliente": "Ana García",
  "fecha": "2027-06-15",
  "hora": "10:00",
  "duracionMinutos": 30
}
```

El manejo global de errores devuelve, entre otras respuestas, **400** para una duración inválida, **404** cuando no existe el turno a eliminar y **409** para horarios ocupados o duplicados.

## Cómo ejecutarlo localmente

Necesitás **Java 17 o superior**, **MySQL** y una terminal. El repositorio incluye `mvnw` / `mvnw.cmd`, así que no es obligatorio instalar Maven por separado.

1. Cloná el repositorio:

```bash
git clone https://github.com/johanMnu/Sistema-turno.git
cd Sistema-turno
```

2. Creá una base de datos local llamada `sistema_turnos`:

```sql
CREATE DATABASE sistema_turnos;
```

3. Configurá la conexión con variables de entorno. El archivo `application.properties` lee `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`. Si usás la URL y el usuario predeterminados, solo tenés que definir la contraseña.

**PowerShell (Windows):**
```powershell
$env:DB_PASSWORD = "tu_contrasena_local"
./mvnw.cmd spring-boot:run
```

**Terminal Bash (Linux/macOS):**
```bash
export DB_PASSWORD="tu_contrasena_local"
./mvnw spring-boot:run
```

Si tu usuario de MySQL no es `root`, definí también `DB_USERNAME`. Si la base está en otro servidor, definí `DB_URL`.

> No subas contraseñas reales al repositorio. El valor usado arriba es solo un ejemplo que tenés que reemplazar por tu propia contraseña local.

4. Probá la API con Postman o un cliente HTTP. También podés abrir el archivo `index.html` del repositorio para ver la interfaz.

## Demo del frontend

[Ver interfaz publicada con GitHub Pages](https://johanmnu.github.io/Sistema-turno/)

**Aclaración:** GitHub Pages publica los archivos estáticos del frontend, pero no ejecuta el backend Java ni aloja la base MySQL. Para crear y consultar turnos mediante la interfaz, la API debe estar disponible y configurada correctamente.

## Qué aprendí

Este proyecto me sirvió especialmente para separar la lógica de negocio del controlador, trabajar con persistencia y pensar en casos que no se resuelven con una simple validación de campos. La detección de turnos superpuestos fue una parte importante de ese aprendizaje.

Es un proyecto de práctica y todavía tiene margen para mejorar, por ejemplo con pruebas automatizadas y validaciones adicionales.
