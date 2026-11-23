
package py.edu.uc.lp3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.domain.Vector3D;
import py.edu.uc.lp3.domain.Zombie;

class JugadorAtaqueServiceTest {

    private final JugadorService service = new JugadorService();

    @Test
    void jugadorAtacaYZombiePierdeUnPuntoDeSalud() {
        Jugador jugador = new Jugador(
                "Steve", new Vector3D(0, 64, 0));

        Zombie objetivo = new Zombie(
                new Vector3D(1, 64, 0));

        service.atacar(jugador, objetivo);

        assertThat(objetivo.getSalud()).isEqualTo(19);
    }

    @Test
    void rechazaJugadorNulo() {
        Zombie objetivo = new Zombie(
                new Vector3D(1, 64, 0));

        assertThatThrownBy(() -> service.atacar(null, objetivo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El jugador no puede ser nulo");
    }

    @Test
    void rechazaObjetivoNulo() {
        Jugador jugador = new Jugador(
                "Steve", new Vector3D(0, 64, 0));

        assertThatThrownBy(() -> service.atacar(jugador, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El objetivo no puede ser nulo");
    }
}
