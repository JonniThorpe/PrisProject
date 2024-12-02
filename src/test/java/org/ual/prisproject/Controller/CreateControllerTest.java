package Controller;

import entidades.Proyecto;
import entidades.Usuario;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import repository.ProyectoRepository;
import repository.UsuarioRepository;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateControllerTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private CreateController createController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba de Caja Negra: Acceder a la página de creación
    @Test
    void testMostrarPaginaDestino() {
        String view = createController.mostrarPaginaDestino();
        assertEquals("create", view); // Verifica que se retorne la vista "create"
    }

    // Prueba de Caja Blanca: Usuario no autenticado (sin sesión)
    @Test
    void testCrearProyecto_SinSesion() {
        when(session.getAttribute("idUsuario")).thenReturn(null);

        String view = createController.crearProyecto("Proyecto A", 10, session, model);

        assertEquals("redirect:/login", view); // Debe redirigir al login
        verify(model).addAttribute("error", "Usuario no autenticado. Inicie sesión.");
    }

    // Prueba de Caja Negra: Usuario no encontrado en la base de datos
    @Test
    void testCrearProyecto_UsuarioNoEncontrado() {
        when(session.getAttribute("idUsuario")).thenReturn(1);
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        String view = createController.crearProyecto("Proyecto A", 10, session, model);

        assertEquals("create", view); // Debe volver a la página de creación
        verify(model).addAttribute("error", "Usuario no válido.");
    }

    // Prueba de Caja Blanca: Proyecto creado con éxito
    @Test
    void testCrearProyecto_Exito() {
        when(session.getAttribute("idUsuario")).thenReturn(1);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Usuario Test");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        String view = createController.crearProyecto("Proyecto A", 10, session, model);

        assertEquals("redirect:/admin", view); // Debe redirigir al panel de administración

        verify(proyectoRepository).save(argThat(proyecto ->
                proyecto.getNombre().equals("Proyecto A") &&
                        proyecto.getPesoMaximoTareas().equals(10) &&
                        proyecto.getUsuarioIdusuario().equals(usuario) &&
                        proyecto.getFechaCreacion() != null
        ));
    }
}
