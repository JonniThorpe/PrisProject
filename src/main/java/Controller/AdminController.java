package Controller;

import entidades.Proyecto;
import entidades.Tarea;
import entidades.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import repository.ProyectoRepository;
import repository.TareaRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @GetMapping("/admin")
    public String mostrarPanelAdmin(HttpSession session, Model model) {
        // Obtener el idUsuario y nombreUsuario de la sesión
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        if (idUsuario == null) {
            return "redirect:/login";  // Si no hay usuario en sesión, redirige al login
        }

        // Crear un objeto Usuario con el idUsuario
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        // Consultar los proyectos para el usuario
        List<Proyecto> proyectos = proyectoRepository.findByUsuarioIdusuario(usuario);

        // Añadir los proyectos y datos del usuario al modelo para mostrar en la página
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "admin";  // Retorna la vista admin.html
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

    // Método para añadir una nueva tarea a un proyecto existente
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
                model.addAttribute("mensaje", "Tarea añadida con éxito.");
            } else {
                model.addAttribute("mensaje", "Error: No tienes permiso para añadir tareas a este proyecto.");
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
}
