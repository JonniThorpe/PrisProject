document.addEventListener("DOMContentLoaded", () => {
    const modalCambioTarea = document.getElementById("modalCambioTarea");
    const idTareaExcedidaInput = document.getElementById("idTareaExcedida");

    // Función para mostrar el modal
    window.mostrarCambioTarea = function (idTareaExcedida) {
        if (!idTareaExcedida) {
            console.error("ID de tarea excedida no proporcionado.");
            return;
        }

        // Establece el ID de la tarea excedida en el campo oculto
        idTareaExcedidaInput.value = idTareaExcedida;

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
