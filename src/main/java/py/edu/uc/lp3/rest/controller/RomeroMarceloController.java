
package py.edu.uc.lp3.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.service.MobHostilService;
import py.edu.uc.lp3.service.MobHostilService.ResultadoAtaqueGhast;

@RestController
@RequestMapping("/api/minecraft/ghast")
public class RomeroMarceloController {

    private final MobHostilService service;

    public RomeroMarceloController(MobHostilService service) {
        this.service = service;
    }

    @GetMapping("/simular-ataque")
    public ResultadoAtaqueGhast simularAtaque(
            @RequestParam double x,
            @RequestParam double y,
            @RequestParam double z) {

        return service.simularAtaqueGhast(x, y, z);
    }
}
