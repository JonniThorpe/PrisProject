// Variable global para almacenar el ID del proyecto seleccionado
let selectedProjectId = null;

// Función para mostrar los detalles del proyecto
function showProjectDetails(projectName, projectId) {
    // Mostrar la sección de detalles del proyecto
    var projectDetailsSection = document.getElementById('projectDetails');
    projectDetailsSection.classList.remove('hidden'); // Mostrar los detalles

    // Asignar el nombre del proyecto al título
    var projectTitle = document.getElementById('projectTitle');
    projectTitle.textContent = projectName;

    // Guardar el ID del proyecto seleccionado
    selectedProjectId = projectId;

    // Cargar las tareas del proyecto seleccionado en el formulario de valoración
    loadTasksForProject(projectId);
    showTasksForProject(projectId);
}

// Añadir evento de clic a cada elemento de la lista de proyectos
document.addEventListener("DOMContentLoaded", function () {
    var projectItems = document.querySelectorAll('.project-item'); // Seleccionar todos los proyectos

    projectItems.forEach(function (item) {
        item.addEventListener('click', function () {
            var projectName = item.textContent;
            var projectId = item.getAttribute('data-project-id'); // Obtener el ID del proyecto desde el atributo data

            // Mostrar los detalles del proyecto al hacer clic
            showProjectDetails(projectName, projectId);
        });
    });
});

// Función para cargar las tareas del proyecto seleccionado en el desplegable
function loadTasksForProject(projectId) {
    const taskSelect = document.getElementById('taskSelect');

    // Seleccionar todas las opciones de tareas en el HTML
    const allTasks = document.querySelectorAll('#taskSelect option[data-project-id]');

    // Filtrar y mostrar solo las tareas que coinciden con el proyecto seleccionado
    allTasks.forEach(task => {
        if (task.getAttribute('data-project-id') === projectId) {
            taskSelect.appendChild(task); // Añadir al desplegable
        }
    });
}

// Función para mostrar las tareas del proyecto seleccionado en la lista
function showTasksForProject(projectId) {
    const taskList = document.getElementById('taskList');
    const allTasks = document.querySelectorAll('#taskList li');

    // Ocultar todas las tareas primero
    allTasks.forEach(task => {
        task.classList.add('hidden');
    });

    // Mostrar solo las tareas que coinciden con el proyecto seleccionado
    allTasks.forEach(task => {
        if (task.getAttribute('data-project-id') === projectId) {
            task.classList.remove('hidden'); // Mostrar la tarea
        }
    });
}

// Mostrar el formulario para valorar tareas
document.getElementById('addRatingBtn').addEventListener('click', function () {
    var ratingForm = document.getElementById('ratingForm');
    ratingForm.classList.toggle('hidden');  // Alternar entre mostrar y ocultar

    // Si no hay proyecto seleccionado, mostrar una alerta
    if (selectedProjectId === null) {
        alert('Selecciona un proyecto antes de valorar una tarea');
    }
});

// Función para manejar la selección de la valoración
document.querySelectorAll('.rating-btn').forEach(button => {
    button.addEventListener('click', function () {
        // Quitar la clase activa de otros botones
        document.querySelectorAll('.rating-btn').forEach(btn => btn.classList.remove('active'));

        // Añadir la clase activa al botón seleccionado
        this.classList.add('active');

        // Guardar el valor seleccionado en el input hidden
        document.getElementById('valoracionInput').value = this.getAttribute('data-value');
    });
});

// Mostrar la lista de tareas
document.getElementById('viewTasksBtn').addEventListener('click', function () {
    var taskList = document.getElementById('taskList');
    taskList.classList.toggle('hidden');  // Alternar entre mostrar y ocultar
});
