package py.edu.uc.lp3.minecraft;

public class Enderman extends MobHostil {

	public Enderman(Vector3D posicion) {
		super(TipoEntidad.ENDERMAN, "Enderman", 40, 7, 32, posicion);
	}

	public void teletransportarse(Vector3D destino) {
		moverse(destino);
	}

	public Bloque tomarBloque(Bloque bloque) {
		if (bloque == null) {
			throw new IllegalArgumentException("El bloque no puede ser nulo");
		}
		return bloque;
	}
}