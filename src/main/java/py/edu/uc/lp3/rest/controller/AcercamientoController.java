
package py.edu.uc.lp3.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.service.MobHostilService;
import py.edu.uc.lp3.service.MobHostilService.ResultadoAcercamiento;

@RestController
@RequestMapping("/api/minecraft/creeper")
public class AcercamientoController {

    private final MobHostilService service;

    public AcercamientoController(MobHostilService service) {
        this.service = service;
    }

    @GetMapping("/acercamiento")
    public ResultadoAcercamiento demostrarAcercamiento(
            @RequestParam double distanciaMaxima) {

        return service.simularAcercamientoCreeper(distanciaMaxima);
    }
}
