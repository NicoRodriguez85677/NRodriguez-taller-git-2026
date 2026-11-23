
package py.edu.uc.lp3.service;

import java.util.List;
import java.util.UUID;
import py.edu.uc.lp3.exception.AccionIlegalException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.exception.MobNoEncontradoException;
import py.edu.uc.lp3.domain.*;
import py.edu.uc.lp3.repository.MobHostilRepository;

@Service
public class MobHostilService {

    private final MobHostilRepository repository;

    public MobHostilService(MobHostilRepository repository) {
        this.repository = repository;
    }

    private Vector3D posicion(double x, double y, double z) {
        if (!Double.isFinite(x) || !Double.isFinite(y)
                || !Double.isFinite(z)) {
            throw new IllegalArgumentException(
                    "Las coordenadas deben ser finitas");
        }
        return new Vector3D(x, y, z);
    }

    private MobHostil buscar(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new MobNoEncontradoException(id));
    }

    @Transactional
    public MobHostil crearCreeper(
            double x, double y, double z, Double tiempoDetonacion) {

        Vector3D pos = posicion(x, y, z);
        Creeper creeper;

        if (tiempoDetonacion == null) {
            creeper = new Creeper(pos);
        } else {
            if (!Double.isFinite(tiempoDetonacion)
                    || tiempoDetonacion <= 0) {
                throw new IllegalArgumentException(
                        "El tiempo de detonacion debe ser positivo");
            }
            creeper = new Creeper(pos, tiempoDetonacion);
        }

        return repository.save(creeper);
    }

    @Transactional
    public MobHostil crearZombie(double x, double y, double z) {
        Zombie zombie = new Zombie(posicion(x, y, z));
        return repository.save(zombie);
    }

    @Transactional(readOnly = true)
    public List<MobHostil> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public MobHostil obtener(UUID id) {
        return buscar(id);
    }

    @Transactional
    public MobHostil actualizar(
            UUID id, double x, double y, double z, double salud) {

        MobHostil mob = buscar(id);
        Vector3D nuevaPosicion = posicion(x, y, z);

        if (!Double.isFinite(salud)
                || salud < 0
                || salud > mob.getSaludMaxima()) {
            throw new IllegalArgumentException(
                    "La salud debe estar entre 0 y la salud maxima");
        }

        double diferencia = salud - mob.getSalud();

        if (diferencia > 0 && !mob.estaViva()) {
            throw new IllegalArgumentException(
                    "No se puede revivir un mob muerto");
        }

        mob.moverse(nuevaPosicion);

        if (diferencia > 0) {
            mob.curar(diferencia);
        } else if (diferencia < 0) {
            mob.recibirDano(-diferencia);
        }

        return repository.save(mob);
    }

    @Transactional
    public void eliminar(UUID id) {
        repository.delete(buscar(id));
    }
@Transactional(readOnly = true)
public Entidad buscarObjetivo(MobHostil mob, List<Entidad> entidades) {
    if (mob == null) {
        throw new IllegalArgumentException(
                "El mob no puede ser nulo");
    }

    if (entidades == null) {
        throw new IllegalArgumentException(
                "La lista de entidades no puede ser nula");
    }

    Entidad masCercana = null;
    double menorDistancia = Double.MAX_VALUE;

    for (Entidad entidad : entidades) {
        if (entidad == null || entidad == mob || !entidad.estaViva()) {
            continue;
        }

        double distancia = mob.getPosicion()
                .distanciaA(entidad.getPosicion());

        if (distancia <= mob.getAlcanceVision()
                && distancia < menorDistancia) {
            menorDistancia = distancia;
            masCercana = entidad;
        }
    }

    return masCercana;
}

public ResultadoAtaqueGhast simularAtaqueGhast(
        double x, double y, double z) {

    Vector3D posicionGhast = posicion(x, y, z);

    Ghast ghast = new Ghast(posicionGhast);
    MobHostil atacante = ghast;

    Entidad objetivo = new Zombie(
            new Vector3D(x + 1, y, z));

    atacante.atacar(objetivo);

    return new ResultadoAtaqueGhast(
            atacante.getNombre(),
            atacante.getTipo(),
            atacante.getPosicion(),
            objetivo.getNombre(),
            objetivo.getSalud(),
            ghast.getTicksEnfriamientoAtaque());
}

public record ResultadoAtaqueGhast(
        String atacante,
        TipoEntidad tipo,
        Vector3D posicion,
        String objetivo,
        double saludObjetivo,
        int ticksEnfriamiento) {
}

public Explosivo obtenerExplosivo(UUID id) {
    MobHostil mob = buscar(id);

    if (!(mob instanceof Explosivo explosivo)) {
        throw new AccionIlegalException(
                "El mob no implementa la interface Explosivo");
    }

    return explosivo;
}

public ResultadoAcercamiento simularAcercamientoCreeper(
        double distanciaMaxima) {

    if (!Double.isFinite(distanciaMaxima)
            || distanciaMaxima < 0) {
        throw new IllegalArgumentException(
                "La distancia maxima debe ser finita y no negativa");
    }

    Creeper sinLimite = new Creeper(
            new Vector3D(0, 64, 0));

    Creeper conLimite = new Creeper(
            new Vector3D(0, 64, 0));

    Zombie objetivo = new Zombie(
            new Vector3D(3, 64, 0));

    // Primera sobrecarga
    sinLimite.acercarse(objetivo);

    // Segunda sobrecarga
    conLimite.acercarse(objetivo, distanciaMaxima);

    return new ResultadoAcercamiento(
            sinLimite.getPosicion(),
            conLimite.getPosicion(),
            distanciaMaxima);
}

public record ResultadoAcercamiento(
        Vector3D posicionSinLimite,
        Vector3D posicionConLimite,
        double distanciaMaxima) {
}

}
