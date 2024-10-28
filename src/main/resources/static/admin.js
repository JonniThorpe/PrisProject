// Variable global para almacenar el ID del proyecto seleccionado
let selectedProjectId = null;
let activeSection = null; // Variable para rastrear la sección o formulario activo

// Función para mostrar u ocultar la sección de detalles del proyecto
function showProjectDetails(projectName, projectId) {
    const projectDetailsSection = document.getElementById('projectDetails');
    if (!projectDetailsSection.classList.contains('hidden') && selectedProjectId === projectId) {
        projectDetailsSection.classList.add('hidden');
        selectedProjectId = null;
        document.getElementById('projectTitle').textContent = '';
    } else {
        projectDetailsSection.classList.remove('hidden');
        document.getElementById('projectTitle').textContent = projectName;
        document.getElementById('selectedProjectIdAssignClient').value = projectId;
        document.getElementById('selectedProjectIdTask').value = projectId;
        document.getElementById('selectedProjectIdBudget').value = projectId;
        selectedProjectId = projectId;
        showProjectTasks(projectId); // Asegura que se muestran solo las tareas del proyecto seleccionado
    }
}

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

// Función para mostrar solo las valoraciones del proyecto seleccionado
function showProjectRatings(projectId) {
    const ratingItems = document.querySelectorAll('.rating-item');
    ratingItems.forEach((ratingItem) => {
        const ratingProjectId = ratingItem.getAttribute('data-project-id');
        ratingItem.style.display = ratingProjectId === projectId.toString() ? '' : 'none';
    });
}

// Función para alternar la visibilidad de una sección, asegurando que solo una esté visible
function toggleSection(section) {
    if (activeSection && activeSection !== section) {
        activeSection.classList.add('hidden'); // Oculta la sección activa anterior
    }
    if (section.classList.contains('hidden')) {
        section.classList.remove('hidden');
        activeSection = section; // Actualiza la sección activa
    } else {
        section.classList.add('hidden'); // Oculta la sección si se hace clic nuevamente
        activeSection = null; // Reinicia la sección activa
    }
}

// Configuración de eventos y lógica
document.addEventListener("DOMContentLoaded", function () {
    const projectItems = document.querySelectorAll('.project-item');
    projectItems.forEach((item) => {
        item.addEventListener('click', function () {
            const projectName = item.textContent;
            const projectId = item.getAttribute('data-project-id');
            showProjectDetails(projectName, projectId);
        });
    });

    // Botón "Asignar Cliente a Proyecto"
    document.getElementById('assignProjectClientBtn').addEventListener('click', function () {
        const assignClientForm = document.getElementById('assignClientForm');
        toggleSection(assignClientForm);

        if (selectedProjectId === null) {
            alert('Selecciona un proyecto antes de asignar clientes.');
        } else {
            document.getElementById('selectedProjectIdAssignClient').value = selectedProjectId;
        }
    });

    // Mostrar/Añadir cliente
    document.getElementById('addClientBtn').addEventListener('click', function () {
        toggleSection(document.getElementById('clientForm'));
    });

    // Mostrar/Añadir tarea
    document.getElementById('addTaskBtn').addEventListener('click', function () {
        const taskForm = document.getElementById('taskForm');
        toggleSection(taskForm);
        if (selectedProjectId === null) {
            alert('Selecciona un proyecto antes de añadir una tarea.');
        }
    });

    // Mostrar/Añadir presupuesto
    document.getElementById('addBudgetBtn').addEventListener('click', function () {
        const budgetForm = document.getElementById('budgetForm');
        toggleSection(budgetForm);
        if (selectedProjectId === null) {
            alert('Selecciona un proyecto antes de añadir un presupuesto.');
        }
    });

    // Ver lista de tareas
    document.getElementById('viewTasksBtn').addEventListener('click', function () {
        toggleSection(document.getElementById('taskList'));
    });

    // Ver lista de valoraciones
    document.getElementById('viewRatingsBtn').addEventListener('click', function () {
        if (selectedProjectId === null) {
            alert('Selecciona un proyecto antes de ver las valoraciones.');
        } else {
            showProjectRatings(selectedProjectId);
            toggleSection(document.getElementById('ratingList'));
        }
    });

    // Ver cálculo de resultado
    document.getElementById('viewResultBtn').addEventListener('click', function () {
        toggleSection(document.getElementById('resultCalculation'));
    });
});
