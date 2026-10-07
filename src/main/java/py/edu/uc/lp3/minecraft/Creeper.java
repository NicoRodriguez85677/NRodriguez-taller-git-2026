package py.edu.uc.lp3.minecraft;

public class Creeper extends MobHostil {

	private static final double TIEMPO_DETONACION_INICIAL = 3.0;
	private static final double DANO_EXPLOSION = 17;

	private double tiempoDetonacion;
	private boolean detonando;
	private boolean explotado;

	public Creeper(Vector3D posicion) {
		this(posicion, TIEMPO_DETONACION_INICIAL);
	}

	public Creeper(Vector3D posicion, double tiempoDetonacion) {
		super(TipoEntidad.CREEPER, "Creeper", 20, DANO_EXPLOSION, 4, posicion);

		if (tiempoDetonacion <= 0) {
			throw new IllegalArgumentException("El tiempo de detonación debe ser mayor que cero");
		}

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
}