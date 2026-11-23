package py.edu.uc.lp3.integration;

import java.util.UUID;

import org.springframework.stereotype.Component;

import py.edu.uc.lp3.service.JugadaCruzadaService.Decision;
import py.edu.uc.lp3.service.JugadaCruzadaService.ValidadorExterno;

@Component
public class ValidadorExternoHttp implements ValidadorExterno {

    private final ClienteAmir amir;
    private final ClienteSofi sofi;

    public ValidadorExternoHttp(ClienteAmir amir, ClienteSofi sofi) {
        this.amir = amir;
        this.sofi = sofi;
    }

    @Override
    public Decision evaluarA2(int danio) {
        boolean aceptada = amir.zombieSobrevive(danio);
        return new Decision(aceptada, aceptada ? null : "El Zombie de Amir no sobrevive al ataque");
    }

    @Override
    public Decision evaluarA3(UUID objetivoId) {
        boolean aceptada = sofi.zombieVivo(objetivoId);
        return new Decision(aceptada, aceptada ? null : "El objetivo de Sofi no es un Zombie vivo");
    }
}
