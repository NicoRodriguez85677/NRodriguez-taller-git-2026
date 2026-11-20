
package py.edu.uc.lp3.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import py.edu.uc.lp3.domain.*;
import py.edu.uc.lp3.service.MobHostilService;

@RestController
@RequestMapping("/api/minecraft/mobs")
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

    @PostMapping("/creepers")
    @ResponseStatus(HttpStatus.CREATED)
    public MobRespuesta crearCreeper(@RequestBody CrearMob datos) {
        return respuesta(service.crearCreeper(
                datos.x(),
                datos.y(),
                datos.z(),
                datos.tiempoDetonacion()));
    }

    @PostMapping("/zombies")
    @ResponseStatus(HttpStatus.CREATED)
    public MobRespuesta crearZombie(@RequestBody CrearMob datos) {
        return respuesta(service.crearZombie(
                datos.x(),
                datos.y(),
                datos.z()));
    }

    @GetMapping
    public List<MobRespuesta> listar() {
        return service.listar()
                .stream()
                .map(this::respuesta)
                .toList();
    }

    @GetMapping("/{id}")
    public MobRespuesta obtener(@PathVariable UUID id) {
        return respuesta(service.obtener(id));
    }

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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        service.eliminar(id);
    }
}
