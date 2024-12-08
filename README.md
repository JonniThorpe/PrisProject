# ProTask

**ProTask** es una aplicación web diseñada para la gestión avanzada de proyectos y tareas, permitiendo un análisis profundo de las dependencias, contribuciones y cobertura de los requisitos. Este sistema ofrece a los administradores y clientes una herramienta poderosa para priorizar, valorar y optimizar tareas según las limitaciones de esfuerzo y las necesidades de los usuarios.

---

## 🚀 **Características principales**

1. **Gestión de Proyectos y Tareas**
    - Creación, edición y eliminación de proyectos y tareas.
    - Asignación de tareas a proyectos con información detallada de esfuerzo y satisfacción.

2. **Análisis de Resultados de Proyectos**
    - Clasificación automática de tareas en:
        - **Dentro del límite**: Tareas seleccionadas según las restricciones de esfuerzo.
        - **Fuera del límite**: Tareas excedidas tras el análisis.
    - Cálculo de:
        - **Productividad de la solución**: Relación entre satisfacción y esfuerzo.
        - **Contribución de los clientes**: Impacto ponderado de los clientes en la solución.
        - **Cobertura**: Nivel de satisfacción alcanzado por los requisitos de los clientes en la solución.
    - Visualización de resultados en tablas y gráficos interactivos:
        - **Gráficos de Contribuciones y Cobertura**: Muestra la distribución de valoraciones de los clientes en la solución.
        - **Gráficos de Productividad**: Representa la eficiencia de la solución en términos de satisfacción y esfuerzo.
        - **Gráficos de Tareas**: Muestra las tareas seleccionadas
        - **Gráficos de Clientes**: Muestra las contribuciones y cobertura de los clientes.
3. **Visualización de Dependencias**
    - Visualización jerárquica de las dependencias entre tareas mediante gráficos interactivos.

4. **Interacción Cliente-Proyecto**
    - Registro y valoración de tareas por parte de los clientes.
    - Asignación de pesos a clientes para evaluar su impacto en las decisiones del proyecto.

5. **Cálculos Automáticos**
    - Algoritmo para maximizar la productividad y priorizar tareas basado en el problema de la mochila.
    - Cálculo de contribuciones y cobertura con resultados en tiempo real.

---

## 📐 **Estructura del Proyecto**

### Backend
- **Framework**: Spring Boot
- **Bases de Datos**: MySQL
- **Repositorio**:
    - **`TareaRepository`**: Gestiona las consultas de tareas, incluyendo valoraciones ponderadas y dependencias.
    - **`ProyectoRepository`**: Proyectos y sus relaciones con los usuarios.
    - **`DependenciaRepository`**: Relaciones de dependencia entre tareas.
- **Controladores**:
    - `AdminController`: Gestión de panel administrativo y cálculos avanzados para resultados del proyecto.

### Frontend
- **Librerías**:
    - [Vis.js](https://visjs.github.io/vis-network/) para gráficos de dependencias.
    - Thymeleaf para la generación dinámica de vistas HTML.
    - [Highcharts](https://www.highcharts.com/) para gráficos interactivos.
- **Estructura**:
    - Vistas principales como `projectResults.html` muestran resultados detallados de proyectos, contribuciones, y cobertura en tablas y gráficos interactivos.
    - Modales dinámicos para mostrar gráficos y cálculos avanzados.

---

## 🧮 **Cálculos principales**

### Productividad
- **Fórmula**:  
  \[
  \text{Productividad} = \frac{\text{Suma de satisfacciones}}{\text{Suma de esfuerzos}}
  \]
- **Propósito**: Mide la eficiencia de la solución seleccionada en términos de satisfacción lograda por unidad de esfuerzo.

---

### Contribución de los Clientes
- **Fórmula**:  
  \[
  \text{Contribución} = \frac{\text{Peso del cliente} \times \text{Valoración al requisito}}{\text{Satisfacción total de la solución}}
  \]
- **Propósito**: Evalúa el impacto específico de cada cliente en la solución seleccionada.
### Cobertura
- **Fórmula**:
  \
  \text{Cobertura} = \frac{\text{Sumatoria de valoraciones del cliente en solución}}{\text{Sumatoria de valoraciones del cliente en todas las tareas}}
  \]
- **Propósito**: Determina qué tan bien se satisface cada cliente en función de sus propuestas totales.

---

## 🖼 **Visualización de Datos**
### Grafo de Dependencias
- Representa visualmente las relaciones jerárquicas entre tareas.
- Utiliza nodos para tareas y aristas para las dependencias.

### Tablas Dinámicas
- **Tareas dentro y fuera del límite**: Muestra detalles como ID, nombre, esfuerzo, satisfacción y productividad.
- **Contribuciones y Cobertura**: Presenta los cálculos avanzados para cada cliente.

---

## 🛠 **Requisitos del Sistema**

- **Java**: JDK 17 o superior
- **Maven**: 3.8.0 o superior
- **Base de Datos**: MySQL 8.0+
- **Node.js**: Para manejar librerías de frontend si se necesitan.

---

## 📚 **Cómo usar ProTask**

1. **Configuración Inicial**:
    - Clonar el repositorio y configurar `application.properties` con tus credenciales de base de datos.
    - Ejecutar las migraciones SQL para generar las tablas necesarias.

2. **Inicio del Servidor**:
    - Ejecutar el comando:
      ```bash
      mvn spring-boot:run
      ```
    - Acceder a la aplicación en `http://localhost:8080`.

3. **Características principales**:
    - Inicia sesión como administrador para gestionar proyectos y analizar tareas.
    - Permite que los clientes valoren tareas según sus prioridades.

---

## 💡 **Contribuciones**
Las contribuciones son bienvenidas. Si encuentras errores o deseas mejorar el sistema, crea un issue o envía un pull request.

---

## 🏗 **Futuras Mejoras**
- Integración de notificaciones en tiempo real para actualizaciones de proyectos.
- Implementación de un sistema de aprendizaje automático para predecir tareas críticas.
- Mejora de la interfaz de usuario y experiencia de usuario.
- Soporte para múltiples bases de datos y servicios en la nube.
- Integración con herramientas de gestión de proyectos como Jira y Trello.

---

**ProTask**: Optimizando la gestión de proyectos, una tarea a la vez.  
✨ ¡Gracias por usar ProTask! ✨
