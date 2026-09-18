package py.edu.uc.lp3.minecraft;

public class Cerdo extends Animal {

	private static final double CANTIDAD_CURACION_ALIMENTO = 2;

	private final boolean montable;

	public Cerdo(Vector3D posicion) {
		super(TipoEntidad.CERDO, "Cerdo", 10, posicion, false);
		this.montable = true;
	}

	public boolean esMontable() {
		return montable;
	}

	@Override
	public String emitirSonido() {
		return "Oinc oinc";
	}

	@Override
	public boolean alimentar(Item alimento) {
		if (alimento == null) {
			throw new IllegalArgumentException("El alimento no puede ser nulo");
		}
		curar(CANTIDAD_CURACION_ALIMENTO);
		return true;
	}
}