document.addEventListener("DOMContentLoaded", () => {
    const errorPopup = document.getElementById("errorPopup");

    // Función para cerrar el popup de error
    window.cerrarErrorPopup = function () {
        if (errorPopup) {
            errorPopup.classList.remove("show");
            errorPopup.classList.add("hidden");
        }
    };

    if (errorPopup) {
        errorPopup.classList.add("show");
        setTimeout(() => {
            cerrarErrorPopup();
        }, 5000);
    }

    // Modal para contribuciones
    const modalVerContribucion = document.getElementById("modalVerContribucion");
    const modalOverlay = document.getElementById("modalOverlay");

    window.mostrarModalContribucion = function () {
        modalVerContribucion.classList.add("show");
        modalOverlay.classList.add("show");
    };

    window.cerrarModalContribucion = function () {
        modalVerContribucion.classList.remove("show");
        modalOverlay.classList.remove("show");
    };

    modalOverlay.addEventListener("click", cerrarModalContribucion);

    // Modal para contribuciones en solución
    const modalVerContribucionSolucion = document.getElementById("modalVerContribucionSolucion");
    const modalOverlayContribucion = document.getElementById("modalOverlayContribucion");

    window.mostrarModalContribucionSolucion = function () {
        modalVerContribucionSolucion.classList.add("show");
        modalOverlayContribucion.classList.add("show");
    };

    window.cerrarModalContribucionSolucion = function () {
        modalVerContribucionSolucion.classList.remove("show");
        modalOverlayContribucion.classList.remove("show");
    };

    modalOverlay.addEventListener("click", cerrarModalContribucionSolucion);

    // Modal para cobertura
    const modalVerCobertura = document.getElementById("modalVerCobertura");
    const modalOverlayCobertura = document.getElementById("modalOverlayCobertura");

    window.mostrarModalCobertura = function () {
        modalVerCobertura.classList.add("show");
        modalOverlayCobertura.classList.add("show");
    };

    window.cerrarModalCobertura = function () {
        modalVerCobertura.classList.remove("show");
        modalOverlayCobertura.classList.remove("show");
    };

    modalOverlay.addEventListener("click", cerrarModalCobertura);

    // Modal para grafo de tareas dentro del límite
    window.mostrarModalGrafo = function () {
        const modalVerGrafo = document.getElementById("modalVerGrafo");
        const modalOverlay2 = document.getElementById("modalOverlay2");
        const grafoContainer = document.getElementById("grafoContainer");

        // Mostrar el modal
        modalVerGrafo.classList.remove("hidden");
        modalOverlay2.classList.remove("hidden");

        try {
            // Leer los datos JSON del elemento <script>
            const tareas = JSON.parse(document.getElementById("tareasData").textContent);

            // Crear nodos a partir de las tareas
            const nodes = tareas.map(tarea => ({
                id: tarea.idTarea,
                label: `${tarea.nombreTarea}\nValoración: ${tarea.valoracionPonderada}`,
            }));

            // Crear aristas (enlazar los nodos en orden)
            const edges = [];
            for (let i = 0; i < nodes.length - 1; i++) {
                edges.push({ from: nodes[i].id, to: nodes[i + 1].id });
            }

            // Configurar los datos y las opciones del grafo
            const data = {
                nodes: new vis.DataSet(nodes),
                edges: new vis.DataSet(edges),
            };

            const options = {
                nodes: {
                    shape: 'dot',
                    size: 25,
                    color: {
                        background: '#ecddb3',
                        border: '#554b3b',
                        highlight: {
                            background: '#a69d81',
                            border: '#403935'
                        }
                    },
                    font: {
                        color: '#000000',
                        size: 14,
                        face: 'Verdana'
                    }
                },
                edges: {
                    color: {
                        color: '#000000',
                        highlight: '#575757',
                        hover: '#5e5e5e'
                    },
                    width: 2,
                    smooth: true,
                    arrows: { to: true }
                },
                layout: {
                    hierarchical: {
                        direction: 'LR',
                        nodeSpacing: 100
                    }
                },
                physics: {
                    enabled: false // Desactivar física para que se vea más estático
                }
            };

            // Limpiar el contenedor antes de renderizar
            grafoContainer.innerHTML = "";
            new vis.Network(grafoContainer, data, options);
        } catch (error) {
            console.error("Error al renderizar el grafo:", error);
        }
    };

    // Función para cerrar el modal
    window.cerrarModalGrafo = function () {
        document.getElementById("modalVerGrafo").classList.add("hidden");
        document.getElementById("modalOverlay2").classList.add("hidden");
    };

    // Cerrar el modal si se hace clic en el overlay
    document.getElementById("modalOverlay2").addEventListener("click", cerrarModalGrafo);

    // Modal para grafo de dependencias
    const modalDependenciasGrafo = document.getElementById("modalDependenciasGrafo");
    const modalDependenciasOverlay = document.getElementById("modalDependenciasOverlay");
    const dependenciasGrafoContainer = document.getElementById("dependenciasGrafoContainer");

    window.mostrarModalDependenciasGrafo = function () {
        modalDependenciasGrafo.classList.remove("hidden");
        modalDependenciasOverlay.classList.remove("hidden");

        try {
            // Leer los datos JSON del elemento <script>
            const dependencias = JSON.parse(document.getElementById("dependenciasData").textContent);

            // Crear nodos únicos a partir de las dependencias
            const nodes = Array.from(
                new Set(
                    dependencias.flatMap(dep => [dep.from, dep.to])
                )
            ).map(id => ({
                id: id,
                label: `Tarea ${id}`,
            }));

            // Configurar los datos y las opciones del grafo
            const data = {
                nodes: new vis.DataSet(nodes),
                edges: new vis.DataSet(dependencias),
            };

            const options = {
                nodes: {
                    shape: 'dot',
                    size: 20,
                    color: {
                        background: '#ecddb3',
                        border: '#554b3b',
                        highlight: {
                            background: '#a69d81',
                            border: '#403935',
                        },
                    },
                    font: {
                        color: '#000000',
                        size: 14,
                        face: 'Verdana',
                    },
                },
                edges: {
                    color: {
                        color: '#000000',
                        highlight: '#575757',
                        hover: '#5e5e5e',
                    },
                    width: 2,
                    smooth: true,
                    arrows: { to: true },
                },
                layout: {
                    hierarchical: {
                        direction: 'LR',
                        nodeSpacing: 100,
                    },
                },
                physics: {
                    enabled: false, // Desactivar física para una estructura más estática
                },
            };

            // Limpiar el contenedor antes de renderizar
            dependenciasGrafoContainer.innerHTML = "";
            new vis.Network(dependenciasGrafoContainer, data, options);
        } catch (error) {
            console.error("Error al renderizar el grafo de dependencias:", error);
        }
    };

    window.cerrarModalDependenciasGrafo = function () {
        modalDependenciasGrafo.classList.add("hidden");
        modalDependenciasOverlay.classList.add("hidden");
    };

    modalDependenciasOverlay.addEventListener("click", cerrarModalDependenciasGrafo);

    // Modal para cobertura
    const modalVerDistribucion = document.getElementById("modalVerDistribucion");
    const modalDistribucionOverlay = document.getElementById("modalDistribucionOverlay");

    window.mostrarModalDistribucion = function () {
        modalVerDistribucion.classList.add("show");
        modalDistribucionOverlay.classList.add("show");
    };

    window.cerrarModalDistribucion = function () {
        modalVerDistribucion.classList.remove("show");
        modalDistribucionOverlay.classList.remove("show");
    };

    modalOverlay.addEventListener("click", cerrarModalDistribucion);

    // Modal para cobertura
    const modalVerContribuciones = document.getElementById("modalVerContribuciones");
    const modalGraficoContribucionesOverlay = document.getElementById("modalGraficoContribucionesOverlay");

    window.mostrarModalContribucionGrafico = function () {
        modalVerContribuciones.classList.add("show");
        modalGraficoContribucionesOverlay.classList.add("show");
    };

    window.cerrarModalContribucionGrafico = function () {
        modalVerContribuciones.classList.remove("show");
        modalGraficoContribucionesOverlay.classList.remove("show");
    };

    modalGraficoContribucionesOverlay.addEventListener("click", cerrarModalContribucionGrafico);

    // Modal para Productividad
    const modalVerProductividad = document.getElementById("modalVerProductividad");
    const modalProductividadOverlay = document.getElementById("modalProductividadOverlay");

    window.mostrarModalProductividad = function () {
        modalVerProductividad.classList.add("show");
        modalProductividadOverlay.classList.add("show");
    }
    window.cerrarModalProductividad = function () {
        modalVerProductividad.classList.remove("show");
        modalProductividadOverlay.classList.remove("show");
    };

    modalProductividadOverlay.addEventListener("click", cerrarModalContribucionGrafico);


    // Modal para Esfuerzo vs Satisfacción
    const modalVerEsfuerzoVsSatisfaccion = document.getElementById("modalVerEsfuerzoVsSatisfaccion");
    const modalEsfuerzoVsSatisfaccionOverlay = document.getElementById("modalEsfuerzoVsSatisfaccionOverlay");

    window.mostrarModalEsfuerzoVsSatisfaccion = function () {
        modalVerEsfuerzoVsSatisfaccion.classList.add("show");
        modalEsfuerzoVsSatisfaccionOverlay.classList.add("show");
    }
    window.cerrarModalEsfuerzoVsSatisfaccion = function () {
        modalVerEsfuerzoVsSatisfaccion.classList.remove("show");
        modalEsfuerzoVsSatisfaccionOverlay.classList.remove("show");
    };

    modalEsfuerzoVsSatisfaccionOverlay.addEventListener("click", cerrarModalEsfuerzoVsSatisfaccion);

    try {
        const graficosData = JSON.parse(document.getElementById("graficosData").textContent);
        const productividadData = graficosData.productividadTareas;
        const esfuerzoSatisfaccionData = graficosData.productividadTareas;
        console.log("Datos de los gráficos:", graficosData);

            // Gráfico de distribución
            Highcharts.chart('graficoDistribucion', {
                chart: { type: 'pie' },
                title: { text: 'Distribución de Tareas' },
                series: [{
                    name: 'Tareas',
                    data: [
                        { name: 'Dentro del Límite', y: graficosData.tareasDistribucion.dentroLimite },
                        { name: 'Excedidas', y: graficosData.tareasDistribucion.excedidas }
                    ]
                }]
            });

            // Gráfico de contribuciones
            Highcharts.chart('graficoContribuciones', {
                chart: { type: 'column' },
                title: { text: 'Contribuciones de los Clientes' },
                xAxis: { categories: graficosData.contribucionesClientes.map(c => c.cliente) },
                series: [{
                    name: 'Contribución',
                    data: graficosData.contribucionesClientes.map(c => c.contribucion)
                }]
            });
        Highcharts.chart('graficoProductividad', {
            chart: { type: 'line' },
            title: { text: 'Productividad de la Solución' },
            xAxis: {
                categories: productividadData.map(t => t.nombre),
                title: { text: 'Tareas' }
            },
            yAxis: {
                title: { text: 'Productividad' }
            },
            series: [{
                name: 'Productividad',
                data: productividadData.map(t => t.productividad)
            }]
        });
        Highcharts.chart('graficoEsfuerzoVsSatisfaccion', {
            chart: { type: 'scatter', zoomType: 'xy' },
            title: { text: 'Esfuerzo vs Satisfacción' },
            xAxis: { title: { text: 'Esfuerzo' } },
            yAxis: { title: { text: 'Satisfacción' } },
            series: [{
                name: 'Tareas',
                data: esfuerzoSatisfaccionData.map(t => [t.productividad, t.productividad * 10]) // Ajustar valores si son distintos
            }]
        });
        } catch (error) {
            console.error("Error al inicializar gráficos:", error);
        }
});
