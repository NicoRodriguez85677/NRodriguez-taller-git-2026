package py.edu.uc.lp3;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(
    title = "Minecraft - API de Nicolas Rodriguez",
    version = "1.0",
    description = "Servicio REST de Minecraft: CRUD, reglas de explosion, " +
                  "simulacion persistida en H2 y documentacion para POO-09."
))
public class OpenApiConfig {
}
