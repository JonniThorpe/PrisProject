package Controller;

import entidades.Proyecto;
import entidades.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import repository.ProyectoRepository;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private ProyectoRepository proyectoRepository;

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
        usuario.setId((int) idUsuario.longValue());

        // Consultar los proyectos para el usuario
        List<Proyecto> proyectos = proyectoRepository.findByUsuarioIdusuario(usuario);

        // Añadir los proyectos y datos del usuario al modelo para mostrar en la página
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "admin";  // Retorna la vista admin.html
    }
}
