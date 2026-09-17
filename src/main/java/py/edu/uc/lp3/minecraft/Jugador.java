package py.edu.uc.lp3.minecraft;

public class Jugador extends SerVivo {

	private static final double SALUD_MAXIMA_JUGADOR = 20;
	private static final int CAPACIDAD_INVENTARIO = 36;

	private final String nombreJugador;
	private final Inventario inventario;
	private int nivelExperiencia;
	private boolean modoCreativo;

	public Jugador(String nombreJugador, Vector3D posicion) {
		super(TipoEntidad.JUGADOR, nombreJugador, SALUD_MAXIMA_JUGADOR, SALUD_MAXIMA_JUGADOR, posicion);
		if (nombreJugador == null || nombreJugador.isBlank()) {
			throw new IllegalArgumentException("El nombre de jugador no puede ser nulo ni vacío");
		}
		this.nombreJugador = nombreJugador;
		this.inventario = new Inventario(CAPACIDAD_INVENTARIO);
		this.nivelExperiencia = 0;
		this.modoCreativo = false;
	}

	public String getNombreJugador() {
		return nombreJugador;
	}

	public int getNivelExperiencia() {
		return nivelExperiencia;
	}

	public boolean esModoCreativo() {
		return modoCreativo;
	}

	public void setModoCreativo(boolean modoCreativo) {
		this.modoCreativo = modoCreativo;
	}

	public void aumentarExperiencia(int cantidad) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La experiencia otorgada debe ser mayor que cero");
		}
		nivelExperiencia += cantidad;
	}

	public void atacar(Entidad objetivo) {
		if (objetivo == null) {
			throw new IllegalArgumentException("El objetivo no puede ser nulo");
		}
		objetivo.recibirDano(1);
	}

	public boolean recoger(Item item) {
		return inventario.agregar(item);
	}

	public void colocarBloque(Bloque bloque) {
		if (bloque == null) {
			throw new IllegalArgumentException("El bloque no puede ser nulo");
		}
	}

	public Inventario abrirInventario() {
		return inventario;
	}
}