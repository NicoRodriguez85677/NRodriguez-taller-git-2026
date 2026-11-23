
package py.edu.uc.lp3.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AcercamientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void limiteDosNoPermiteAcercamiento() throws Exception {
        mockMvc.perform(get("/api/minecraft/creeper/acercamiento")
                .param("distanciaMaxima", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.posicionSinLimite.x").value(3.0))
            .andExpect(jsonPath("$.posicionConLimite.x").value(0.0));
    }

    @Test
    void limiteCuatroPermiteAcercamiento() throws Exception {
        mockMvc.perform(get("/api/minecraft/creeper/acercamiento")
                .param("distanciaMaxima", "4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.posicionSinLimite.x").value(3.0))
            .andExpect(jsonPath("$.posicionConLimite.x").value(3.0));
    }
}
