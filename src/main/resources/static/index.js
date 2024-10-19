function hideAllSections() {
    document.getElementById('clientList').classList.add('hidden');
    document.getElementById('taskForm').classList.add('hidden');
    document.getElementById('taskList').classList.add('hidden');
    document.getElementById('ratingList').classList.add('hidden');
    document.getElementById('resultCalculation').classList.add('hidden');
}

function showProjectDetails(projectName) {
    const projectDetails = document.getElementById('projectDetails');
    const projectTitle = document.getElementById('projectTitle');

    if (projectTitle.innerText === projectName && !projectDetails.classList.contains('hidden')) {
        projectDetails.classList.add('hidden');
    } else {
        projectTitle.innerText = projectName;
        projectDetails.classList.remove('hidden');
        hideAllSections();
    }
}

document.getElementById('addClientBtn').addEventListener('click', function() {
    const clientList = document.getElementById('clientList');
    if (clientList.classList.contains('hidden')) {
        hideAllSections();
        clientList.classList.remove('hidden');
    } else {
        clientList.classList.add('hidden');
    }
});

document.getElementById('addTaskBtn').addEventListener('click', function() {
    const taskForm = document.getElementById('taskForm');
    if (taskForm.classList.contains('hidden')) {
        hideAllSections();
        taskForm.classList.remove('hidden');
    } else {
        taskForm.classList.add('hidden');
    }
});


document.getElementById('addBudgetBtn').addEventListener('click', function() {
    const budgetForm = document.getElementById('budgetForm');
    if (budgetForm.classList.contains('hidden')) {
        hideAllSections();
        budgetForm.classList.remove('hidden');
    } else {
        budgetForm.classList.add('hidden');
    }
});

document.getElementById('viewTasksBtn').addEventListener('click', function() {
    const taskList = document.getElementById('taskList');
    if (taskList.classList.contains('hidden')) {
        hideAllSections();
        taskList.classList.remove('hidden');
    } else {
        taskList.classList.add('hidden');
    }
});

document.getElementById('viewRatingsBtn').addEventListener('click', function() {
    const ratingList = document.getElementById('ratingList');
    if (ratingList.classList.contains('hidden')) {
        hideAllSections();
        ratingList.classList.remove('hidden');
    } else {
        ratingList.classList.add('hidden');
    }
});

document.getElementById('viewResultBtn').addEventListener('click', function() {
    const resultCalculation = document.getElementById('resultCalculation');
    if (resultCalculation.classList.contains('hidden')) {
        hideAllSections();
        resultCalculation.classList.remove('hidden');
    } else {
        resultCalculation.classList.add('hidden');
    }
});
