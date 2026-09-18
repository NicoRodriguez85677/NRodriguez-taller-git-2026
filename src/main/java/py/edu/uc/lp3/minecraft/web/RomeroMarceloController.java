package py.edu.uc.lp3.minecraft.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.minecraft.Entidad;
import py.edu.uc.lp3.minecraft.Ghast;
import py.edu.uc.lp3.minecraft.MobHostil;
import py.edu.uc.lp3.minecraft.TipoEntidad;
import py.edu.uc.lp3.minecraft.Vector3D;
import py.edu.uc.lp3.minecraft.Zombie;

@RestController
@RequestMapping("/api/minecraft/ghast")
public class RomeroMarceloController {

	@GetMapping("/simular-ataque")
	public ResultadoAtaque simularAtaque(
			@RequestParam double x,
			@RequestParam double y,
			@RequestParam double z) {
		Ghast ghast = new Ghast(new Vector3D(x, y, z));
		MobHostil atacante = ghast;
		Entidad objetivo = new Zombie(new Vector3D(x + 1, y, z));

		atacante.atacar(objetivo);

		return new ResultadoAtaque(
				atacante.getNombre(),
				atacante.getTipo(),
				atacante.getPosicion(),
				objetivo.getNombre(),
				objetivo.getSalud(),
				ghast.getTicksEnfriamientoAtaque());
	}

	public record ResultadoAtaque(
			String atacante,
			TipoEntidad tipo,
			Vector3D posicion,
			String objetivo,
			double saludObjetivo,
			int ticksEnfriamiento) {
	}
}
