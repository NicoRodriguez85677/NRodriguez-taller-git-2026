
package py.edu.uc.lp3.rest.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import py.edu.uc.lp3.domain.JugadaCruzada;

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
            @Schema(description = "UUID de un Creeper existente en A1") UUID creeperId,
            @Schema(description = "UUID del objetivo en la API de Sofi") UUID objetivoId,
            @Schema(description = "Danio simulado en Amir", minimum = "0", defaultValue = "0") Integer danio) {

        public SolicitudJugada {
            if (danio == null) {
                danio = 0;
            }
        }
    }

    @Operation(
        summary = "Ejecutar jugada cruzada",
        description = "Amir valida que su Zombie sobreviva al danio. Luego Sofi valida que el objetivo exista y sea un Zombie vivo. A1 guarda la jugada si ambos aceptan."
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
    @ApiResponse(responseCode = "404", description = "No existe el Creeper local")
    @PostMapping
    public ResponseEntity<Resultado> ejecutar(
            @RequestBody SolicitudJugada solicitud) {

        Resultado resultado = service.ejecutar(
                solicitud.creeperId(),
                solicitud.objetivoId(),
                solicitud.danio());

        if (!resultado.guardada()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(resultado);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resultado);
    }

    @Operation(
        summary = "Listar jugadas cruzadas",
        description = "Recupera las jugadas aprobadas y persistidas en H2."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de jugadas guardadas"
    )
    @GetMapping
    public List<JugadaCruzada> listar() {
        return service.listar();
    }

}
