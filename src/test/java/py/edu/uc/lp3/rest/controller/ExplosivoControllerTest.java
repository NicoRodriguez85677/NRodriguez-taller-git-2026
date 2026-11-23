
package py.edu.uc.lp3.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    void conservaContratoParaCreeperVivoYMuerto() throws Exception {
        MobHostil creeper = service.crearCreeper(0, 64, 0, 5.0);

        mockMvc.perform(get("/api/minecraft/explosivos/{id}", creeper.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.interfaceDominio").value("Explosivo"))
                .andExpect(jsonPath("$.puedeExplotar").value(true));

        service.actualizar(creeper.getId(), 0, 64, 0, 0);

        mockMvc.perform(get("/api/minecraft/explosivos/{id}", creeper.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.interfaceDominio").value("Explosivo"))
                .andExpect(jsonPath("$.puedeExplotar").value(false));
    }

    @Test
    void tiempoDeDetonacionInvalidoConservaElErrorHttp() throws Exception {
        mockMvc.perform(post("/api/minecraft/mobs/creepers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"x":0,"y":64,"z":0,"tiempoDetonacion":-2}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("ARGUMENTO_INVALIDO"))
                .andExpect(jsonPath("$.mensaje").value("El tiempo de detonacion debe ser positivo"));
    }

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
