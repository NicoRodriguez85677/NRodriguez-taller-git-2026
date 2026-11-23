
package py.edu.uc.lp3.service;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import py.edu.uc.lp3.domain.JugadaCruzada;
import py.edu.uc.lp3.repository.JugadaCruzadaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JugadaCruzadaService {

    public record Decision(
            boolean aceptada,
            String motivo) {
    }

    public interface ValidadorExterno {

        Decision evaluarA2(UUID creeperId, UUID objetivoId);

        Decision evaluarA3(UUID creeperId, UUID objetivoId);
    }

    public record Resultado(
            boolean guardada,
            String rechazoDe,
            String motivo,
            JugadaCruzada jugada) {
    }

    private final JugadaCruzadaRepository jugadas;
    private final ValidadorExterno validador;

    public JugadaCruzadaService(
            JugadaCruzadaRepository jugadas,
            ValidadorExterno validador) {

        this.jugadas = jugadas;
        this.validador = validador;
    }

    public Resultado ejecutar(
            UUID creeperId, UUID objetivoId) {

        if (creeperId == null || objetivoId == null) {
            throw new IllegalArgumentException(
                    "Los identificadores son obligatorios");
        }


        Decision a2;

        try {
            a2 = Objects.requireNonNull(
                    validador.evaluarA2(creeperId, objetivoId),
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
                    validador.evaluarA3(creeperId, objetivoId),
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
                new JugadaCruzada(creeperId, objetivoId));

        return new Resultado(
                true, null, null, guardada);
    }
}
