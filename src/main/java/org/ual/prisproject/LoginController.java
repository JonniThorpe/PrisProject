package org.ual.prisproject;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarPaginaDestino() {
        return "login";  // Thymeleaf buscará automáticamente en /templates/login.html
    }
}
