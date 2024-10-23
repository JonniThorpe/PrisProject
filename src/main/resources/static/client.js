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

// Función para ocultar la sección de detalles del proyecto
function hideProjectDetails() {
    var projectDetailsSection = document.getElementById('projectDetails');
    projectDetailsSection.classList.add('hidden');
}

// Mostrar/Ocultar la lista de valoraciones (ejemplo de cómo se pueden mostrar otras secciones)
document.getElementById('viewRatingsBtn').addEventListener('click', function () {
    var ratingList = document.getElementById('ratingList');
    ratingList.classList.toggle('hidden');  // Alternar entre mostrar y ocultar
});

// Mostrar/Ocultar el cálculo de resultados
document.getElementById('viewResultBtn').addEventListener('click', function () {
    var resultCalculation = document.getElementById('resultCalculation');
    resultCalculation.classList.toggle('hidden');  // Alternar entre mostrar y ocultar
});
