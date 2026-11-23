
package py.edu.uc.lp3.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import py.edu.uc.lp3.domain.Jugador;

class JugadorServiceTest {

    private final JugadorService service = new JugadorService();

    @Test
    void creaJugadorValido() {
        Jugador jugador = service.crearJugador("Steve", 1, 64, 2);

        assertEquals("Steve", jugador.getNombreJugador());
        assertEquals(1.0, jugador.getPosicion().getX());
    }

    @Test
    void rechazaNombreVacio() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> service.crearJugador(" ", 0, 64, 0));

        assertEquals("El nombre no puede estar vacio",
                error.getMessage());
    }

    @Test
    void rechazaCoordenadasInvalidas() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.crearJugador(
                        "Steve", Double.NaN, 64, 0));
    }
}
