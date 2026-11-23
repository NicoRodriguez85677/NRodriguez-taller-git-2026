
package py.edu.uc.lp3.rest.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import py.edu.uc.lp3.service.JugadaCruzadaService;
import py.edu.uc.lp3.service.JugadaCruzadaService.Resultado;

@RestController
@RequestMapping("/api/minecraft/jugadas-cruzadas")
@Tag(
    name = "POO-09 - APIs cruzadas",
    description = "Escenario A1 que requiere aprobacion de A2 y A3"
)
public class JugadaCruzadaController {

    private final JugadaCruzadaService service;

    public JugadaCruzadaController(
            JugadaCruzadaService service) {
        this.service = service;
    }

    public record SolicitudJugada(
            UUID creeperId,
            UUID objetivoId) {
    }

    @Operation(
        summary = "Ejecutar jugada cruzada",
        description = "Consulta A2 y A3. Solo guarda en H2 si ambos aceptan."
    )
    @ApiResponse(
        responseCode = "201",
        description = "Jugada aprobada y guardada"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud invalida"
    )
    @ApiResponse(
        responseCode = "409",
        description = "A2 o A3 rechazo la jugada"
    )
    @ApiResponse(
        responseCode = "503",
        description = "API externa no disponible"
    )
    @PostMapping
    public ResponseEntity<Resultado> ejecutar(
            @RequestBody SolicitudJugada solicitud) {

        Resultado resultado = service.ejecutar(
                solicitud.creeperId(),
                solicitud.objetivoId());

        if (!resultado.guardada()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(resultado);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resultado);
    }
}
