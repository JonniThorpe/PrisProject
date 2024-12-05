package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ResultadoTareaDTO;
import dto.ValoracionDTO;
import entidades.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import repository.ProyectoHasUsuarioRepository;
import repository.ProyectoRepository;
import repository.TareaRepository;
import repository.DependenciaRepository;

import repository.UsuarioRepository;

import java.util.*;

@Controller
public class AdminController {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProyectoHasUsuarioRepository proyectoHasUsuarioRepository;

    @Autowired
    private DependenciaRepository dependenciaRepository;

    @GetMapping("/admin")
    public String mostrarPanelAdmin(HttpSession session, Model model) {
        String rolUsuario = (String) session.getAttribute("rol");

        if (rolUsuario == null) {
            return "redirect:/login";
        } else if (rolUsuario.equals("Client")) {
            return "redirect:/client";
        }

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        if (idUsuario == null) {
            return "redirect:/login";
        }

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        // Obtener proyectos, tareas y clientes
        List<Proyecto> proyectos = proyectoRepository.findByUsuarioIdusuario(usuario);
        List<Tarea> todasLasTareas = tareaRepository.findAllByProyectoUsuarioId(idUsuario);
        List<Usuario> clientes = usuarioRepository.findByRol("Client");

        // Procesar valoraciones de clientes
        List<ValoracionDTO> valoraciones = new ArrayList<>();
        for (Proyecto proyecto : proyectos) {
            List<ProyectoHasUsuario> clientesProyecto = proyectoHasUsuarioRepository.findByProyectoIdproyecto(proyecto);

            for (ProyectoHasUsuario phu : clientesProyecto) {
                Usuario cliente = phu.getUsuarioIdusuario();

                for (Tarea tarea : todasLasTareas) {
                    if (tarea.getProyectoIdproyecto().getId().equals(proyecto.getId())) {
                        Optional<UsuarioValoraTarea> valoracionOpt = tarea.getUsuarioValoraTareas()
                                .stream()
                                .filter(v -> v.getUsuarioIdusuario().getId().equals(cliente.getId()))
                                .findFirst();

                        String valoracion = valoracionOpt.map(v -> String.valueOf(v.getValoracion())).orElse("No valorada");
                        valoraciones.add(new ValoracionDTO(proyecto.getNombre(), cliente.getNombre(), tarea.getNombre(), valoracion, proyecto.getId()));
                    }
                }
            }
        }


        // Agregar atributos al modelo
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("nombreUsuario", nombreUsuario);
        model.addAttribute("clientes", clientes);
        model.addAttribute("valoraciones", valoraciones);
        model.addAttribute("tareas", todasLasTareas);

        return "admin";
    }

    @GetMapping("/admin/result")
    public String mostrarResultadoProyecto(@RequestParam("idProyecto") Long idProyecto, HttpSession session, Model model) {
        String rolUsuario = (String) session.getAttribute("rol");

        if (rolUsuario == null || session.getAttribute("idUsuario") == null) {
            return "redirect:/login";
        }

        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(idProyecto);
        if (proyectoOpt.isEmpty()) {
            return "redirect:/admin";
        }

        Proyecto proyecto = proyectoOpt.get();
        double esfuerzoMaximo = proyecto.getPesoMaximoTareas();

        // Obtener tareas
        List<ResultadoTareaDTO> tareasDentroDelLimite = (List<ResultadoTareaDTO>) session.getAttribute("tareasDentroDelLimite");
        List<ResultadoTareaDTO> tareasExcedidas = (List<ResultadoTareaDTO>) session.getAttribute("tareasExcedidas");

        if (tareasDentroDelLimite == null || tareasExcedidas == null) {
            List<Object[]> resultados = tareaRepository.obtenerTareasConValoracionPonderada(idProyecto, "Client");
            tareasDentroDelLimite = new ArrayList<>();
            tareasExcedidas = new ArrayList<>();

            List<ResultadoTareaDTO> todasLasTareas = new ArrayList<>();
            for (Object[] resultado : resultados) {
                Long idTarea = ((Number) resultado[0]).longValue();
                String nombreTarea = (String) resultado[1];
                Integer esfuerzo = ((Number) resultado[2]).intValue();
                Double valoracionPonderada = resultado[3] != null ? ((Number) resultado[3]).doubleValue() : 0.0;

                todasLasTareas.add(new ResultadoTareaDTO(idTarea, nombreTarea, esfuerzo, valoracionPonderada));
            }

            todasLasTareas.sort(Comparator.comparing(ResultadoTareaDTO::getValoracionPonderada).reversed());

            double esfuerzoAcumulado = 0;
            for (ResultadoTareaDTO tarea : todasLasTareas) {
                if (esfuerzoAcumulado + tarea.getEsfuerzo() <= esfuerzoMaximo) {
                    esfuerzoAcumulado += tarea.getEsfuerzo();
                    tareasDentroDelLimite.add(tarea);
                } else {
                    tareasExcedidas.add(tarea);
                }
            }

            session.setAttribute("tareasDentroDelLimite", tareasDentroDelLimite);
            session.setAttribute("tareasExcedidas", tareasExcedidas);
        }

        List<Map<String, Object>> contribuciones = calcularContribuciones(proyecto, tareasDentroDelLimite);

        // Grafo de dependencias
        List<Map<String, Object>> grafoDependencias = new ArrayList<>();
        List<Tarea> tareas = tareaRepository.findByProyectoIdproyecto(proyecto);

        for (Tarea tarea : tareas) {
            List<Dependencia> dependencias = dependenciaRepository.findByTarea(tarea);
            for (Dependencia dependencia : dependencias) {
                Map<String, Object> link = new HashMap<>();
                link.put("from", tarea.getId());
                link.put("fromName", tarea.getNombre());
                link.put("to", dependencia.getIdTareaDependencia().getId());
                link.put("toName", dependencia.getIdTareaDependencia().getNombre());
                grafoDependencias.add(link);
            }
        }

        model.addAttribute("tareasJson", convertirAJson(tareasDentroDelLimite));
        model.addAttribute("grafoDependencias", convertirAJson(grafoDependencias));
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("tareasDentroDelLimite", tareasDentroDelLimite);
        model.addAttribute("tareasExcedidas", tareasExcedidas);
        model.addAttribute("esfuerzoMaximo", esfuerzoMaximo);
        model.addAttribute("contribuciones", contribuciones);

        return "projectResults";
    }

