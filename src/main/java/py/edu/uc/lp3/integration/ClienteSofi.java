
package py.edu.uc.lp3.integration;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ClienteSofi {

    private final RestClient cliente;
    private final ApisCruzadasConfig config;

    public ClienteSofi(
            RestClient.Builder builder,
            ApisCruzadasConfig config) {

        this.cliente = builder.build();
        this.config = config;
    }

    public boolean zombieVivo(UUID entidadId) {

        if (entidadId == null) {
            throw new IllegalArgumentException(
                    "El ID de Sofi es obligatorio");
        }

        if (config.getUrlA3().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "La URL de A3 no esta configurada");
        }

        Map<?, ?> entidad;

        try {
            entidad = cliente.get()
                    .uri(config.getUrlA3()
                            + "/api/entidades/{id}", entidadId)
                    .retrieve()
                    .body(Map.class);

        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }

        if (entidad == null
                || !(entidad.get("tipo") instanceof String tipo)
                || !(entidad.get("salud") instanceof Number salud)) {
            throw new RestClientException(
                    "Respuesta invalida de Sofi");
        }

        return tipo.equalsIgnoreCase("ZOMBIE")
                && salud.doubleValue() > 0;
    }
}
