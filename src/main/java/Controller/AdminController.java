package Controller;

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

        List<Proyecto> proyectos = proyectoRepository.findByUsuarioIdusuario(usuario);
        List<Tarea> todasLasTareas = tareaRepository.findAllByProyectoUsuarioId(idUsuario);
        List<Usuario> clientes = usuarioRepository.findByRol("Client");

        // Cargar valoraciones de clientes para cada tarea y organizarlas para la vista
        List<ValoracionDTO> valoraciones = new ArrayList<>();

        for (Proyecto proyecto : proyectos) {
            List<ProyectoHasUsuario> clientesProyecto = proyectoHasUsuarioRepository.findByProyectoIdproyecto(proyecto);

            for (ProyectoHasUsuario phu : clientesProyecto) {
                Usuario cliente = phu.getUsuarioIdusuario();

                for (Tarea tarea : todasLasTareas) {
                    if (tarea.getProyectoIdproyecto().getId().equals(proyecto.getId())) {
                        // Buscar si el cliente ha valorado esta tarea
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

        model.addAttribute("proyectos", proyectos);
        model.addAttribute("nombreUsuario", nombreUsuario);
        model.addAttribute("clientes", clientes);
        model.addAttribute("valoraciones", valoraciones);
        model.addAttribute("tareas", todasLasTareas);

        return "admin";
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

        return "admin";  // Retorna la vista admin.html actualizada
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
                                        HttpSession session, Model model) {

        // Verifica que el administrador esté logueado
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/login";
        }

        // Validar que projectId y clientIds no sean nulos
        if (projectId == null || clientIds == null) {
            model.addAttribute("error", "No se ha seleccionado un proyecto o clientes para asignar.");
            return "admin";
        }

        // Buscar el proyecto por ID y asegurarse de que esté completamente cargado
        Optional<Proyecto> proyectoOptional = proyectoRepository.findById(Long.valueOf(projectId));
        if (proyectoOptional.isEmpty()) {
            model.addAttribute("mensaje", "Proyecto no encontrado.");
            return "redirect:/admin";
        }
        Proyecto proyecto = proyectoOptional.get();

        // Asignar cada cliente al proyecto y guardar la relación en la base de datos
        for (Integer clientId : clientIds) {
            Usuario usuario = usuarioRepository.findById(clientId).orElse(null);
            if (usuario != null) {
                ProyectoHasUsuario proyectoHasUsuario = new ProyectoHasUsuario();

                // Inicializar la ID compuesta
                ProyectoHasUsuarioId proyectoHasUsuarioId = new ProyectoHasUsuarioId();
                proyectoHasUsuarioId.setProyectoIdproyecto(proyecto.getId());
                proyectoHasUsuarioId.setUsuarioIdusuario(usuario.getId());

                // Configurar la ID embebida en ProyectoHasUsuario
                proyectoHasUsuario.setId(proyectoHasUsuarioId);

                // Asignar las entidades relacionadas
                proyectoHasUsuario.setProyectoIdproyecto(proyecto);
                proyectoHasUsuario.setUsuarioIdusuario(usuario);

                // Guardar la entidad en la base de datos
                proyectoHasUsuarioRepository.save(proyectoHasUsuario);
            }
        }

        model.addAttribute("mensaje", "Clientes asignados al proyecto con éxito.");
        return "redirect:/admin";  // Redirige al panel del administrador con la lista actualizada
    }
}
