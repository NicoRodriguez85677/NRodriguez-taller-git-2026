
package py.edu.uc.lp3.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.exception.AccionIlegalException;
import py.edu.uc.lp3.exception.MobNoEncontradoException;
import py.edu.uc.lp3.minecraft.Creeper;
import py.edu.uc.lp3.minecraft.MobHostil;
import py.edu.uc.lp3.minecraft.SimulacionExplosion;
import py.edu.uc.lp3.repository.MobHostilRepository;
import py.edu.uc.lp3.repository.SimulacionExplosionRepository;

@Service
public class ExplosionService {

    private final MobHostilRepository mobs;
    private final SimulacionExplosionRepository simulaciones;

    public ExplosionService(
            MobHostilRepository mobs,
            SimulacionExplosionRepository simulaciones) {
        this.mobs = mobs;
        this.simulaciones = simulaciones;
    }

    @Transactional
    public SimulacionExplosion simular(
            UUID creeperId, UUID objetivoId) {

        if (creeperId == null || objetivoId == null) {
            throw new AccionIlegalException(
                    "Los identificadores son obligatorios");
        }

        MobHostil origen = mobs.findById(creeperId)
                .orElseThrow(() ->
                        new MobNoEncontradoException(creeperId));

        MobHostil objetivo = mobs.findById(objetivoId)
                .orElseThrow(() ->
                        new MobNoEncontradoException(objetivoId));

        if (!(origen instanceof Creeper creeper)) {
            throw new AccionIlegalException(
                    "El origen debe ser un Creeper");
        }

        if (creeperId.equals(objetivoId)) {
            throw new AccionIlegalException(
                    "El Creeper no puede ser su propio objetivo");
        }

        if (!creeper.estaViva() || !objetivo.estaViva()) {
            throw new AccionIlegalException(
                    "Los mobs deben estar vivos");
        }

        if (creeper.estaDetonando()) {
            throw new AccionIlegalException(
                    "El Creeper ya esta detonando");
        }

        double distancia = creeper.getPosicion()
                .distanciaA(objetivo.getPosicion());

        double saludAntes = objetivo.getSalud();
        double dano = creeper.calcularDanoExplosion(objetivo);

        creeper.comenzarDetonacion();

        if (!creeper.explotar()) {
            throw new AccionIlegalException(
                    "No se pudo ejecutar la explosion");
        }

        if (dano > 0) {
            objetivo.recibirDano(dano);
        }

        double danoReal = saludAntes - objetivo.getSalud();

        SimulacionExplosion simulacion =
                new SimulacionExplosion(
                        creeperId,
                        objetivoId,
                        distancia,
                        danoReal,
                        saludAntes,
                        objetivo.getSalud());

        mobs.save(creeper);
        mobs.save(objetivo);

        return simulaciones.save(simulacion);
    }

    @Transactional(readOnly = true)
    public List<SimulacionExplosion> listar() {
        return simulaciones.findAll();
    }

    @Transactional(readOnly = true)
    public List<SimulacionExplosion> porCreeper(UUID id) {
        return simulaciones.findByCreeperId(id);
    }
}
