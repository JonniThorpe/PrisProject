package Controller;

import entidades.Usuario;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba de Caja Negra: Acceder a la página de login
    @Test
    void testLogin() {
        String view = loginController.login();
        assertEquals("login", view); // Verifica que se retorna la vista "login"
    }

    // Prueba de Caja Blanca: Credenciales válidas, rol Admin
    @Test
    void testProcesarLogin_AdminExitoso() {
        String user = "admin";
        String password = "password";

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre(user);
        usuario.setContraseña(password);
        usuario.setRol("Admin");

        when(usuarioRepository.findByNombre(user)).thenReturn(usuario);

        String view = loginController.procesarLogin(user, password, session, model);

        assertEquals("redirect:/admin", view); // Debe redirigir al panel de administración
        verify(session).setAttribute("idUsuario", 1);
        verify(session).setAttribute("nombreUsuario", "admin");
        verify(session).setAttribute("rol", "Admin");
    }

    // Prueba de Caja Blanca: Credenciales válidas, rol Client
    @Test
    void testProcesarLogin_ClientExitoso() {
        String user = "client";
        String password = "password";

        Usuario usuario = new Usuario();
        usuario.setId(2);
        usuario.setNombre(user);
        usuario.setContraseña(password);
        usuario.setRol("Client");

        when(usuarioRepository.findByNombre(user)).thenReturn(usuario);

        String view = loginController.procesarLogin(user, password, session, model);

        assertEquals("redirect:/client", view); // Debe redirigir al panel de cliente
        verify(session).setAttribute("idUsuario", 2);
        verify(session).setAttribute("nombreUsuario", "client");
        verify(session).setAttribute("rol", "Client");
    }

    // Prueba de Caja Negra: Rol no autorizado
    @Test
    void testProcesarLogin_RolNoAutorizado() {
        String user = "other";
        String password = "password";

        Usuario usuario = new Usuario();
        usuario.setId(3);
        usuario.setNombre(user);
        usuario.setContraseña(password);
        usuario.setRol("Other");

        when(usuarioRepository.findByNombre(user)).thenReturn(usuario);

        String view = loginController.procesarLogin(user, password, session, model);

        assertEquals("login", view); // Debe permanecer en la página de login
        verify(model).addAttribute("error", "Rol no autorizado");
    }

    // Prueba de Caja Blanca: Credenciales incorrectas
    @Test
    void testProcesarLogin_CredencialesIncorrectas() {
        String user = "invalid";
        String password = "wrongPassword";

        when(usuarioRepository.findByNombre(user)).thenReturn(null); // Usuario no existe

        String view = loginController.procesarLogin(user, password, session, model);

        assertEquals("login", view); // Debe permanecer en la página de login
        verify(model).addAttribute("error", "Usuario o contraseña incorrectos");
    }

    // Prueba de Caja Negra: Cerrar sesión
    @Test
    void testLogout() {
        String view = loginController.logout(session);

        assertEquals("redirect:/login", view); // Debe redirigir al login
        verify(session).invalidate(); // Verifica que la sesión se invalida
    }
}
