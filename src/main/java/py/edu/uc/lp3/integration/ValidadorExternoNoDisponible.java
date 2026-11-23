
package py.edu.uc.lp3.integration;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import py.edu.uc.lp3.service.JugadaCruzadaService.Decision;
import py.edu.uc.lp3.service.JugadaCruzadaService.ValidadorExterno;

@Component
public class ValidadorExternoNoDisponible
        implements ValidadorExterno {

    @Override
    public Decision evaluarA2(UUID creeperId, UUID objetivoId) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "A2 no esta conectada: falta integrar su API real");
    }

    @Override
    public Decision evaluarA3(UUID creeperId, UUID objetivoId) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "A3 no esta conectada: falta integrar su API real");
    }
}
