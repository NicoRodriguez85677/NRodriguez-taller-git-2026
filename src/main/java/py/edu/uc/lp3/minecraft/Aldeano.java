package py.edu.uc.lp3.minecraft;

import java.util.Objects;

public class Aldeano extends Residente {

	private String profesion;
	private int nivelComercio;

	public Aldeano(Vector3D posicion) {
		super(TipoEntidad.ALDEANO, "Aldeano", 20, posicion, true);
		this.profesion = "Campesino";
		this.nivelComercio = 1;
	}

	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		if (profesion == null || profesion.isBlank()) {
			throw new IllegalArgumentException("La profesión no puede ser nula ni vacía");
		}
		this.profesion = profesion;
	}

	public int getNivelComercio() {
		return nivelComercio;
	}

	public boolean comerciar(Jugador jugador, Item item) {
		Objects.requireNonNull(jugador, "El jugador no puede ser nulo");
		Objects.requireNonNull(item, "El ítem no puede ser nulo");
		if (!puedeComerciar()) {
			return false;
		}
		if (jugador.recoger(item)) {
			nivelComercio++;
			return true;
		}
		return false;
	}

	@Override
	public void interactuar(Jugador jugador) {
		Objects.requireNonNull(jugador, "El jugador no puede ser nulo");
		if (puedeComerciar()) {
			nivelComercio++;
		}
	}
}