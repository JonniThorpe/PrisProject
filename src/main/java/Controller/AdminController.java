package Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String mostrarPanelAdmin(HttpSession session, Model model) {
        // Obtener el idUsuario de la sesión
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        String nombreUsuario = (String) session.getAttribute("nombreUsuario");


        if (idUsuario == null) {
            return "redirect:/login";  // Si no hay usuario en sesión, redirige al login
        }

        // Añadir los datos del usuario al modelo para mostrar en la página
        model.addAttribute("idUsuario", idUsuario);
        model.addAttribute("nombreUsuario", nombreUsuario);

        return "admin";  // Retorna la vista admin.html
    }
}
