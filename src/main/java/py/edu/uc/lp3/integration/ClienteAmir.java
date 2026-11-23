
package py.edu.uc.lp3.integration;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ClienteAmir {

    private final RestClient cliente;
    private final ApisCruzadasConfig config;

    public ClienteAmir(
            RestClient.Builder builder,
            ApisCruzadasConfig config) {

        this.cliente = builder.build();
        this.config = config;
    }

    public boolean zombieSobrevive(int danio) {

        if (danio < 0) {
            throw new IllegalArgumentException(
                    "El dano no puede ser negativo");
        }

        if (config.getUrlA2().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "La URL de A2 no esta configurada");
        }

        Map<?, ?> respuesta = cliente.get()
                .uri(config.getUrlA2()
                        + "/entidad/zombie?danio={danio}", danio)
                .retrieve()
                .body(Map.class);

        if (respuesta == null
                || !(respuesta.get("entidad") instanceof Map<?, ?> entidad)) {
            throw new RestClientException(
                    "Respuesta invalida de Amir");
        }

        Object viva = entidad.get("viva");

        if (!(viva instanceof Boolean)) {
            throw new RestClientException(
                    "La respuesta de Amir no contiene viva");
        }

        return (Boolean) viva;
    }
}
