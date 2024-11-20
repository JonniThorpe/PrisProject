package Controller;

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

        // Verificar si el proyecto pertenece al usuario
        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(idProyecto);
        if (proyectoOpt.isEmpty()) {
            return "redirect:/admin";
        }

        Proyecto proyecto = proyectoOpt.get();

        // Obtener los resultados del cálculo para este proyecto
        List<Object[]> resultados = tareaRepository.obtenerTareasConValoracionPonderada(idProyecto, "Client");

        List<ResultadoTareaDTO> resultadoDTOs = new ArrayList<>();
        for (Object[] resultado : resultados) {
            Long idTarea = ((Number) resultado[0]).longValue();
            String nombreTarea = (String) resultado[1];
            Integer esfuerzo = ((Number) resultado[2]).intValue();
            Double valoracionPonderada = resultado[3] != null ? ((Number) resultado[3]).doubleValue() : 0.0;
            resultadoDTOs.add(new ResultadoTareaDTO(idTarea, nombreTarea, esfuerzo, valoracionPonderada));
        }

        // Ordenar por valoración ponderada descendente
        resultadoDTOs.sort(Comparator.comparing(ResultadoTareaDTO::getValoracionPonderada).reversed());

        model.addAttribute("proyecto", proyecto);
        model.addAttribute("resultados", resultadoDTOs);

        return "projectResults";
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
}
