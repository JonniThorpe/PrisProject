package Controller;

import entidades.Proyecto;
import entidades.ProyectoHasUsuario;
import entidades.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import repository.ProyectoHasUsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientController {

    @Autowired
    private ProyectoHasUsuarioRepository proyectoHasUsuarioRepository;

    @GetMapping("/client")
    public String mostrarPanelClient(HttpSession session, Model model) {
        // Obtener el rol del usuario desde la sesión
        String rolUsuario = (String) session.getAttribute("rol");

        // Si el rol no es 'Client', redirigir a una página de error o prohibir acceso
        if (rolUsuario == null) {
            return "redirect:/login"; // O redirige a una página de error o login
        } else if (rolUsuario.equals("Admin")) {
            return "redirect:/admin"; // Redirige a la vista de admin

        }

        // Obtener el idUsuario y nombreUsuario de la sesión
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        String nombreUsuario = (String) session.getAttribute("nombreUsuario");

        if (idUsuario == null) {
            return "redirect:/login";  // Si no hay usuario en sesión, redirige al login
        }

        // Crear un objeto Usuario con el idUsuario
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        // Consultar los proyectos asociados al usuario a través de la tabla intermedia
        List<ProyectoHasUsuario> proyectosDeUsuario = proyectoHasUsuarioRepository.findByUsuarioIdusuario(usuario);

        // Añadir los proyectos y datos del usuario al modelo para mostrar en la página
        model.addAttribute("proyectosDeUsuario", proyectosDeUsuario);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "client";  // Retorna la vista client.html
    }

}
