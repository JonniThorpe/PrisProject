// Variable global para almacenar el ID del proyecto seleccionado
let selectedProjectId = null;

// Función para mostrar los detalles del proyecto
function showProjectDetails(projectName, projectId) {
    // Mostrar la sección de detalles del proyecto
    var projectDetailsSection = document.getElementById('projectDetails');
    projectDetailsSection.classList.remove('hidden');

    // Asignar el nombre del proyecto al título
    var projectTitle = document.getElementById('projectTitle');
    projectTitle.textContent = projectName;

    // Guardar el ID del proyecto seleccionado
    selectedProjectId = projectId;

    // Cargar las tareas del proyecto seleccionado
    loadTasksForProject(projectId);
}

// Función para cargar las tareas de un proyecto mediante AJAX
function loadTasksForProject(projectId) {
    // Realizar una petición para obtener las tareas del proyecto seleccionado
    fetch(`/admin/tasks?projectId=${projectId}`)
        .then(response => response.json())
        .then(data => {
            // Limpiar la lista de tareas anterior
            var taskItems = document.getElementById('taskItems');
            taskItems.innerHTML = '';

            // Si no hay tareas, mostrar un mensaje
            if (data.length === 0) {
                taskItems.innerHTML = '<li>No hay tareas para este proyecto.</li>';
            } else {
                // Recorrer las tareas y añadirlas a la lista
                data.forEach(task => {
                    var li = document.createElement('li');
                    li.textContent = task.nombre; // Nombre de la tarea
                    taskItems.appendChild(li);
                });
            }
        })
        .catch(error => {
            console.error('Error al cargar las tareas:', error);
        });
}

// Función para ocultar la sección de detalles del proyecto
function hideProjectDetails() {
    var projectDetailsSection = document.getElementById('projectDetails');
    projectDetailsSection.classList.add('hidden');
}

// Añadir evento de clic a cada elemento de la lista de proyectos
document.addEventListener("DOMContentLoaded", function () {
    var projectItems = document.querySelectorAll('.project-item');

    projectItems.forEach(function (item) {
        item.addEventListener('click', function () {
            var projectName = item.textContent;
            var projectId = item.getAttribute('data-project-id'); // Obtener el ID del proyecto desde el atributo data
            showProjectDetails(projectName, projectId);
        });
    });
});

// Mostrar/Añadir clientes (Esta función es un esqueleto que se puede expandir)
document.getElementById('addClientBtn').addEventListener('click', function () {
    var clientList = document.getElementById('clientList');
    clientList.classList.toggle('hidden');  // Alternar entre mostrar y ocultar
});

// Mostrar/Añadir tareas (Esta función es un esqueleto que se puede expandir)
document.getElementById('addTaskBtn').addEventListener('click', function () {
    var taskForm = document.getElementById('taskForm');
    taskForm.classList.toggle('hidden');  // Alternar entre mostrar y ocultar
});

// Mostrar/Añadir presupuesto (Esta función es un esqueleto que se puede expandir)
document.getElementById('addBudgetBtn').addEventListener('click', function () {
    var budgetForm = document.getElementById('budgetForm');
    budgetForm.classList.toggle('hidden');  // Alternar entre mostrar y ocultar

    // Si hay un proyecto seleccionado, actualizar el campo hidden con el ID del proyecto
    if (selectedProjectId !== null) {
        document.getElementById('selectedProjectId').value = selectedProjectId;
    } else {
        alert('Selecciona un proyecto antes de añadir un presupuesto');
    }
});

// Ver lista de tareas
document.getElementById('viewTasksBtn').addEventListener('click', function () {
    var taskList = document.getElementById('taskList');
    taskList.classList.toggle('hidden');  // Alternar entre mostrar y ocultar
});

// Ver lista de valoraciones
document.getElementById('viewRatingsBtn').addEventListener('click', function () {
    var ratingList = document.getElementById('ratingList');
    ratingList.classList.toggle('hidden');  // Alternar entre mostrar y ocultar
});

// Ver cálculo de resultado
document.getElementById('viewResultBtn').addEventListener('click', function () {
    var resultCalculation = document.getElementById('resultCalculation');
    resultCalculation.classList.toggle('hidden');  // Alternar entre mostrar y ocultar
});

// Redirigir a la página de detalles del proyecto cuando se selecciona uno
document.addEventListener("DOMContentLoaded", function () {
    var projectItems = document.querySelectorAll('.project-item');

    projectItems.forEach(function (item) {
        item.addEventListener('click', function () {
            var projectId = item.getAttribute('data-project-id'); // Obtener el ID del proyecto desde el atributo data

            // Redirigir a la URL que carga los detalles del proyecto
            window.location.href = `/admin/projectDetails?idProyecto=${projectId}`;
        });
    });
});
