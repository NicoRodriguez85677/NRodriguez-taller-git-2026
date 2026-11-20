
package py.edu.uc.lp3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.domain.Entidad;
import py.edu.uc.lp3.domain.Vector3D;
import py.edu.uc.lp3.domain.Zombie;

class MobHostilServiceTest {

    private MobHostilService service;
    private Zombie mob;

    @BeforeEach
    void configurar() {
        // La busqueda no utiliza el repositorio.
        service = new MobHostilService(null);
        mob = new Zombie(new Vector3D(0, 0, 0));
    }

    @Test
    void debeSeleccionarElObjetivoMasCercano() {
        Zombie lejano = new Zombie(new Vector3D(10, 0, 0));
        Zombie cercano = new Zombie(new Vector3D(3, 0, 0));

        Entidad resultado = service.buscarObjetivo(
                mob, List.of(lejano, cercano));

        assertThat(resultado).isSameAs(cercano);
    }

    @Test
    void debeIgnorarObjetivosFueraDelAlcance() {
        // El Zombie tiene alcance de vision de 16 bloques.
        Zombie lejano = new Zombie(new Vector3D(20, 0, 0));

        Entidad resultado = service.buscarObjetivo(
                mob, List.of(lejano));

        assertThat(resultado).isNull();
    }

    @Test
    void debeIgnorarObjetivosMuertos() {
        Zombie muerto = new Zombie(new Vector3D(2, 0, 0));
        Zombie vivo = new Zombie(new Vector3D(5, 0, 0));

        muerto.recibirDano(muerto.getSalud());

        Entidad resultado = service.buscarObjetivo(
                mob, List.of(muerto, vivo));

        assertThat(resultado).isSameAs(vivo);
    }

    @Test
    void debeRechazarListaNula() {
        assertThatThrownBy(() ->
                service.buscarObjetivo(mob, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La lista de entidades no puede ser nula");
    }

    @Test
    void debeRechazarMobNulo() {
        Zombie objetivo = new Zombie(new Vector3D(2, 0, 0));

        assertThatThrownBy(() ->
                service.buscarObjetivo(null, List.of(objetivo)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El mob no puede ser nulo");
    }
}
