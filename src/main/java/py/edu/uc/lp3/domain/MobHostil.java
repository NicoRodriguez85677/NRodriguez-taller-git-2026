
package py.edu.uc.lp3.domain;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.DiscriminatorColumn;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "clase_mob")
public abstract class MobHostil extends SerVivo implements Atacante {

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
    public void atacar(Entidad objetivo) {
        Objects.requireNonNull(objetivo,
                "El objetivo no puede ser nulo");
        objetivo.recibirDano(fuerzaAtaque);
    }
	@Override
	public String tipoAtaque() {
    	return "Ataque cuerpo a cuerpo";
}
}
