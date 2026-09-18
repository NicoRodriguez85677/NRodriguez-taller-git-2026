package py.edu.uc.lp3.minecraft.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.minecraft.Cerdo;
import py.edu.uc.lp3.minecraft.Entidad;
import py.edu.uc.lp3.minecraft.TipoEntidad;
import py.edu.uc.lp3.minecraft.Vector3D;
import py.edu.uc.lp3.minecraft.Zombie;

@RestController
@RequestMapping("/api/minecraft/polimorfismo")
public class PolimorfismoController {

	@GetMapping("/emitir-sonido")
	public List<SonidoEntidad> emitirSonido() {
		List<Entidad> entidades = List.of(
				new Zombie(new Vector3D(0, 64, 0)),
				new Cerdo(new Vector3D(10, 64, 0)));

		return entidades.stream()
				.map(entidad -> new SonidoEntidad(
						entidad.getNombre(),
						entidad.getTipo(),
						entidad.emitirSonido()))
				.toList();
	}

	public record SonidoEntidad(String nombre, TipoEntidad tipo, String sonido) {
	}
}