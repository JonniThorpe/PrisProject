package Controller;

import org.springframework.stereotype.Controller;

@Controller("/error")
public class Error404Controller {
    public String mostrarPaginaDestino() {
        return "error";  // Thymeleaf buscará automáticamente en /templates/error.html
    }
}
