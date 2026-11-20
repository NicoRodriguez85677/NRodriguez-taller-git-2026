package py.edu.uc.lp3.domain;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
@Entity
@DiscriminatorValue("ZOMBIE")
public class Zombie extends MobHostil {

	private double velocidad;
	private boolean quemandose;
	protected Zombie() {
    super();
}

	public Zombie(Vector3D posicion) {
    	this(posicion, 1.0);
	}

	public Zombie(Vector3D posicion, double velocidad) {
    	super(TipoEntidad.ZOMBIE, "Zombie", 20, 3, 16, posicion);

    	if (!Double.isFinite(velocidad) || velocidad <= 0) {
        	throw new IllegalArgumentException(
                	"La velocidad debe ser positiva y finita");
    	}

    	this.velocidad = velocidad;
    	this.quemandose = false;
	}


	public double getVelocidad() {
		return velocidad;
	}

	public boolean estaQuemandose() {
		return quemandose;
	}

	public void exponerAlSol(boolean expuesto) {
		this.quemandose = expuesto;
	}

	@Override
	public String emitirSonido() {
		if (estaQuemandose()) {
			return "¡Agh! Me estoy quemando";
		}
		return "Grrr...";
	}

	@Override
	public void tick() {
		if (quemandose && estaViva()) {
			recibirDano(1);
		}
		super.tick();
	}
	@Override
	public String tipoAtaque() {
    		return "Golpe cuerpo a cuerpo";
		}
}