    @PostMapping("/admin/expulsarTarea")
    public String expulsarTarea(
            @RequestParam("idTarea") Long idTarea,
            @RequestParam("idProyecto") Long idProyecto,
            HttpSession session,
            Model model) throws JsonProcessingException {

        System.out.println("Iniciando expulsión de tarea con ID: " + idTarea + " para el proyecto ID: " + idProyecto);

        List<ResultadoTareaDTO> tareasDentroDelLimite = (List<ResultadoTareaDTO>) session.getAttribute("tareasDentroDelLimite");
        List<ResultadoTareaDTO> tareasExcedidas = (List<ResultadoTareaDTO>) session.getAttribute("tareasExcedidas");

        if (tareasDentroDelLimite == null || tareasExcedidas == null) {
            System.out.println("Error: Listas no inicializadas en sesión.");
            return mostrarResultadoProyecto(idProyecto, session, model);
        }

        System.out.println("Tareas dentro del límite antes de expulsar: " + tareasDentroDelLimite);
        System.out.println("Tareas excedidas antes de expulsar: " + tareasExcedidas);

        ResultadoTareaDTO tareaAExpulsar = tareasDentroDelLimite.stream()
                .filter(t -> t.getIdTarea().equals(idTarea))
                .findFirst()
                .orElse(null);

        if (tareaAExpulsar != null) {
            tareasDentroDelLimite.remove(tareaAExpulsar);
            tareasExcedidas.add(tareaAExpulsar);
            System.out.println("Tarea expulsada: " + tareaAExpulsar);
        } else {
            System.out.println("Error: Tarea no encontrada en la lista de tareas dentro del límite.");
        }

        System.out.println("Tareas dentro del límite después de expulsar: " + tareasDentroDelLimite);
        System.out.println("Tareas excedidas después de expulsar: " + tareasExcedidas);

        session.setAttribute("tareasDentroDelLimite", tareasDentroDelLimite);
        session.setAttribute("tareasExcedidas", tareasExcedidas);

        return mostrarResultadoProyecto(idProyecto, session, model);
    }

