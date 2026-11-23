
package py.edu.uc.lp3.domain;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class SerVivoTest {

    @Test
    void regeneracionNoSuperaSaludMaxima() {
        Zombie zombie = new Zombie(new Vector3D(0, 64, 0));

        // El Zombie comienza con 20 de salud.
        zombie.recibirDano(5);
        assertEquals(15.0, zombie.getSalud());

        // Intentamos regenerar 10 puntos.
        zombie.aplicarRegeneracion(10);
        zombie.tick();

        // No debe superar su salud maxima de 20.
        assertEquals(20.0, zombie.getSalud());
    }

@Test
void rechazaRegeneracionInvalida() {
    Zombie zombie = new Zombie(new Vector3D(0, 64, 0));

    assertThrows(
            IllegalArgumentException.class,
            () -> zombie.aplicarRegeneracion(-1));

    assertThrows(
            IllegalArgumentException.class,
            () -> zombie.aplicarRegeneracion(Double.NaN));

    assertThrows(
            IllegalArgumentException.class,
            () -> zombie.aplicarRegeneracion(Double.POSITIVE_INFINITY));
}

}
