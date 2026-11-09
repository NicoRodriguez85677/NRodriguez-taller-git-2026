
package py.edu.uc.lp3.rest.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.exception.AccionIlegalException;
import py.edu.uc.lp3.exception.MobNoEncontradoException;
import py.edu.uc.lp3.minecraft.Creeper;
import py.edu.uc.lp3.minecraft.Explosivo;
import py.edu.uc.lp3.minecraft.MobHostil;
import py.edu.uc.lp3.repository.MobHostilRepository;

@RestController
@RequestMapping("/api/minecraft/explosivos")
public class ExplosivoController {

    private final MobHostilRepository repository;

    public ExplosivoController(MobHostilRepository repository) {
        this.repository = repository;
    }

    public record ExplosivoRespuesta(
            UUID id,
            String clase,
            String interfaceDominio,
            boolean puedeExplotar) {
    }

    @GetMapping("/{id}")
    public ExplosivoRespuesta consultar(@PathVariable UUID id) {

        MobHostil mob = repository.findById(id)
                .orElseThrow(() -> new MobNoEncontradoException(id));

        if (!(mob instanceof Explosivo)) {
            throw new AccionIlegalException(
                    "El mob no implementa la interface Explosivo");
        }

        Creeper creeper = (Creeper) mob;

        return new ExplosivoRespuesta(
                creeper.getId(),
                creeper.getClass().getSimpleName(),
                Explosivo.class.getSimpleName(),
                creeper.estaViva() && !creeper.estaDetonando());
    }
}