    @PostMapping("/admin/forzarEntrada")
    public String forzarEntrada(
            @RequestParam("idTarea") Long idTarea,
            @RequestParam("idProyecto") Long idProyecto,
            HttpSession session,
            Model model) throws JsonProcessingException {

        System.out.println("Iniciando forzar entrada de tarea con ID: " + idTarea + " para el proyecto ID: " + idProyecto);

        List<ResultadoTareaDTO> tareasDentroDelLimite = (List<ResultadoTareaDTO>) session.getAttribute("tareasDentroDelLimite");
        List<ResultadoTareaDTO> tareasExcedidas = (List<ResultadoTareaDTO>) session.getAttribute("tareasExcedidas");

        if (tareasDentroDelLimite == null || tareasExcedidas == null) {
            System.out.println("Error: Listas no inicializadas en sesión.");
            return mostrarResultadoProyecto(idProyecto, session, model);
        }

        System.out.println("Tareas dentro del límite antes de forzar: " + tareasDentroDelLimite);
        System.out.println("Tareas excedidas antes de forzar: " + tareasExcedidas);

        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(idProyecto);
        if (proyectoOpt.isEmpty()) {
            System.out.println("Error: Proyecto no encontrado.");
            return mostrarResultadoProyecto(idProyecto, session, model);
        }

        Proyecto proyecto = proyectoOpt.get();
        double esfuerzoMaximo = proyecto.getPesoMaximoTareas();

        double esfuerzoAcumulado = tareasDentroDelLimite.stream()
                .mapToDouble(ResultadoTareaDTO::getEsfuerzo)
                .sum();

        ResultadoTareaDTO tareaAForzar = tareasExcedidas.stream()
                .filter(t -> t.getIdTarea().equals(idTarea))
                .findFirst()
                .orElse(null);

        if (tareaAForzar != null) {
            System.out.println("Esfuerzo actual acumulado: " + esfuerzoAcumulado);
            System.out.println("Esfuerzo de la tarea a forzar: " + tareaAForzar.getEsfuerzo());

            if (esfuerzoAcumulado + tareaAForzar.getEsfuerzo() <= esfuerzoMaximo) {
                tareasExcedidas.remove(tareaAForzar);
                tareasDentroDelLimite.add(tareaAForzar);
                System.out.println("Tarea forzada a entrar: " + tareaAForzar);
            } else {
                System.out.println("Error: No se puede forzar la entrada. Esfuerzo máximo excedido.");
                model.addAttribute("error", "No se puede forzar la entrada de la tarea porque excede el esfuerzo máximo permitido.");
            }
        } else {
            System.out.println("Error: Tarea no encontrada en la lista de tareas excedidas.");
        }

        System.out.println("Tareas dentro del límite después de forzar: " + tareasDentroDelLimite);
        System.out.println("Tareas excedidas después de forzar: " + tareasExcedidas);

        session.setAttribute("tareasDentroDelLimite", tareasDentroDelLimite);
        session.setAttribute("tareasExcedidas", tareasExcedidas);

        return mostrarResultadoProyecto(idProyecto, session, model);
    }


    @PostMapping("/admin/resetCambios")
    public String resetCambios(@RequestParam("idProyecto") Long idProyecto, HttpSession session) {
        // Eliminar las listas modificadas de la sesión
        session.removeAttribute("tareasDentroDelLimite");
        session.removeAttribute("tareasExcedidas");

        // Redirigir a `mostrarResultadoProyecto` para recalcular las listas desde cero
        return "redirect:/admin/result?idProyecto=" + idProyecto;
    }


    @PostMapping("/admin/updateBudget")
    public String updateBudget(@RequestParam("idProyecto") Integer idProyecto,
                               @RequestParam("pesoMaximoTareas") Integer pesoMaximoTareas,
                               HttpSession session, Model model) {
        // Obtener el idUsuario de la sesión
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            return "redirect:/login";  // Si no hay usuario en sesión, redirige al login
        }

        // Buscar el proyecto en la base de datos
        Optional<Proyecto> proyectoOptional = proyectoRepository.findById(Long.valueOf(idProyecto));

        if (proyectoOptional.isPresent()) {
            Proyecto proyecto = proyectoOptional.get();

            // Asegurarse de que el proyecto pertenece al usuario actual
            if (proyecto.getUsuarioIdusuario().getId().equals(idUsuario)) {
                // Actualizar el peso máximo de tareas (presupuesto)
                proyecto.setPesoMaximoTareas(pesoMaximoTareas);

                // Guardar el proyecto actualizado
                proyectoRepository.save(proyecto);

                // Añadir mensaje de éxito al modelo
                model.addAttribute("mensaje", "Presupuesto actualizado con éxito.");
            } else {
                model.addAttribute("mensaje", "Error: No tienes permiso para modificar este proyecto.");
            }
        } else {
            model.addAttribute("mensaje", "Error: Proyecto no encontrado.");
        }

        // Consultar los proyectos para el usuario para actualizar la vista
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        List<Proyecto> proyectos = proyectoRepository.findByUsuarioIdusuario(usuario);
        model.addAttribute("proyectos", proyectos);

