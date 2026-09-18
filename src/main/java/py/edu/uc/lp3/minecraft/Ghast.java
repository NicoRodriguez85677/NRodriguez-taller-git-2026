package py.edu.uc.lp3.minecraft;

public class Ghast extends MobHostil {

	private static final int TICKS_ENFRIAMIENTO_ATAQUE = 3;

	private int ticksEnfriamientoAtaque;

	public Ghast(Vector3D posicion) {
		super(TipoEntidad.GHAST, "Ghast", 10, 6, 100, posicion);
		this.ticksEnfriamientoAtaque = 0;
	}

	public int getTicksEnfriamientoAtaque() {
		return ticksEnfriamientoAtaque;
	}

	public boolean puedeAtacar() {
		return estaViva() && ticksEnfriamientoAtaque == 0;
	}

	@Override
	public void atacar(Entidad objetivo) {
		if (!estaViva()) {
			throw new IllegalStateException("El Ghast no puede atacar porque no esta vivo");
		}
		if (ticksEnfriamientoAtaque > 0) {
			throw new IllegalStateException("El ataque del Ghast aun esta en enfriamiento");
		}
		super.atacar(objetivo);
		ticksEnfriamientoAtaque = TICKS_ENFRIAMIENTO_ATAQUE;
	}

	@Override
	public void tick() {
		if (ticksEnfriamientoAtaque > 0) {
			ticksEnfriamientoAtaque--;
		}
		super.tick();
	}
}
