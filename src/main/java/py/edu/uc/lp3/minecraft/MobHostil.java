
package py.edu.uc.lp3.minecraft;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.DiscriminatorColumn;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "clase_mob")
public abstract class MobHostil extends SerVivo {

    private double fuerzaAtaque;
    private double alcanceVision;

    // Constructor requerido por JPA
    protected MobHostil() {
        super();
    }

    protected MobHostil(TipoEntidad tipo, String nombre,
                        double salud, double fuerzaAtaque,
                        double alcanceVision, Vector3D posicion) {
        super(tipo, nombre, salud, salud, posicion);

        if (!Double.isFinite(fuerzaAtaque) || fuerzaAtaque < 0) {
            throw new IllegalArgumentException(
                    "La fuerza de ataque debe ser finita y no negativa");
        }

        if (!Double.isFinite(alcanceVision) || alcanceVision <= 0) {
            throw new IllegalArgumentException(
                    "El alcance de vision debe ser positivo y finito");
        }

        this.fuerzaAtaque = fuerzaAtaque;
        this.alcanceVision = alcanceVision;
    }

    public double getFuerzaAtaque() {
        return fuerzaAtaque;
    }

    public double getAlcanceVision() {
        return alcanceVision;
    }

    public Entidad buscarObjetivo(List<Entidad> entidades) {
        Objects.requireNonNull(entidades,
                "La lista de entidades no puede ser nula");

        Entidad masCercana = null;
        double menorDistancia = Double.MAX_VALUE;

        for (Entidad entidad : entidades) {
            if (entidad == this || !entidad.estaViva()) {
                continue;
            }

            double distancia = getPosicion()
                    .distanciaA(entidad.getPosicion());

            if (distancia <= alcanceVision &&
                    distancia < menorDistancia) {
                menorDistancia = distancia;
                masCercana = entidad;
            }
        }

        return masCercana;
    }

    public void atacar(Entidad objetivo) {
        Objects.requireNonNull(objetivo,
                "El objetivo no puede ser nulo");
        objetivo.recibirDano(fuerzaAtaque);
    }
}
