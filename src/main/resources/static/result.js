document.addEventListener("DOMContentLoaded", () => {
    const modalCambioTarea = document.getElementById("modalCambioTarea");
    const idTareaExcedidaInput = document.getElementById("idTareaExcedida");
    const idProyectoInput = document.getElementById("idProyecto");
    const errorPopup = document.getElementById("errorPopup");

    // Función para mostrar el modal de cambio de tarea
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

    // Función para cerrar el popup de error manualmente
    window.cerrarErrorPopup = function () {
        if (errorPopup) {
            errorPopup.classList.remove("show");
            errorPopup.classList.add("hidden");
        }
    };

    // Mostrar automáticamente el popup de error si existe
    if (errorPopup) {
        errorPopup.classList.add("show");

        // Cerrar automáticamente después de 5 segundos
        setTimeout(() => {
            cerrarErrorPopup();
        }, 5000);
    }
});
