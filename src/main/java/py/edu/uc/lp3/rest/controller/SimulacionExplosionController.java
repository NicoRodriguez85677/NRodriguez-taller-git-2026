
package py.edu.uc.lp3.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import py.edu.uc.lp3.minecraft.SimulacionExplosion;
import py.edu.uc.lp3.service.ExplosionService;

@RestController
@RequestMapping("/api/minecraft/simulaciones/explosiones")
public class SimulacionExplosionController {

    private final ExplosionService service;

    public SimulacionExplosionController(ExplosionService service) {
        this.service = service;
    }

    public record SolicitudExplosion(
            UUID creeperId,
            UUID objetivoId) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimulacionExplosion simular(
            @RequestBody SolicitudExplosion solicitud) {

        return service.simular(
                solicitud.creeperId(),
                solicitud.objetivoId());
    }

    @GetMapping
    public List<SimulacionExplosion> listar() {
        return service.listar();
    }

    @GetMapping("/creeper/{id}")
    public List<SimulacionExplosion> porCreeper(
            @PathVariable UUID id) {
        return service.porCreeper(id);
    }
}
