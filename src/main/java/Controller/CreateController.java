package Controller;

import repository.ProyectoRepository;
import repository.UsuarioRepository;
import entidades.Proyecto;
import entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Optional;

@Controller
public class CreateController {

    private static final Logger logger = LoggerFactory.getLogger(CreateController.class);

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Muestra la página de creación de proyectos
    @GetMapping("/create")
    public String mostrarPaginaDestino() {
        logger.info("Accediendo a la página de creación de proyecto.");
        return "create";  // Thymeleaf buscará automáticamente en /templates/create.html
    }

    // Procesa el formulario de creación de proyecto
    @PostMapping("/create")
    public String crearProyecto(
            @RequestParam String nombre,
            @RequestParam Integer pesoMaximoTareas,
            HttpSession session, // Obtenemos la sesión HTTP
            Model model) {

        logger.info("Iniciando proceso de creación de proyecto con nombre: {}", nombre);

        // Obtenemos el idUsuario desde la sesión
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        logger.info("ID de usuario obtenido de la sesión: {}", idUsuario);

        if (idUsuario == null) {
            // Si no hay un usuario en sesión, redirige a la página de login
            logger.error("El usuario no está autenticado, redirigiendo al login.");
            model.addAttribute("error", "Usuario no autenticado. Inicie sesión.");
            return "redirect:/login";
        }

        // Verificar si el idUsuario se pasa correctamente
        if (idUsuario != null) {
            logger.info("ID de usuario en sesión: {}", idUsuario);
        } else {
            logger.error("No se encontró ID de usuario en la sesión.");
        }

        // Buscar el usuario en la base de datos
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);

        if (usuarioOptional.isEmpty()) {
            // Si el usuario no existe en la base de datos, mostrar error
            logger.error("Usuario con ID {} no encontrado en la base de datos.", idUsuario);
            model.addAttribute("error", "Usuario no válido.");
            return "create";
        }

        // Creamos el proyecto y lo asociamos al usuario
        Usuario usuario = usuarioOptional.get();
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(nombre);
        proyecto.setPesoMaximoTareas(pesoMaximoTareas);
        proyecto.setFechaCreacion(Instant.now()); // Establecemos la fecha de creación
        proyecto.setUsuarioIdusuario(usuario); // Asociamos el proyecto al usuario

        logger.info("Guardando el proyecto: {} para el usuario: {}", proyecto.getNombre(), usuario.getNombre());

        // Guardamos el proyecto en el repositorio
        proyectoRepository.save(proyecto);

        logger.info("Proyecto creado exitosamente con nombre: {}", proyecto.getNombre());

        // Redirigimos a una página (por ejemplo, el panel de administración o una lista de proyectos)
        return "redirect:/admin";
    }
}
