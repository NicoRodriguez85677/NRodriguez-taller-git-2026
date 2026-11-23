package py.edu.uc.lp3.domain;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
@Entity
@DiscriminatorValue("CREEPER")
public class Creeper extends MobHostil implements Explosivo{

	private static final double TIEMPO_DETONACION_INICIAL = 3.0;
	private static final double DANO_EXPLOSION = 17;

	private double tiempoDetonacion;
	private boolean detonando;
	private boolean explotado;
	protected Creeper() {
    super();
}
	public Creeper(Vector3D posicion) {
		this(posicion, TIEMPO_DETONACION_INICIAL);
	}

	public Creeper(Vector3D posicion, double tiempoDetonacion) {
		super(TipoEntidad.CREEPER, "Creeper", 20, DANO_EXPLOSION, 4, posicion);

		this.tiempoDetonacion = tiempoDetonacion;
		this.detonando = false;
		this.explotado = false;
	}

	public double getTiempoDetonacion() {
		return tiempoDetonacion;
	}

	public boolean estaDetonando() {
		return detonando;
	}
public boolean yaExploto() {
return explotado;
}

	public void comenzarDetonacion() {
		this.detonando = true;
	}

	public void acercarse(Entidad objetivo) {
		if (objetivo != null) {
			moverse(objetivo.getPosicion());
		}
	}
	public void acercarse(Entidad objetivo, double distanciaMaxima) {
		if (objetivo != null) {
			double distancia = getPosicion().distanciaA(objetivo.getPosicion());

			if (distancia <= distanciaMaxima) {
				moverse(objetivo.getPosicion());
			}
		}
	}

	@Override
	public boolean puedeExplotar() {
    	return estaViva() && !detonando && !explotado;
	}

	@Override
	public boolean explotar() {
		if (!detonando || explotado || !estaViva()) {
			return false;
		}
		explotado = true;
		recibirDano(getSalud());
		return true;
	}

	@Override
	public void tick() {
		if (detonando && !explotado && tiempoDetonacion > 0) {
			tiempoDetonacion -= 1;
			if (tiempoDetonacion <= 0) {
				explotar();
			}
		}
		super.tick();
	}

	@Override
	public String tipoAtaque() {
    			return "Explosion";
}
}