document.addEventListener("DOMContentLoaded", () => {
    const modalCambioTarea = document.getElementById("modalCambioTarea");
    const idTareaExcedidaInput = document.getElementById("idTareaExcedida");
    const idProyectoInput = document.getElementById("idProyecto");

    // Función para mostrar el modal
    window.mostrarCambioTarea = function (idTareaExcedida, idProyecto) {
        if (!idTareaExcedida || !idProyecto) {
            console.error("ID de tarea excedida o proyecto no proporcionado.");
            return;
        }

        // Establece los IDs en los campos ocultos
        idTareaExcedidaInput.value = idTareaExcedida;
        idProyectoInput.value = idProyecto;

        // Cambia el estado del modal a visible
        modalCambioTarea.classList.remove("hidden");
        modalCambioTarea.classList.add("show");
    };

    // Función para cerrar el modal
    window.cerrarModal = function () {
        modalCambioTarea.classList.remove("show");
        modalCambioTarea.classList.add("hidden");
    };

    // Cerrar el modal si se hace clic fuera del contenido
    modalCambioTarea.addEventListener("click", (event) => {
        if (event.target === modalCambioTarea) {
            cerrarModal();
        }
    });
});
