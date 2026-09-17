package py.edu.uc.lp3.minecraft;

public abstract class Animal extends Entidad {

	private static final int EDAD_ADULTA = 10;

	private int edad;
	private final boolean domesticable;

	protected Animal(TipoEntidad tipo, String nombre, double salud, Vector3D posicion, boolean domesticable) {
		super(tipo, nombre, salud, posicion);
		this.edad = 0;
		this.domesticable = domesticable;
	}

	public int getEdad() {
		return edad;
	}

	public boolean esAdulto() {
		return edad >= EDAD_ADULTA;
	}

	public boolean esDomesticable() {
		return domesticable;
	}

	public abstract boolean alimentar(Item alimento);

	@Override
	public void tick() {
		if (edad < EDAD_ADULTA) {
			edad++;
		}
	}
}