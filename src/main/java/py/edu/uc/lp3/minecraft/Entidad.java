package py.edu.uc.lp3.minecraft;

import java.util.Objects;
import java.util.UUID;

public abstract class Entidad {

	private final UUID id;
	private final TipoEntidad tipo;
	private final String nombre;
	private double salud;
	private Vector3D posicion;

	protected Entidad(TipoEntidad tipo, String nombre, double salud, Vector3D posicion) {
		this.id = UUID.randomUUID();
		this.tipo = Objects.requireNonNull(tipo, "El tipo de entidad no puede ser nulo");
		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El nombre de la entidad no puede ser nulo ni vacío");
		}
		if (salud < 0) {
			throw new IllegalArgumentException("La salud no puede ser negativa");
		}
		this.nombre = nombre;
		this.salud = salud;
		this.posicion = Objects.requireNonNull(posicion, "La posición no puede ser nula");
	}

	public UUID getId() {
		return id;
	}

	public TipoEntidad getTipo() {
		return tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public double getSalud() {
		return salud;
	}

	public Vector3D getPosicion() {
		return posicion;
	}

	public void recibirDano(double cantidad) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad de daño debe ser mayor que cero");
		}
		if (estaViva()) {
			salud = Math.max(0, salud - cantidad);
		}
	}

	public void curar(double cantidad) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad de curación debe ser mayor que cero");
		}
		if (estaViva()) {
			salud += cantidad;
		}
	}

	public boolean estaViva() {
		return salud > 0;
	}

	public void moverse(Vector3D destino) {
		this.posicion = Objects.requireNonNull(destino, "El destino no puede ser nulo");
	}

	public abstract void tick();

	protected void cambiarSalud(double nuevaSalud) {
		if (nuevaSalud < 0) {
			throw new IllegalArgumentException("La salud no puede ser negativa");
		}
		this.salud = nuevaSalud;
	}
}