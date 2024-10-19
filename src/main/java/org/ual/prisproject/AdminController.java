package org.ual.prisproject;

import entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import repositorios.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/admin")
    public String mostrarPanelAdmin(HttpSession session, Model model) {
        // Obtener el idUsuario de la sesión
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        if (idUsuario == null) {
            return "redirect:/login";  // Si no hay usuario en sesión, redirige al login
        }

        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Añadir el usuario al modelo para mostrar en la página
        model.addAttribute("usuario", usuario);
        return "admin";  // Redirigir a la vista del panel de administración
    }
}
