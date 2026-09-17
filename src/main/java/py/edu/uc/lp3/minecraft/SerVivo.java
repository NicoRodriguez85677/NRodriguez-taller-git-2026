package py.edu.uc.lp3.minecraft;

public abstract class SerVivo extends Entidad {

	private final double saludMaxima;
	private boolean envenenado;
	private double regeneracion;

	protected SerVivo(TipoEntidad tipo, String nombre, double salud, double saludMaxima, Vector3D posicion) {
		super(tipo, nombre, salud, posicion);
		if (saludMaxima <= 0) {
			throw new IllegalArgumentException("La salud máxima debe ser mayor que cero");
		}
		if (salud > saludMaxima) {
			throw new IllegalArgumentException("La salud no puede superar la salud máxima");
		}
		this.saludMaxima = saludMaxima;
		this.envenenado = false;
		this.regeneracion = 0;
	}

	public double getSaludMaxima() {
		return saludMaxima;
	}

	public boolean estaEnvenenado() {
		return envenenado;
	}

	public void envenenar() {
		this.envenenado = true;
	}

	public void curarEnvenenamiento() {
		this.envenenado = false;
	}

	public void aplicarRegeneracion(double cantidadPorTick) {
		if (cantidadPorTick < 0) {
			throw new IllegalArgumentException("La regeneración no puede ser negativa");
		}
		this.regeneracion = cantidadPorTick;
	}

	@Override
	public void curar(double cantidad) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad de curación debe ser mayor que cero");
		}
		cambiarSalud(Math.min(getSalud() + cantidad, saludMaxima));
	}

	@Override
	public void tick() {
		if (envenenado && estaViva()) {
			recibirDano(1);
		}
		if (estaViva() && regeneracion > 0) {
			curar(regeneracion);
		}
	}
}