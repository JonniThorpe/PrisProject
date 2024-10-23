// Variable global para almacenar el ID del proyecto seleccionado
let selectedProjectId = null;

// Función para mostrar los detalles del proyecto
function showProjectDetails(projectName, projectId) {
    var projectDetailsSection = document.getElementById('projectDetails');
    projectDetailsSection.classList.remove('hidden');

    var projectTitle = document.getElementById('projectTitle');
    projectTitle.textContent = projectName;

    selectedProjectId = projectId;

    // Mostrar solo las tareas del proyecto seleccionado
    showProjectTasks(projectId);
}

// Función para mostrar solo las tareas correspondientes al proyecto seleccionado
function showProjectTasks(projectId) {
    var taskItems = document.querySelectorAll('#taskItems li');  // Selecciona todos los elementos de tarea

    taskItems.forEach(function (taskItem) {
        var taskProjectId = taskItem.getAttribute('data-project-id');
        if (taskProjectId === projectId.toString()) {
            taskItem.style.display = '';  // Mostrar tarea
        } else {
            taskItem.style.display = 'none';  // Ocultar tarea
        }
    });
}

// Añadir evento de clic a cada elemento de la lista de proyectos
document.addEventListener("DOMContentLoaded", function () {
    var projectItems = document.querySelectorAll('.project-item');

    projectItems.forEach(function (item) {
        item.addEventListener('click', function () {
            var projectName = item.textContent;
            var projectId = item.getAttribute('data-project-id');
            showProjectDetails(projectName, projectId);  // Mostrar los detalles del proyecto
        });
    });

    // Botón "Ver Tareas"
    document.getElementById('viewTasksBtn').addEventListener('click', function () {
        if (selectedProjectId !== null) {
            showProjectTasks(selectedProjectId);  // Filtrar tareas según el proyecto seleccionado
        } else {
            alert('Por favor, selecciona un proyecto primero.');
        }
    });
});

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

// Mostrar/Añadir tareas
document.getElementById('addClientBtn').addEventListener('click', function () {
    var taskForm = document.getElementById('clientForm');
    taskForm.classList.toggle('hidden');  // Alternar entre mostrar y ocultar
});

// Mostrar/Añadir tareas
document.getElementById('addTaskBtn').addEventListener('click', function () {
    var taskForm = document.getElementById('taskForm');
    taskForm.classList.toggle('hidden');  // Alternar entre mostrar y ocultar

    // Si hay un proyecto seleccionado, actualizar el campo hidden con el ID del proyecto
    if (selectedProjectId !== null) {
        document.getElementById('selectedProjectIdTask').value = selectedProjectId;
    } else {
        alert('Selecciona un proyecto antes de añadir una tarea');
    }
});


// Mostrar/Añadir presupuesto
document.getElementById('addBudgetBtn').addEventListener('click', function () {
    var budgetForm = document.getElementById('budgetForm');
    budgetForm.classList.toggle('hidden');  // Alternar entre mostrar y ocultar

    // Si hay un proyecto seleccionado, actualizar el campo hidden con el ID del proyecto
    if (selectedProjectId !== null) {
        document.getElementById('selectedProjectIdBudget').value = selectedProjectId;
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

});
