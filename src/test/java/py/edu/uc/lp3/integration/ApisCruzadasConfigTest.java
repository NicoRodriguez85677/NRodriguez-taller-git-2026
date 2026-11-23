
package py.edu.uc.lp3.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApisCruzadasConfigTest {

    @Test
    void detectaCuandoNingunaApiEstaConfigurada() {
        ApisCruzadasConfig config =
                new ApisCruzadasConfig("", "");

        assertThat(config.estanConfiguradas()).isFalse();
    }

    @Test
    void detectaCuandoFaltaUnaApi() {
        ApisCruzadasConfig config =
                new ApisCruzadasConfig(
                        "http://localhost:8081", "");

        assertThat(config.estanConfiguradas()).isFalse();
    }

    @Test
    void detectaCuandoAmbasApisEstanConfiguradas() {
        ApisCruzadasConfig config =
                new ApisCruzadasConfig(
                        "http://localhost:8081",
                        "http://localhost:8082");

        assertThat(config.estanConfiguradas()).isTrue();
        assertThat(config.getUrlA2())
                .isEqualTo("http://localhost:8081");
        assertThat(config.getUrlA3())
                .isEqualTo("http://localhost:8082");
    }
}
