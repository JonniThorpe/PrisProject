package Controller;

import entidades.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private ProyectoHasUsuarioRepository proyectoHasUsuarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioValoraTareaRepository usuarioValoraTareaRepository;

    @Mock
    private TareaRepository tareaRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Pruebas para mostrarPanelClient
    @Test
    void testMostrarPanelClient_SinSesion() {
        when(session.getAttribute("idUsuario")).thenReturn(null);

        String view = clientController.mostrarPanelClient(session, model);

        assertEquals("redirect:/login", view);
    }

    @Test
    void testMostrarPanelClient_ConSesion() {
        Integer idUsuario = 1;
        when(session.getAttribute("idUsuario")).thenReturn(idUsuario);
        when(session.getAttribute("nombreUsuario")).thenReturn("Cliente");

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        List<ProyectoHasUsuario> proyectosDeUsuario = new ArrayList<>();
        ProyectoHasUsuario proyectoHasUsuario = new ProyectoHasUsuario();
        proyectosDeUsuario.add(proyectoHasUsuario);

        when(proyectoHasUsuarioRepository.findByUsuarioIdusuario(any(Usuario.class))).thenReturn(proyectosDeUsuario);

        List<Tarea> tareas = new ArrayList<>();
        Tarea tarea = new Tarea();
        tarea.setId(1);
        tareas.add(tarea);

        when(tareaRepository.findAll()).thenReturn(tareas);

        String view = clientController.mostrarPanelClient(session, model);

        assertEquals("client", view);
        verify(model).addAttribute("proyectosDeUsuario", proyectosDeUsuario);
        verify(model).addAttribute("tareas", tareas);
        verify(model).addAttribute("nombreUsuario", "Cliente");
    }

    // Pruebas para addRating
    @Test
    void testAddRating_SinSesion() {
        when(session.getAttribute("idUsuario")).thenReturn(null);

        String view = clientController.addRating(1, 5, session, model);

        assertEquals("redirect:/login", view);
    }

    @Test
    void testAddRating_UsuarioNoEncontrado() {
        Integer idUsuario = 1;
        when(session.getAttribute("idUsuario")).thenReturn(idUsuario);
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        String view = clientController.addRating(1, 5, session, model);

        assertEquals("client", view);
        verify(model).addAttribute("mensaje", "Usuario no encontrado.");
    }

    @Test
    void testAddRating_TareaNoEncontrada() {
        Integer idUsuario = 1;
        when(session.getAttribute("idUsuario")).thenReturn(idUsuario);

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(tareaRepository.findById(1)).thenReturn(Optional.empty());

        String view = clientController.addRating(1, 5, session, model);

        assertEquals("client", view);
        verify(model).addAttribute("mensaje", "Tarea no encontrada.");
    }

    @Test
    void testAddRating_ValoracionNueva() {
        Integer idUsuario = 1;
        Integer tareaId = 1;
        Integer valoracion = 5;

        when(session.getAttribute("idUsuario")).thenReturn(idUsuario);

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        Tarea tarea = new Tarea();
        tarea.setId(1);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(tareaRepository.findById(tareaId)).thenReturn(Optional.of(tarea));
        when(usuarioValoraTareaRepository.findById(any(UsuarioValoraTareaId.class))).thenReturn(Optional.empty());

        String view = clientController.addRating(tareaId, valoracion, session, model);

        assertEquals("redirect:/client", view);

        verify(usuarioValoraTareaRepository).save(argThat(valoracionTarea ->
                valoracionTarea.getId().getTareaIdtarea().equals(tareaId) && // Usa getTareaIdtarea
                        valoracionTarea.getId().getUsuarioIdusuario().equals(idUsuario) && // Usa getUsuarioIdusuario
                        valoracionTarea.getValoracion().equals(valoracion) &&
                        valoracionTarea.getValorada() == 1
        ));
        verify(model).addAttribute("mensaje", "Valoración añadida con éxito.");
    }


    @Test
    void testAddRating_ActualizarValoracion() {
        Integer idUsuario = 1;
        Integer tareaId = 1;
        Integer valoracion = 4;

        when(session.getAttribute("idUsuario")).thenReturn(idUsuario);

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        Tarea tarea = new Tarea();
        tarea.setId(1);

        UsuarioValoraTareaId valoracionId = new UsuarioValoraTareaId(idUsuario, tareaId);
        UsuarioValoraTarea valoracionTarea = new UsuarioValoraTarea();
        valoracionTarea.setId(valoracionId);
        valoracionTarea.setValoracion(3); // Valoración existente

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(tareaRepository.findById(tareaId)).thenReturn(Optional.of(tarea));
        when(usuarioValoraTareaRepository.findById(valoracionId)).thenReturn(Optional.of(valoracionTarea));

        String view = clientController.addRating(tareaId, valoracion, session, model);

        assertEquals("redirect:/client", view);
        verify(usuarioValoraTareaRepository).save(argThat(actualizado ->
                actualizado.getValoracion().equals(valoracion) &&
                        actualizado.getValorada() == 1
        ));
        verify(model).addAttribute("mensaje", "Valoración añadida con éxito.");
    }
}
