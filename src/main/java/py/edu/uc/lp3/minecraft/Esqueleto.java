package py.edu.uc.lp3.minecraft;

public class Esqueleto extends MobHostil {

	private boolean conArco;

	public Esqueleto(Vector3D posicion) {
		super(TipoEntidad.ESQUELETO, "Esqueleto", 20, 2.5, 24, posicion);
		this.conArco = true;
	}

	public boolean tieneArco() {
		return conArco;
	}

	public boolean dispararFlecha(Entidad objetivo) {
		if (!conArco || objetivo == null || !estaViva()) {
			return false;
		}
		atacar(objetivo);
		return true;
	}
}