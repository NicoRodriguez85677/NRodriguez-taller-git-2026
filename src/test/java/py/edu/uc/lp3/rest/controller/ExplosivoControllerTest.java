
package py.edu.uc.lp3.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import py.edu.uc.lp3.domain.MobHostil;
import py.edu.uc.lp3.service.MobHostilService;

@SpringBootTest
@AutoConfigureMockMvc
class ExplosivoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MobHostilService service;

    @Test
    void zombieNoEsExplosivoDevuelve409() throws Exception {
        MobHostil zombie = service.crearZombie(0, 64, 0);

        try {
            mockMvc.perform(get(
                    "/api/minecraft/explosivos/{id}",
                    zombie.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error")
                        .value("ACCION_ILEGAL"))
                .andExpect(jsonPath("$.mensaje")
                        .value("El mob no implementa la interface Explosivo"));
        } finally {
            service.eliminar(zombie.getId());
        }
    }
}
