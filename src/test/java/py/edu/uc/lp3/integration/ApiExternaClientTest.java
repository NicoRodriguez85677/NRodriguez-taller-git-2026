
package py.edu.uc.lp3.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

class ApiExternaClientTest {

    @Test
    void obtieneDocumentoOpenApiCorrectamente() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer servidor =
                MockRestServiceServer.bindTo(builder).build();

        ApiExternaClient cliente = new ApiExternaClient(builder);

        String url = "http://localhost:9999/v3/api-docs";
        String json = "{\"openapi\":\"3.1.0\"}";

        servidor.expect(requestTo(url))
                .andRespond(withSuccess(
                        json, MediaType.APPLICATION_JSON));

        String respuesta = cliente.obtenerOpenApi(url);

        assertThat(respuesta).isEqualTo(json);
        servidor.verify();
    }

    @Test
    void detectaErrorCuandoLaApiNoExiste() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer servidor =
                MockRestServiceServer.bindTo(builder).build();

        ApiExternaClient cliente = new ApiExternaClient(builder);

        String url = "http://localhost:9999/v3/api-docs";

        servidor.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(() -> cliente.obtenerOpenApi(url))
                .isInstanceOf(HttpClientErrorException.NotFound.class);

        servidor.verify();
    }
}
