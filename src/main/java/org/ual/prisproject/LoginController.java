package org.ual.prisproject;

import entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import repositorios.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String mostrarPaginaLogin() {
        return "login";  // Carga la vista de login
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        // Buscar el usuario por nombre
        Usuario usuario = usuarioRepository.findByNombre(username);

        if (usuario != null && usuario.getPassword().equals(password)) {
            // Login exitoso, guarda el idUsuario en la sesión
            session.setAttribute("idUsuario", usuario.getId());
            session.setAttribute("nombreUsuario", usuario.getNombre());
            return "redirect:/admin";  // Redirige al panel administrador
        } else {
            // Login fallido, muestra un mensaje de error
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalida la sesión
        session.invalidate();
        return "redirect:/login";
    }
}
