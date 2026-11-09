
package py.edu.uc.lp3.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import py.edu.uc.lp3.minecraft.*;
import py.edu.uc.lp3.repository.MobHostilRepository;
import py.edu.uc.lp3.exception.MobNoEncontradoException;
@RestController
@RequestMapping("/api/minecraft/mobs")
public class MobHostilController {

    private final MobHostilRepository repository;

    public MobHostilController(MobHostilRepository repository) {
        this.repository = repository;
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


    private MobHostil buscar(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new MobNoEncontradoException(id));
    }


    private Vector3D posicion(double x, double y, double z) {
        if (!Double.isFinite(x) || !Double.isFinite(y)
                || !Double.isFinite(z)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Las coordenadas deben ser finitas");
        }
        return new Vector3D(x, y, z);
    }

    @PostMapping("/creepers")
    @ResponseStatus(HttpStatus.CREATED)
    public MobRespuesta crearCreeper(@RequestBody CrearMob datos) {
        Vector3D pos = posicion(datos.x(), datos.y(), datos.z());

        Creeper creeper;
        if (datos.tiempoDetonacion() == null) {
            creeper = new Creeper(pos);
        } else {
            double tiempo = datos.tiempoDetonacion();
            if (!Double.isFinite(tiempo) || tiempo <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El tiempo de detonacion debe ser positivo");
            }
            creeper = new Creeper(pos, tiempo);
        }

        return respuesta(repository.save(creeper));
    }

    @PostMapping("/zombies")
    @ResponseStatus(HttpStatus.CREATED)
    public MobRespuesta crearZombie(@RequestBody CrearMob datos) {
        Zombie zombie = new Zombie(
                posicion(datos.x(), datos.y(), datos.z()));

        return respuesta(repository.save(zombie));
    }

    @GetMapping
    public List<MobRespuesta> listar() {
        return repository.findAll()
                .stream()
                .map(this::respuesta)
                .toList();
    }

    @GetMapping("/{id}")
    public MobRespuesta obtener(@PathVariable UUID id) {
        return respuesta(buscar(id));
    }

    @PutMapping("/{id}")
    public MobRespuesta actualizar(
            @PathVariable UUID id,
            @RequestBody ActualizarMob datos) {

        MobHostil mob = buscar(id);

        if (!Double.isFinite(datos.salud())
                || datos.salud() < 0
                || datos.salud() > mob.getSaludMaxima()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La salud debe estar entre 0 y la salud maxima");
        }

        mob.moverse(posicion(datos.x(), datos.y(), datos.z()));

        double diferencia = datos.salud() - mob.getSalud();

        if (diferencia > 0) {
            if (!mob.estaViva()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "No se puede revivir un mob muerto");
            }
            mob.curar(diferencia);
        } else if (diferencia < 0) {
            mob.recibirDano(-diferencia);
        }

        return respuesta(repository.save(mob));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        MobHostil mob = buscar(id);
        repository.delete(mob);
    }
}
