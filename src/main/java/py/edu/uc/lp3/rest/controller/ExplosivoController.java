

package py.edu.uc.lp3.rest.controller;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.*;

import py.edu.uc.lp3.domain.Explosivo;
import py.edu.uc.lp3.service.MobHostilService;

@RestController
@RequestMapping("/api/minecraft/explosivos")
@Tag(name = "Consulta de explosivo", description = "Capacidad de explosion de un mob segun interfaz Explosivo.")
public class ExplosivoController {

    private final MobHostilService service;

    public ExplosivoController(MobHostilService service) {
        this.service = service;
    }

    public record ExplosivoRespuesta(
            UUID id,
            String clase,
            String interfaceDominio,
            boolean puedeExplotar) {
    }


        @Operation(summary = "Consultar si un mob puede explotar", description = "Consulta la regla de estado de Explosivo sin detonarlo ni cambiar la base H2.")
        @ApiResponse(responseCode = "200", description = "Estado del explosivo consultado correctamente")
        @ApiResponse(responseCode = "404", description = "Mob inexistente")
        @ApiResponse(responseCode = "409", description = "El mob no implementa Explosivo")
        @GetMapping("/{id}")
        public ExplosivoRespuesta consultar(@PathVariable UUID id) {

        Explosivo explosivo = service.obtenerExplosivo(id);

        return new ExplosivoRespuesta(
                id,
                explosivo.getClass().getSimpleName(),
                Explosivo.class.getSimpleName(),
                explosivo.puedeExplotar());
        }

}

