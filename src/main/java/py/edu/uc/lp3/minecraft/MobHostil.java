package py.edu.uc.lp3.minecraft;

import java.util.List;
import java.util.Objects;

public abstract class MobHostil extends SerVivo {

	private final double fuerzaAtaque;
	private final double alcanceVision;

	protected MobHostil(TipoEntidad tipo, String nombre, double salud, double fuerzaAtaque, double alcanceVision,
			Vector3D posicion) {
		super(tipo, nombre, salud, salud, posicion);
		if (fuerzaAtaque < 0) {
			throw new IllegalArgumentException("La fuerza de ataque no puede ser negativa");
		}
		if (alcanceVision <= 0) {
			throw new IllegalArgumentException("El alcance de visión debe ser mayor que cero");
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
		Objects.requireNonNull(entidades, "La lista de entidades no puede ser nula");
		Entidad masCercana = null;
		double menorDistancia = Double.MAX_VALUE;
		for (Entidad entidad : entidades) {
			if (entidad == this || !entidad.estaViva()) {
				continue;
			}
			double distancia = getPosicion().distanciaA(entidad.getPosicion());
			if (distancia <= alcanceVision && distancia < menorDistancia) {
				menorDistancia = distancia;
				masCercana = entidad;
			}
		}
		return masCercana;
	}

	public void atacar(Entidad objetivo) {
		Objects.requireNonNull(objetivo, "El objetivo no puede ser nulo");
		objetivo.recibirDano(fuerzaAtaque);
	}
}