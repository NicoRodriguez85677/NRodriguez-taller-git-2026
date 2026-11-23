
package py.edu.uc.lp3.domain;

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
        if (objetivo != null) {
            objetivo.recibirDano(fuerzaAtaque);
        }
    }
	@Override
	public String tipoAtaque() {
    	return "Ataque cuerpo a cuerpo";
}
}
