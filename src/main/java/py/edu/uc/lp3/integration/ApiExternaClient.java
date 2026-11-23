
package py.edu.uc.lp3.integration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ApiExternaClient {

    private final RestClient cliente;

    public ApiExternaClient(RestClient.Builder builder) {
        this.cliente = builder.build();
    }

    public String obtenerOpenApi(String urlDocumento) {
        return cliente.get()
                .uri(urlDocumento)
                .retrieve()
                .body(String.class);
    }
}
