
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
import py.edu.uc.lp3.repository.MobHostilRepository;
import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.domain.Vector3D;

@SpringBootTest(properties = "poo09.a2.base-url=")
@AutoConfigureMockMvc
@Transactional
class JugadaCruzadaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JugadaCruzadaRepository repositorio;

    @Autowired
    private MobHostilRepository mobs;

    @Test
    void apiNoDisponibleResponde503YNoGuardaEnH2()
            throws Exception {

        long cantidadAntes = repositorio.count();

        var creeper = mobs.save(new Creeper(new Vector3D(0, 0, 0)));
        String solicitud = """
                {
                    "creeperId": "%s",
                    "objetivoId": "22222222-2222-4222-8222-222222222222"
                }
                """.formatted(creeper.getId());

        mockMvc.perform(post("/api/minecraft/jugadas-cruzadas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(solicitud))
                .andExpect(status().isServiceUnavailable());

        assertThat(repositorio.count())
                .isEqualTo(cantidadAntes);
    }
}
