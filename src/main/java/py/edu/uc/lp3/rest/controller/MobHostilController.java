
package py.edu.uc.lp3.rest.controller;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import py.edu.uc.lp3.domain.*;
import py.edu.uc.lp3.service.MobHostilService;

@RestController
@RequestMapping("/api/minecraft/mobs")
@Tag(name = "Mobs", description = "CRUD de Creeper y Zombie persistidos en H2.")
public class MobHostilController {

    private final MobHostilService service;

    public MobHostilController(MobHostilService service) {
        this.service = service;
    }

    public record CrearMob(
            double x, double y, double z,
            Double tiempoDetonacion) {
    }

    public record ActualizarMob(
            double x, double y, double z,
            double salud) {
    }

    public record MobRespuesta(
            UUID id,
            TipoEntidad tipo,
            String nombre,
            double salud,
            Vector3D posicion,
            String sonido) {
    }

    private MobRespuesta respuesta(MobHostil mob) {
        return new MobRespuesta(
                mob.getId(),
                mob.getTipo(),
                mob.getNombre(),
                mob.getSalud(),
                mob.getPosicion(),
                mob.emitirSonido());
    }

    @Operation(summary = "Crear Creeper", description = "Crea un Creeper en la posicion indicada; tiempoDetonacion es opcional y debe ser positivo.")
    @ApiResponse(responseCode = "201", description = "Creado con UUID")
    @ApiResponse(responseCode = "400", description = "Argumentos invalidos")
    @PostMapping("/creepers")
    @ResponseStatus(HttpStatus.CREATED)
    public MobRespuesta crearCreeper(@RequestBody CrearMob datos) {
        return respuesta(service.crearCreeper(
                datos.x(),
                datos.y(),
                datos.z(),
                datos.tiempoDetonacion()));
    }

    @Operation(summary = "Crear Zombie", description = "Crea un Zombie en la posicion indicada.")
    @ApiResponse(responseCode = "201", description = "Creado con UUID")
    @ApiResponse(responseCode = "400", description = "Argumentos invalidos")
    @PostMapping("/zombies")
    @ResponseStatus(HttpStatus.CREATED)
    public MobRespuesta crearZombie(@RequestBody CrearMob datos) {
        return respuesta(service.crearZombie(
                datos.x(),
                datos.y(),
                datos.z()));
    }

    @Operation(summary = "Listar mobs persistidos")
    @GetMapping
    public List<MobRespuesta> listar() {
        return service.listar()
                .stream()
                .map(this::respuesta)
                .toList();
    }

    @Operation(summary = "Consultar mob por UUID")
    @ApiResponse(responseCode = "404", description = "UUID inexistente")
    @GetMapping("/{id}")
    public MobRespuesta obtener(@PathVariable UUID id) {
        return respuesta(service.obtener(id));
    }

    @Operation(summary = "Actualizar posicion y salud", description = "Es obligatorio enviar x, y, z y salud; no reemplaza el subtipo.")
    @ApiResponse(responseCode = "400", description = "Valores invalidos")
    @ApiResponse(responseCode = "404", description = "UUID inexistente")
    @PutMapping("/{id}")
    public MobRespuesta actualizar(
            @PathVariable UUID id,
            @RequestBody ActualizarMob datos) {

        return respuesta(service.actualizar(
                id,
                datos.x(),
                datos.y(),
                datos.z(),
                datos.salud()));
    }

    @Operation(summary = "Eliminar mob")
    @ApiResponse(responseCode = "204", description = "Eliminado")
    @ApiResponse(responseCode = "404", description = "UUID inexistente")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        service.eliminar(id);
    }
}
