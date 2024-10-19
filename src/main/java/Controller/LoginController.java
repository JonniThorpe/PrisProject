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
        return "login";  // Muestra la página de login
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String user, @RequestParam String password, HttpSession session, Model model) {
        logger.info("Procesando login para el usuario: " + user);
        Usuario usuario = usuarioRepository.findByNombre(user);

        if (usuario != null && usuario.getContraseña().equals(password)) {
            session.setAttribute("idUsuario", usuario.getId());
            session.setAttribute("nombreUsuario", usuario.getNombre());
            logger.info("Login exitoso para el usuario: " + user);
            return "redirect:/admin";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            logger.error("Error de login para el usuario: " + user);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        logger.info("Cerrando sesión");
        session.invalidate();
        return "redirect:/login";
    }
}
