const boton = document.getElementById("btnCargar");
const lista = document.getElementById("listaTurnos");
const form = document.getElementById("formTurno");
const mensaje = document.getElementById("mensaje");

const API_URL = "http://localhost:8080/turnos";

// ==========================
// CARGAR TURNOS
// ==========================
boton.addEventListener("click", cargarTurnos);

function cargarTurnos() {
  fetch(API_URL)
    .then(response => response.json())
    .then(data => {
      lista.innerHTML = "";
      data.forEach(turno => {
        agregarTurnoALaLista(turno);
      });
    })
    .catch(error => {
      console.error("Error:", error);
    });
}

// ==========================
// CREAR TURNO
// ==========================
form.addEventListener("submit", (event) => {
  event.preventDefault();

  const turno = {
    nombreCliente: document.getElementById("nombre").value,
    fecha: document.getElementById("fecha").value,
    hora: document.getElementById("hora").value,
    duracionMinutos: parseInt(document.getElementById("duracion").value),
    estado: "RESERVADO"
  };

  fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(turno)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Error al crear el turno");
      }
      return response.json();
    })
    .then(data => {
    mostrarMensaje("Turno creado correctamente", "mensaje-exito");
      agregarTurnoALaLista(data);
      form.reset();
    })
    .catch(error => {
     mostrarMensaje(error.message, "mensaje-error");

    });
});

// ==========================
// MOSTRAR TURNO + BOTÓN ELIMINAR
// ==========================
function agregarTurnoALaLista(turno) {
  const li = document.createElement("li");

  li.innerHTML = `
    <strong>${turno.nombreCliente}</strong> <br>
    Fecha: ${turno.fecha} <br>
    Hora: ${turno.hora} <br>
    Estado: ${turno.estado} <br>
  `;

  const btnEliminar = document.createElement("button");
  btnEliminar.textContent = "Eliminar";

  btnEliminar.addEventListener("click", () => {
    eliminarTurno(turno.id);
  });

  li.appendChild(btnEliminar);
  lista.appendChild(li);
}

// ==========================
// ELIMINAR TURNO
// ==========================
function eliminarTurno(id) {
  const confirmar = confirm("¿Seguro que querés eliminar este turno?");

  if (!confirmar) return;

  fetch(`${API_URL}/${id}`, {
    method: "DELETE"
  })
    .then(() => {
       mostrarMensaje("Turno eliminado correctamente", "mensaje-exito");
      cargarTurnos();
    })
    .catch(error => {
      mostrarMensaje("Error al eliminar el turno", "mensaje-error");
    });
}
function mostrarMensaje(texto, tipo) {
  mensaje.textContent = texto;
  mensaje.className = "";
  mensaje.classList.add(tipo);
  mensaje.style.display = "block";

  setTimeout(() => {
    mensaje.style.display = "none";
  }, 3000);
}
