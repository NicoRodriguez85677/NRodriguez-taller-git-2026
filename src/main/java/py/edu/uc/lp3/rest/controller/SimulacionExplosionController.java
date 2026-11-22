
package py.edu.uc.lp3.rest.controller;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import py.edu.uc.lp3.domain.SimulacionExplosion;
import py.edu.uc.lp3.service.ExplosionService;

@RestController
@RequestMapping("/api/minecraft/simulaciones/explosiones")
@Tag(name = "Simulaciones de explosiones", description = "Detonacion legal del Creeper y dano calculado por distancia, con historial en H2.")
public class SimulacionExplosionController {

    private final ExplosionService service;

    public SimulacionExplosionController(ExplosionService service) {
        this.service = service;
    }

    public record SolicitudExplosion(
            @Schema(description = "UUID del Creeper registrado") UUID creeperId,
            @Schema(description = "UUID del mob objetivo registrado") UUID objetivoId) {
    }

    @Operation(
        summary = "Simular y persistir explosion",
        description = "Regla 1: solo puede detonar un Creeper vivo, no detonando y que no haya explotado. " +
                "Regla 2: el dano decrece con la distancia, radio maximo de 4 bloques. " +
                "Si la operacion es legal se guarda en H2; un rechazo no debe dejar una simulacion nueva."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Explosion ejecutada y persistida"),
        @ApiResponse(responseCode = "400", description = "Argumentos invalidos"),
        @ApiResponse(responseCode = "404", description = "Mob inexistente"),
        @ApiResponse(responseCode = "409", description = "Accion ilegal del dominio")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimulacionExplosion simular(
            @RequestBody SolicitudExplosion solicitud) {

        return service.simular(
                solicitud.creeperId(),
                solicitud.objetivoId());
    }

    @Operation(summary = "Listar simulaciones persistidas")
    @GetMapping
    public List<SimulacionExplosion> listar() {
        return service.listar();
    }

    @Operation(summary = "Listar historial de un Creeper")
    @GetMapping("/creeper/{id}")
    public List<SimulacionExplosion> porCreeper(
            @PathVariable UUID id) {
        return service.porCreeper(id);
    }
}
