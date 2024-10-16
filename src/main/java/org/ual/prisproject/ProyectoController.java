package org.ual.prisproject;

import entidades.Proyecto;
import entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.ual.prisproject.repository.ProyectoRepository;
import org.ual.prisproject.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class ProyectoController {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/create")
    public String mostrarFormulario() {
        return "create";  // Muestra el formulario en create.html
    }

    @PostMapping("/create")
    public String crearProyecto(@RequestParam String nombre,
                                @RequestParam Double pesoMaximoTareas,
                                @RequestParam Integer usuarioId,
                                Model model) {
        // Buscar el usuario por ID
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);

        if (usuario.isPresent()) {
            // Crear un nuevo objeto Proyecto
            Proyecto proyecto = new Proyecto();
            proyecto.setNombre(nombre);
            proyecto.setFechaCreacion(LocalDate.now());  // Fecha actual
            proyecto.setPesoMaximoTareas(pesoMaximoTareas);

            // Asignar el usuario al proyecto
            Set<Usuario> usuarios = new HashSet<>();
            usuarios.add(usuario.get());
            proyecto.setUsuarios(usuarios);

            // Guardar el proyecto en la base de datos
            proyectoRepository.save(proyecto);

            // Redirigir a una página de confirmación o la página principal
            return "redirect:/";
        } else {
            // Si el usuario no existe, mostrar un error
            model.addAttribute("error", "Usuario no encontrado");
            return "create";
        }
    }
}
