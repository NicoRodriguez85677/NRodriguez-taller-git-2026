package py.edu.uc.lp3.minecraft;

public abstract class Residente extends Entidad {

	private final boolean puedeComerciar;

	protected Residente(TipoEntidad tipo, String nombre, double salud, Vector3D posicion, boolean puedeComerciar) {
		super(tipo, nombre, salud, posicion);
		this.puedeComerciar = puedeComerciar;
	}

	public boolean puedeComerciar() {
		return puedeComerciar;
	}

	public abstract void interactuar(Jugador jugador);

	@Override
	public void tick() {
	}
}