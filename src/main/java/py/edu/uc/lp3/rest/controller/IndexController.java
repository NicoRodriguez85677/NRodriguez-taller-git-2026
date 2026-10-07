package py.edu.uc.lp3.minecraft.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.minecraft.Jugador;
import py.edu.uc.lp3.minecraft.TipoEntidad;
import py.edu.uc.lp3.minecraft.Vector3D;

@RestController
public class IndexController {

	@GetMapping("/")
	public EstadoServicio index() {
		return new EstadoServicio(
				"ACTIVO",
				"El servicio de Minecraft esta activo",
				"Nicolas Rodriguez",
				"Minecraft");
	}

	@GetMapping("/api/minecraft/jugador")
	public DetalleJugador crearJugador(
			@RequestParam String nombre,
			@RequestParam double x,
			@RequestParam double y,
			@RequestParam double z) {
		Jugador jugador = new Jugador(nombre, new Vector3D(x, y, z));
		return new DetalleJugador(
				jugador.getNombre(),
				jugador.getTipo(),
				jugador.getPosicion(),
				jugador.getSalud(),
				jugador.getNombreJugador(),
				jugador.getNivelExperiencia(),
				jugador.esModoCreativo());
	}

	public record EstadoServicio(String estado, String mensaje, String autor, String dominio) {
	}

	public record DetalleJugador(
			String nombre,
			TipoEntidad tipo,
			Vector3D posicion,
			double salud,
			String nombreJugador,
			int nivelExperiencia,
			boolean modoCreativo) {
	}
}