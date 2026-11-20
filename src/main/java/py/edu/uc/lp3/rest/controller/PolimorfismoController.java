

package py.edu.uc.lp3.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.minecraft.*;

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

    @GetMapping("/tipos-ataque")
    public List<AtaqueRespuesta> tiposAtaque() {

        List<Atacante> atacantes = List.of(
                new Creeper(new Vector3D(0, 64, 0)),
                new Zombie(new Vector3D(5, 64, 0)));

        return atacantes.stream()
                .map(atacante -> new AtaqueRespuesta(
                        atacante.getClass().getSimpleName(),
                        atacante.getFuerzaAtaque(),
                        atacante.tipoAtaque()))
                .toList();
    }

    public record SonidoEntidad(
            String nombre,
            TipoEntidad tipo,
            String sonido) {
    }

    public record AtaqueRespuesta(
            String clase,
            double fuerzaAtaque,
            String tipoAtaque) {
    }
}
