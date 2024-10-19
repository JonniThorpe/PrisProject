package Controller;

import dao.ProyectoRepository;
import entidades.Proyecto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateController {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @GetMapping("/create")
    public String mostrarPaginaDestino() {
        String idUsuario ;
        Proyecto proyectosUsuario =  proyectoRepository.findById(1).get();

        return "create";  // Thymeleaf buscará automáticamente en /templates/create.html
    }
}