        return "redirect:/admin";  // Retorna la vista admin.html actualizada
    }

    @PostMapping("/admin/addTask")
    public String addTask(@RequestParam("nombre") String nombre,
                          @RequestParam("esfuerzo") Integer esfuerzo,
                          @RequestParam("proyectoIdproyecto") Integer proyectoIdproyecto,
                          HttpSession session, Model model) {

        // Obtener el idUsuario de la sesión
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            return "redirect:/login";  // Si no hay usuario en sesión, redirige al login
        }

        // Buscar el proyecto al que se añadirá la tarea
        Optional<Proyecto> proyectoOptional = proyectoRepository.findById(Long.valueOf(proyectoIdproyecto));

        if (proyectoOptional.isPresent()) {
            Proyecto proyecto = proyectoOptional.get();

            // Asegurarse de que el proyecto pertenece al usuario actual
            if (proyecto.getUsuarioIdusuario().getId().equals(idUsuario)) {
                // Crear la nueva tarea
                Tarea nuevaTarea = new Tarea();
                nuevaTarea.setNombre(nombre);
                nuevaTarea.setEsfuerzo(esfuerzo);
                nuevaTarea.setProyectoIdproyecto(proyecto);

                // Guardar la tarea en la base de datos
                tareaRepository.save(nuevaTarea);

                // Añadir mensaje de éxito al modelo
                session.setAttribute("mensaje", "Tarea añadida con éxito.");
            } else {
                session.setAttribute("mensaje", "Error: No tienes permiso para añadir tareas a este proyecto.");
            }
        } else {
            session.setAttribute("mensaje", "Error: Proyecto no encontrado.");
        }

        // Redirigir a la vista principal del administrador
        return "redirect:/admin";
    }

    @PostMapping("/admin/newClient")
    public String addNewClient(@RequestParam("nombre") String nombre,
                               @RequestParam("contraseña") String contraseña,
                               HttpSession session, Model model) {

        // Comprobar que el usuario administrador esté logueado
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/login";  // Si no hay usuario en sesión, redirige al login
        }

        // Crear un nuevo Usuario con rol Cliente
        Usuario nuevoCliente = new Usuario();
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setContraseña(contraseña);
        nuevoCliente.setRol("Client");  // Establecer el rol como "Cliente"

        // Guardar el cliente en la base de datos
        usuarioRepository.save(nuevoCliente);

        // Añadir mensaje de éxito al modelo
        model.addAttribute("mensaje", "Cliente añadido con éxito.");

        // Recargar la vista con los proyectos del administrador
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        List<Proyecto> proyectos = proyectoRepository.findByUsuarioIdusuario(usuario);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));

        return "redirect:/admin";
    }

    @PostMapping("/admin/assignClientToProject")
    public String assignClientToProject(@RequestParam("projectId") Integer projectId,
                                        @RequestParam("clientIds") List<Integer> clientIds,
                                        @RequestParam("pesoCliente") Integer pesoCliente,
                                        HttpSession session, Model model) {
        // Verifica que el administrador esté logueado
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/login";
        }

        // Validar que projectId, clientIds y pesoCliente no sean nulos
        if (projectId == null || clientIds == null || pesoCliente == null) {
            model.addAttribute("error", "No se ha seleccionado un proyecto, clientes o el peso no es válido.");
            return "admin";
        }

        // Buscar el proyecto por ID
        Optional<Proyecto> proyectoOptional = proyectoRepository.findById(Long.valueOf(projectId));
        if (proyectoOptional.isEmpty()) {
            model.addAttribute("mensaje", "Proyecto no encontrado.");
            return "redirect:/admin";
        }
        Proyecto proyecto = proyectoOptional.get();

        // Asignar cada cliente al proyecto con el peso especificado
        for (Integer clientId : clientIds) {
            Usuario usuario = usuarioRepository.findById(clientId).orElse(null);
            if (usuario != null) {
                ProyectoHasUsuario proyectoHasUsuario = new ProyectoHasUsuario();

                // Configurar la ID compuesta
                ProyectoHasUsuarioId proyectoHasUsuarioId = new ProyectoHasUsuarioId();
                proyectoHasUsuarioId.setProyectoIdproyecto(proyecto.getId());
                proyectoHasUsuarioId.setUsuarioIdusuario(usuario.getId());

                // Asignar las entidades relacionadas y el peso
                proyectoHasUsuario.setId(proyectoHasUsuarioId);
                proyectoHasUsuario.setProyectoIdproyecto(proyecto);
                proyectoHasUsuario.setUsuarioIdusuario(usuario);
                proyectoHasUsuario.setPesoCliente(pesoCliente);

                // Guardar la relación en la base de datos
                proyectoHasUsuarioRepository.save(proyectoHasUsuario);
            }
        }

        model.addAttribute("mensaje", "Clientes asignados al proyecto con éxito con el peso especificado.");
        return "redirect:/admin";
    }

    @GetMapping("/admin/dependencias")
    public String mostrarDependencias(@RequestParam("idTarea") Integer idTarea,
                                      Model model,
                                      HttpSession session) {
        // Validar la sesión del usuario
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/login";
        }

        // Buscar la tarea por ID
        Optional<Tarea> tareaOpt = tareaRepository.findById(idTarea);
        if (tareaOpt.isEmpty()) {
            model.addAttribute("error", "Tarea no encontrada.");
            return "redirect:/admin";
        }
        Tarea tarea = tareaOpt.get();

        // Obtener el proyecto asociado
        Proyecto proyecto = tarea.getProyectoIdproyecto();
        if (proyecto == null) {
            model.addAttribute("error", "Proyecto asociado a la tarea no encontrado.");
            return "redirect:/admin";
        }

        // Guardar idProyecto en el modelo
        model.addAttribute("idProyecto", proyecto.getId().longValue());

        // Obtener todas las dependencias actuales y las tareas disponibles
        List<Dependencia> dependencias = dependenciaRepository.findByTarea(tarea);
        List<Tarea> tareasDisponibles = tareaRepository.findByProyectoIdproyecto(proyecto);

        // Excluir la propia tarea y tareas ya dependientes
        tareasDisponibles.remove(tarea);
        tareasDisponibles.removeAll(dependencias.stream().map(Dependencia::getIdTareaDependencia).toList());

        // Agregar los datos al modelo
        model.addAttribute("tarea", tarea);
        model.addAttribute("dependencias", dependencias);
        model.addAttribute("tareas", tareasDisponibles);

        return "dependencias";
    }


    @PostMapping("/admin/dependencias/agregar")
    public String agregarDependencia(@RequestParam("idTarea") Integer idTarea,
                                     @RequestParam("idDependencia") Integer idDependencia,
                                     Model model, HttpSession session) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/login";
        }

        // Validar que ambas tareas existen
        Optional<Tarea> tareaOpt = tareaRepository.findById(idTarea);
        Optional<Tarea> dependenciaOpt = tareaRepository.findById(idDependencia);

        if (tareaOpt.isEmpty() || dependenciaOpt.isEmpty()) {
            model.addAttribute("error", "Tarea o dependencia no encontrada.");
            return "redirect:/admin";
        }

        Tarea tarea = tareaOpt.get();
        Tarea dependencia = dependenciaOpt.get();

        // Crear y guardar la nueva dependencia
        Dependencia nuevaDependencia = new Dependencia();
        nuevaDependencia.setIdTarea(tarea);
        nuevaDependencia.setIdTareaDependencia(dependencia);

        dependenciaRepository.save(nuevaDependencia);

        return "redirect:/admin/dependencias?idTarea=" + idTarea;
    }

    @PostMapping("/admin/dependencias/eliminar")
    public String eliminarDependencia(@RequestParam("idDependencia") Integer idDependencia,
                                      @RequestParam("idTarea") Integer idTarea) {
        dependenciaRepository.deleteById(idDependencia);
        return "redirect:/admin/dependencias?idTarea=" + idTarea;
    }


    private List<Map<String, Object>> calcularContribuciones(Proyecto proyecto, List<ResultadoTareaDTO> tareasDentroDelLimite) {
        List<Map<String, Object>> contribuciones = new ArrayList<>();

        for (ResultadoTareaDTO tarea : tareasDentroDelLimite) {
            Long idTarea = tarea.getIdTarea();
            List<ProyectoHasUsuario> clientesProyecto = proyectoHasUsuarioRepository.findByProyectoIdproyecto(proyecto);

            for (ProyectoHasUsuario phu : clientesProyecto) {
                Usuario cliente = phu.getUsuarioIdusuario();
                Optional<UsuarioValoraTarea> valoracionOpt = tareaRepository.findValoracionByClienteAndTarea(cliente.getId().longValue(), idTarea);

                if (valoracionOpt.isPresent()) {
                    UsuarioValoraTarea valoracion = valoracionOpt.get();
                    double contribucion = (phu.getPesoCliente() * valoracion.getValoracion()) / tarea.getValoracionPonderada();

                    Map<String, Object> contribucionData = new HashMap<>();
                    contribucionData.put("cliente", cliente.getNombre());
                    contribucionData.put("tarea", tarea.getNombreTarea());
                    contribucionData.put("contribucion", contribucion);

                    contribuciones.add(contribucionData);
                }
            }
        }

        return contribuciones;
    }

    public String convertirAJson(Object objeto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(objeto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}"; // Retorna un JSON vacío en caso de error
        }
    }


}
