package Controller;

import entidades.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import repository.*;

import java.util.List;

@Controller
public class ClientController {

    @Autowired
    private ProyectoHasUsuarioRepository proyectoHasUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioValoraTareaRepository usuarioValoraTareaRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @GetMapping("/client")
    public String mostrarPanelClient(HttpSession session, Model model) {
        // Obtener el idUsuario y nombreUsuario de la sesión
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            return "redirect:/login";  // Si no hay usuario en sesión, redirige al login
        }

        // Crear un objeto Usuario con el idUsuario
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        // Consultar los proyectos asociados al usuario
        List<ProyectoHasUsuario> proyectosDeUsuario = proyectoHasUsuarioRepository.findByUsuarioIdusuario(usuario);

        // Cargar las tareas asociadas a cada proyecto
        List<Tarea> tareas = tareaRepository.findAll(); // Aquí puedes filtrar las tareas si lo necesitas

        // Añadir los proyectos, tareas y datos del usuario al modelo
        model.addAttribute("proyectosDeUsuario", proyectosDeUsuario);
        model.addAttribute("tareas", tareas);
        model.addAttribute("nombreUsuario", (String) session.getAttribute("nombreUsuario"));

        return "client";
    }
    
    @PostMapping("/client/addRating")
    public String addRating(@RequestParam("tareaId") Integer tareaId,
                            @RequestParam("valoracion") Integer valoracion,
                            HttpSession session, Model model) {

        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            model.addAttribute("mensaje", "Usuario no encontrado.");
            return "client";
        }

        Tarea tarea = tareaRepository.findById(tareaId).orElse(null);
        if (tarea == null) {
            model.addAttribute("mensaje", "Tarea no encontrada.");
            return "client";
        }

        UsuarioValoraTareaId valoracionId = new UsuarioValoraTareaId(idUsuario, tareaId);
        UsuarioValoraTarea valoracionTarea = usuarioValoraTareaRepository.findById(valoracionId).orElse(null);

        if (valoracionTarea == null) {
            valoracionTarea = new UsuarioValoraTarea();
            valoracionTarea.setId(valoracionId);
            valoracionTarea.setUsuarioIdusuario(usuario);
            valoracionTarea.setTareaIdtarea(tarea);
        }

        valoracionTarea.setValoracion(valoracion);
        valoracionTarea.setValorada((byte) 1);

        usuarioValoraTareaRepository.save(valoracionTarea);

        model.addAttribute("mensaje", "Valoración añadida con éxito.");

        return "redirect:/client";
    }
}
