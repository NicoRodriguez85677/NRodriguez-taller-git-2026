package py.edu.uc.lp3.minecraft;

public class Zombie extends MobHostil {

	private final double velocidad;
	private boolean quemandose;

	public Zombie(Vector3D posicion) {
		super(TipoEntidad.ZOMBIE, "Zombie", 20, 3, 16, posicion);
		this.velocidad = 1.0;
		this.quemandose = false;
	}

	public double getVelocidad() {
		return velocidad;
	}

	public boolean estaQuemandose() {
		return quemandose;
	}

	public void exponerAlSol(boolean expuesto) {
		this.quemandose = expuesto;
	}

	@Override
	public String emitirSonido() {
		if (estaQuemandose()) {
			return "¡Agh! Me estoy quemando";
		}
		return "Grrr...";
	}

	@Override
	public void tick() {
		if (quemandose && estaViva()) {
			recibirDano(1);
		}
		super.tick();
	}
}