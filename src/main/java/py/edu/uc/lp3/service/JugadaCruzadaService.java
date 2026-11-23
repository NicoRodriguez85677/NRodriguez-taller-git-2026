
package py.edu.uc.lp3.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.domain.JugadaCruzada;
import py.edu.uc.lp3.exception.MobNoEncontradoException;
import py.edu.uc.lp3.repository.JugadaCruzadaRepository;
import py.edu.uc.lp3.repository.MobHostilRepository;

@Service
public class JugadaCruzadaService {

    public record Decision(
            boolean aceptada,
            String motivo) {
    }

    public interface ValidadorExterno {

        Decision evaluarA2(int danio);

        Decision evaluarA3(UUID objetivoId);
    }

    public record Resultado(
            boolean guardada,
            String rechazoDe,
            String motivo,
            JugadaCruzada jugada) {
    }

    private final JugadaCruzadaRepository jugadas;
    private final MobHostilRepository mobs;
    private final ValidadorExterno validador;

    public JugadaCruzadaService(
            JugadaCruzadaRepository jugadas,
            MobHostilRepository mobs,
            ValidadorExterno validador) {

        this.jugadas = jugadas;
        this.mobs = mobs;
        this.validador = validador;
    }

    public Resultado ejecutar(UUID creeperId, UUID objetivoId) {
        return ejecutar(creeperId, objetivoId, 0);
    }

    public Resultado ejecutar(
            UUID creeperId, UUID objetivoId, int danio) {

        if (creeperId == null || objetivoId == null) {
            throw new IllegalArgumentException(
                    "Los identificadores son obligatorios");
        }

        if (danio < 0) {
            throw new IllegalArgumentException(
                    "El danio no puede ser negativo");
        }

        var mob = mobs.findById(creeperId)
                .orElseThrow(() -> new MobNoEncontradoException(creeperId));
        if (!(mob instanceof Creeper)) {
            throw new IllegalArgumentException("El creeperId debe corresponder a un Creeper local");
        }

        Decision a2;

        try {
            a2 = Objects.requireNonNull(
                    validador.evaluarA2(danio),
                    "A2 no devolvio una decision");
        } catch (RestClientException e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo consultar la API de A2", e);
        }

        if (!a2.aceptada()) {
            return new Resultado(
                    false, "A2", a2.motivo(), null);
        }

        Decision a3;

        try {
            a3 = Objects.requireNonNull(
                    validador.evaluarA3(objetivoId),
                    "A3 no devolvio una decision");
        } catch (RestClientException e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo consultar la API de A3", e);
        }

        if (!a3.aceptada()) {
            return new Resultado(
                    false, "A3", a3.motivo(), null);
        }

        JugadaCruzada guardada = jugadas.save(
                new JugadaCruzada(creeperId, objetivoId, danio));

        return new Resultado(
                true, null, null, guardada);
    }

    @Transactional(readOnly = true)
    public List<JugadaCruzada> listar() {
        return jugadas.findAll();
    }

}
