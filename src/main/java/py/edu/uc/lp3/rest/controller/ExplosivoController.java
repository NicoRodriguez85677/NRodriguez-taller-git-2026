
package py.edu.uc.lp3.rest.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import py.edu.uc.lp3.exception.AccionIlegalException;
import py.edu.uc.lp3.exception.MobNoEncontradoException;
import py.edu.uc.lp3.domain.Explosivo;
import py.edu.uc.lp3.domain.MobHostil;
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

        if (!(mob instanceof Explosivo explosivo)) {
            throw new AccionIlegalException(
                    "El mob no implementa la interface Explosivo");
        }

        return new ExplosivoRespuesta(
                mob.getId(),
                mob.getClass().getSimpleName(),
                Explosivo.class.getSimpleName(),
                explosivo.puedeExplotar());
    }
}

