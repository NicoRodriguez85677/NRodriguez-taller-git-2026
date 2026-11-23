
package py.edu.uc.lp3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class ExplosivoTest {

    @Test
    void ambasClasesImplementanExplosivo() {
        Explosivo creeper = new Creeper(new Vector3D(0, 64, 0));
        Explosivo tnt = new TNT();

        List<Explosivo> explosivos = List.of(creeper, tnt);

        assertThat(explosivos).hasSize(2);

        for (Explosivo explosivo : explosivos) {
            assertThat(explosivo).isInstanceOf(Explosivo.class);
        }
    }

    @Test
    void ambasClasesExplotanMedianteLaInterfaz() {
        Creeper creeper = new Creeper(new Vector3D(0, 64, 0));
        TNT tnt = new TNT();

        creeper.comenzarDetonacion();
        tnt.encender();

        List<Explosivo> explosivos = List.of(creeper, tnt);

        for (Explosivo explosivo : explosivos) {
            assertThat(explosivo.explotar()).isTrue();
            assertThat(explosivo.explotar()).isFalse();
        }

        assertThat(creeper.yaExploto()).isTrue();
        assertThat(tnt.yaExploto()).isTrue();
    }

    @Test
    void tntNoExplotaSinEncenderse() {
        Explosivo tnt = new TNT();

        assertThat(tnt.puedeExplotar()).isFalse();
        assertThat(tnt.explotar()).isFalse();
    }

    @Test
    void tntPuedeExplotarDespuesDeEncenderse() {
        TNT tnt = new TNT();

        tnt.encender();

        Explosivo explosivo = tnt;

        assertThat(explosivo.puedeExplotar()).isTrue();
        assertThat(explosivo.explotar()).isTrue();
        assertThat(explosivo.puedeExplotar()).isFalse();
    }

@Test
void creeperExplotaDespuesDeTresTicks() {
    Creeper creeper = new Creeper(new Vector3D(0, 64, 0));

    assertThat(creeper.puedeExplotar()).isTrue();

    creeper.comenzarDetonacion();

    creeper.tick();
    creeper.tick();

    assertThat(creeper.yaExploto()).isFalse();

    creeper.tick();

    assertThat(creeper.yaExploto()).isTrue();
    assertThat(creeper.estaViva()).isFalse();

    // No puede explotar dos veces.
    assertThat(creeper.explotar()).isFalse();
}

}
