package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import entidades.*;
import dto.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private TareaRepository tareaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProyectoHasUsuarioRepository proyectoHasUsuarioRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMostrarPanelAdmin_SinSesion() {
        when(session.getAttribute("rol")).thenReturn(null);

        String view = adminController.mostrarPanelAdmin(session, model);

        assertEquals("redirect:/login", view);
    }

    @Test
    void testMostrarPanelAdmin_RolCliente() {
        when(session.getAttribute("rol")).thenReturn("Client");

        String view = adminController.mostrarPanelAdmin(session, model);

        assertEquals("redirect:/client", view);
    }

    @Test
    void testMostrarPanelAdmin_SinIdUsuario() {
        when(session.getAttribute("rol")).thenReturn("Admin");
        when(session.getAttribute("idUsuario")).thenReturn(null);

        String view = adminController.mostrarPanelAdmin(session, model);

        assertEquals("redirect:/login", view);
    }

    @Test
    void testMostrarPanelAdmin_CargaCorrecta() {
        when(session.getAttribute("rol")).thenReturn("Admin");
        when(session.getAttribute("idUsuario")).thenReturn(1);
        when(session.getAttribute("nombreUsuario")).thenReturn("AdminUser");

        Usuario usuario = new Usuario();
        usuario.setId(1);

        Proyecto proyecto = new Proyecto();
        proyecto.setId(1);
        proyecto.setNombre("Proyecto A");
        proyecto.setUsuarioIdusuario(usuario);

        Tarea tarea = new Tarea();
        tarea.setId(1);
        tarea.setNombre("Tarea 1");
        tarea.setProyectoIdproyecto(proyecto);

        when(proyectoRepository.findByUsuarioIdusuario(any(Usuario.class))).thenReturn(List.of(proyecto));
        when(tareaRepository.findAllByProyectoUsuarioId(1)).thenReturn(List.of(tarea));
        when(usuarioRepository.findByRol("Client")).thenReturn(Collections.emptyList());
        when(proyectoHasUsuarioRepository.findByProyectoIdproyecto(any())).thenReturn(Collections.emptyList());

        String view = adminController.mostrarPanelAdmin(session, model);

        assertEquals("admin", view);
        verify(model).addAttribute("proyectos", List.of(proyecto));
        verify(model).addAttribute("nombreUsuario", "AdminUser");
        verify(model).addAttribute("clientes", Collections.emptyList());
        verify(model).addAttribute("valoraciones", new ArrayList<>());
        verify(model).addAttribute("tareas", List.of(tarea));
    }

    @Test
    void testMostrarResultadoProyecto_SinSesion() throws JsonProcessingException {
        when(session.getAttribute("rol")).thenReturn(null);

        String view = adminController.mostrarResultadoProyecto(1L, session, model);

        assertEquals("redirect:/login", view);
    }

    @Test
    void testMostrarResultadoProyecto_ProyectoNoEncontrado() throws JsonProcessingException {
        when(session.getAttribute("rol")).thenReturn("Admin");
        when(session.getAttribute("idUsuario")).thenReturn(1);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());

        String view = adminController.mostrarResultadoProyecto(1L, session, model);

        assertEquals("redirect:/admin", view);
    }

    @Test
    void testMostrarResultadoProyecto_ProyectoEncontrado() throws JsonProcessingException {
        when(session.getAttribute("rol")).thenReturn("Admin");
        when(session.getAttribute("idUsuario")).thenReturn(1);

        Proyecto proyecto = new Proyecto();
        proyecto.setId(1);
        proyecto.setPesoMaximoTareas(10);

        List<Object[]> resultados = new ArrayList<>();
        resultados.add(new Object[]{1L, "Tarea 1", 5, 4.0});

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(tareaRepository.obtenerTareasConValoracionPonderada(1L, "Client")).thenReturn(resultados);

        String view = adminController.mostrarResultadoProyecto(1L, session, model);

        assertEquals("projectResults", view);
        verify(model).addAttribute("proyecto", proyecto);
        verify(model).addAttribute(eq("tareasDentroDelLimite"), anyList());
        verify(model).addAttribute(eq("tareasExcedidas"), anyList());
        verify(model).addAttribute("esfuerzoMaximo", 10.0);
    }

    @Test
    void testUpdateBudget_Exito() {
        when(session.getAttribute("idUsuario")).thenReturn(1);

        Proyecto proyecto = new Proyecto();
        proyecto.setId(1);
        proyecto.setUsuarioIdusuario(new Usuario());
        proyecto.getUsuarioIdusuario().setId(1);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        String view = adminController.updateBudget(1, 100, session, model);

        assertEquals("redirect:/admin", view);
        verify(proyectoRepository).save(any(Proyecto.class));
    }

    @Test
    void testUpdateBudget_NoPermiso() {
        when(session.getAttribute("idUsuario")).thenReturn(2);

        Proyecto proyecto = new Proyecto();
        proyecto.setId(1);
        proyecto.setUsuarioIdusuario(new Usuario());
        proyecto.getUsuarioIdusuario().setId(1);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        String view = adminController.updateBudget(1, 100, session, model);

        assertEquals("redirect:/admin", view);
        verify(proyectoRepository, never()).save(any());
    }

    @Test
    void testAddTask_Exito() {
        when(session.getAttribute("idUsuario")).thenReturn(1);

        Proyecto proyecto = new Proyecto();
        proyecto.setId(1);
        proyecto.setUsuarioIdusuario(new Usuario());
        proyecto.getUsuarioIdusuario().setId(1);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        String view = adminController.addTask("Nueva Tarea", 5, 1, session, model);

        assertEquals("redirect:/admin", view);
        verify(tareaRepository).save(any(Tarea.class));
    }

    @Test
    void testAddTask_NoPermiso() {
        when(session.getAttribute("idUsuario")).thenReturn(2);

        Proyecto proyecto = new Proyecto();
        proyecto.setId(1);
        proyecto.setUsuarioIdusuario(new Usuario());
        proyecto.getUsuarioIdusuario().setId(1);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        String view = adminController.addTask("Nueva Tarea", 5, 1, session, model);

        assertEquals("redirect:/admin", view);
        verify(tareaRepository, never()).save(any());
    }
    @Test
    void testCambiarTarea_FaltanParametros() {
        String view = adminController.cambiarTarea(null, null, null, model);

        assertEquals("projectResults", view);
        verify(model).addAttribute("error", "Faltan parámetros requeridos.");
    }

    @Test
    void testCambiarTarea_ProyectoNoEncontrado() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());

        String view = adminController.cambiarTarea(2L, 3L, 1L, model);

        assertEquals("projectResults", view);
        verify(model).addAttribute("error", "El proyecto especificado no existe.");
    }

    @Test
    void testCambiarTarea_TareasNoEncontradas() {
        Proyecto proyecto = new Proyecto();
        proyecto.setId(1);
        proyecto.setPesoMaximoTareas(10);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(tareaRepository.obtenerTareasConValoracionPonderada(1L, "Client")).thenReturn(new ArrayList<>());

        String view = adminController.cambiarTarea(2L, 3L, 1L, model);

        assertEquals("projectResults", view);
        verify(model).addAttribute("error", "No se encontraron las tareas seleccionadas.");
    }

    @Test
    void testCambiarTarea_EsfuerzoExcedido() {
        Proyecto proyecto = new Proyecto();
        proyecto.setId(1);
        proyecto.setPesoMaximoTareas(10);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        List<Object[]> resultados = new ArrayList<>();
        resultados.add(new Object[]{1L, "Tarea 1", 6, 4.0}); // Esfuerzo 6
        resultados.add(new Object[]{2L, "Tarea 2", 5, 3.0}); // Esfuerzo 5
        when(tareaRepository.obtenerTareasConValoracionPonderada(1L, "Client")).thenReturn(resultados);

        String view = adminController.cambiarTarea(2L, 1L, 1L, model);

        assertEquals("projectResults", view);
        verify(model).addAttribute("error", "No se puede mover la tarea seleccionada porque excede el esfuerzo máximo permitido.");
        verify(model, never()).addAttribute(eq("proyecto"), any());
        verify(model, never()).addAttribute(eq("tareasDentroDelLimite"), any());
        verify(model, never()).addAttribute(eq("tareasExcedidas"), any());
        verify(model, never()).addAttribute(eq("esfuerzoMaximo"), any());
    }

    @Test
    void testCambiarTarea_CambioExitoso() {
        Proyecto proyecto = new Proyecto();
        proyecto.setId(1);
        proyecto.setPesoMaximoTareas(10);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        List<Object[]> resultados = new ArrayList<>();
        resultados.add(new Object[]{1L, "Tarea 1", 4, 4.0});
        resultados.add(new Object[]{2L, "Tarea 2", 5, 3.0});
        when(tareaRepository.obtenerTareasConValoracionPonderada(1L, "Client")).thenReturn(resultados);

        String view = adminController.cambiarTarea(2L, 1L, 1L, model);

        assertEquals("projectResults", view);
        verify(model).addAttribute("proyecto", proyecto);
        verify(model).addAttribute(eq("tareasDentroDelLimite"), anyList());
        verify(model).addAttribute(eq("tareasExcedidas"), anyList());
        verify(model).addAttribute("esfuerzoMaximo", 10.0);
    }
    @Test
    void testAddNewClient_SinSesion() {
        when(session.getAttribute("idUsuario")).thenReturn(null);

        String view = adminController.addNewClient("Cliente1", "password123", session, model);

        assertEquals("redirect:/login", view);
    }

    @Test
    void testAddNewClient_Exito() {
        when(session.getAttribute("idUsuario")).thenReturn(1);
        when(session.getAttribute("nombreUsuario")).thenReturn("AdminUser");

        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1);

        List<Proyecto> proyectosMock = new ArrayList<>();
        Proyecto proyecto1 = new Proyecto();
        proyecto1.setId(1);
        proyecto1.setNombre("Proyecto A");
        proyectosMock.add(proyecto1);

        when(proyectoRepository.findByUsuarioIdusuario(any(Usuario.class))).thenReturn(proyectosMock);

        String view = adminController.addNewClient("Cliente1", "password123", session, model);

        assertEquals("redirect:/admin", view);

        // Verificar que el cliente fue creado y guardado
        verify(usuarioRepository).save(argThat(cliente ->
                cliente.getNombre().equals("Cliente1") &&
                        cliente.getContraseña().equals("password123") &&
                        cliente.getRol().equals("Client")
        ));

        // Verificar que se añadieron los atributos al modelo
        verify(model).addAttribute("mensaje", "Cliente añadido con éxito.");
        verify(model).addAttribute("proyectos", proyectosMock);
        verify(model).addAttribute("nombreUsuario", "AdminUser");
    }

    @Test
    void testAssignClientToProject_SinSesion() {
        when(session.getAttribute("idUsuario")).thenReturn(null);

        String view = adminController.assignClientToProject(1, List.of(1, 2), 10, session, model);

        assertEquals("redirect:/login", view);
    }

    @Test
    void testAssignClientToProject_ParametrosInvalidos() {
        when(session.getAttribute("idUsuario")).thenReturn(1);

        String view = adminController.assignClientToProject(null, null, null, session, model);

        assertEquals("admin", view);
        verify(model).addAttribute("error", "No se ha seleccionado un proyecto, clientes o el peso no es válido.");
    }

    @Test
    void testAssignClientToProject_ProyectoNoEncontrado() {
        when(session.getAttribute("idUsuario")).thenReturn(1);
        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());

        String view = adminController.assignClientToProject(1, List.of(1, 2), 10, session, model);

        assertEquals("redirect:/admin", view);
        verify(model).addAttribute("mensaje", "Proyecto no encontrado.");
    }

    @Test
    void testAssignClientToProject_Exito() {
        when(session.getAttribute("idUsuario")).thenReturn(1);

        Proyecto proyecto = new Proyecto();
        proyecto.setId(1);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        Usuario usuario1 = new Usuario();
        usuario1.setId(1);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario2));

        String view = adminController.assignClientToProject(1, List.of(1, 2), 10, session, model);

        assertEquals("redirect:/admin", view);

        // Verificar que las relaciones fueron guardadas
        verify(proyectoHasUsuarioRepository, times(2)).save(argThat(relacion ->
                relacion.getProyectoIdproyecto().equals(proyecto) &&
                        (relacion.getUsuarioIdusuario().equals(usuario1) || relacion.getUsuarioIdusuario().equals(usuario2)) &&
                        relacion.getPesoCliente().equals(10)
        ));

        // Verificar que se añadió el mensaje de éxito
        verify(model).addAttribute("mensaje", "Clientes asignados al proyecto con éxito con el peso especificado.");
    }

}
