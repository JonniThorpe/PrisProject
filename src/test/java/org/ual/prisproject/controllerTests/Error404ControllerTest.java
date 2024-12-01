package Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Error404ControllerTest {

    @InjectMocks
    private Error404Controller error404Controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba de Caja Negra: Verificar que retorna la vista correcta
    @Test
    void testMostrarPaginaDestino() {
        String view = error404Controller.mostrarPaginaDestino();
        assertEquals("error", view); // Verifica que la vista devuelta sea "error"
    }
}
