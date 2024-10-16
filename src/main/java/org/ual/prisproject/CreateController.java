package org.ual.prisproject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateController {

    @GetMapping("/create.html   ")
    public String mostrarPaginaDestino() {
        return "create";  // Thymeleaf buscará automáticamente en /templates/create.html
    }
}
