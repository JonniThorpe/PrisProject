package Controller;

import entidades.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String login() {
        logger.info("Accediendo a la página de login");
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String user, @RequestParam String password, HttpSession session, Model model) {
        logger.info("Procesando login para el usuario: " + user);

        // Buscar usuario en la base de datos
        Usuario usuario = usuarioRepository.findByNombre(user);

        if (usuario != null && usuario.getContraseña().equals(password)) {
            // Guardar los datos del usuario en la sesión
            session.setAttribute("idUsuario", usuario.getId());
            session.setAttribute("nombreUsuario", usuario.getNombre());
            session.setAttribute("rol", usuario.getRol());  // Guardar el rol en la sesión

            logger.info("Login exitoso para el usuario: " + user);

            // Redirigir según el rol del usuario
            if (usuario.getRol().equals("Admin")) {
                return "redirect:/admin";
            } else if (usuario.getRol().equals("Client")) {
                return "redirect:/client";
            } else {
                model.addAttribute("error", "Rol no autorizado");
                return "login";
            }
        } else {
            // Error de login, credenciales incorrectas
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            logger.error("Error de login para el usuario: " + user);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        logger.info("Cerrando sesión");
        session.invalidate();  // Invalidar la sesión y eliminar los datos
        return "redirect:/login";  // Redirigir al login tras cerrar sesión
    }
}
