
package py.edu.uc.lp3.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApisCruzadasConfig {

    private final String urlA2;
    private final String urlA3;

    public ApisCruzadasConfig(
            @Value("${poo09.a2.base-url:}") String urlA2,
            @Value("${poo09.a3.base-url:}") String urlA3) {

        this.urlA2 = urlA2;
        this.urlA3 = urlA3;
    }

    public String getUrlA2() {
        return urlA2;
    }

    public String getUrlA3() {
        return urlA3;
    }

    public boolean estanConfiguradas() {
        return !urlA2.isBlank() && !urlA3.isBlank();
    }
}
