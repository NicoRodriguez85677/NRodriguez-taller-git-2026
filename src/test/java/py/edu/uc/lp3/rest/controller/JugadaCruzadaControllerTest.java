
package py.edu.uc.lp3.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.repository.JugadaCruzadaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class JugadaCruzadaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JugadaCruzadaRepository repositorio;

    @Test
    void apiNoDisponibleResponde503YNoGuardaEnH2()
            throws Exception {

        long cantidadAntes = repositorio.count();

        String solicitud = """
                {
                    "creeperId": "11111111-1111-4111-8111-111111111111",
                    "objetivoId": "22222222-2222-4222-8222-222222222222"
                }
                """;

        mockMvc.perform(post("/api/minecraft/jugadas-cruzadas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(solicitud))
                .andExpect(status().isServiceUnavailable());

        assertThat(repositorio.count())
                .isEqualTo(cantidadAntes);
    }
}
