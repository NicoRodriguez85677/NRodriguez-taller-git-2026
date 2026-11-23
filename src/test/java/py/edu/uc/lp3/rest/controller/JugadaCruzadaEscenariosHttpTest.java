
package py.edu.uc.lp3.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.springframework.web.client.ResourceAccessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.repository.JugadaCruzadaRepository;
import py.edu.uc.lp3.service.JugadaCruzadaService.Decision;
import py.edu.uc.lp3.service.JugadaCruzadaService.ValidadorExterno;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class JugadaCruzadaEscenariosHttpTest {

    private static final UUID CREEPER_ID =
            UUID.fromString("11111111-1111-4111-8111-111111111111");

    private static final UUID OBJETIVO_ID =
            UUID.fromString("22222222-2222-4222-8222-222222222222");

    private static final String SOLICITUD = """
            {
                "creeperId": "11111111-1111-4111-8111-111111111111",
                "objetivoId": "22222222-2222-4222-8222-222222222222"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JugadaCruzadaRepository repositorio;

    @MockitoBean
    private ValidadorExterno validador;

    @Test
    void rechazoA2Devuelve409YNoGuarda() throws Exception {
        long antes = repositorio.count();

        when(validador.evaluarA2(CREEPER_ID, OBJETIVO_ID))
                .thenReturn(new Decision(false, "Rechazo A2"));

        mockMvc.perform(post("/api/minecraft/jugadas-cruzadas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(SOLICITUD))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.guardada").value(false))
                .andExpect(jsonPath("$.rechazoDe").value("A2"))
                .andExpect(jsonPath("$.motivo").value("Rechazo A2"));

        assertThat(repositorio.count()).isEqualTo(antes);
        verify(validador, never()).evaluarA3(any(), any());
    }

    @Test
    void rechazoA3Devuelve409YNoGuarda() throws Exception {
        long antes = repositorio.count();

        when(validador.evaluarA2(CREEPER_ID, OBJETIVO_ID))
                .thenReturn(new Decision(true, "Aceptado"));

        when(validador.evaluarA3(CREEPER_ID, OBJETIVO_ID))
                .thenReturn(new Decision(false, "Rechazo A3"));

        mockMvc.perform(post("/api/minecraft/jugadas-cruzadas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(SOLICITUD))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.guardada").value(false))
                .andExpect(jsonPath("$.rechazoDe").value("A3"))
                .andExpect(jsonPath("$.motivo").value("Rechazo A3"));

        assertThat(repositorio.count()).isEqualTo(antes);
    }

    @Test
    void ambasAceptanDevuelve201YGuarda() throws Exception {
        long antes = repositorio.count();

        when(validador.evaluarA2(CREEPER_ID, OBJETIVO_ID))
                .thenReturn(new Decision(true, "Aceptado"));

        when(validador.evaluarA3(CREEPER_ID, OBJETIVO_ID))
                .thenReturn(new Decision(true, "Aceptado"));

        mockMvc.perform(post("/api/minecraft/jugadas-cruzadas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(SOLICITUD))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.guardada").value(true))
                .andExpect(jsonPath("$.jugada.creeperId")
                        .value(CREEPER_ID.toString()))
                .andExpect(jsonPath("$.jugada.objetivoId")
                        .value(OBJETIVO_ID.toString()));

        assertThat(repositorio.count()).isEqualTo(antes + 1);
    }

@Test
void errorConexionA2Devuelve503YNoGuarda() throws Exception {
    long antes = repositorio.count();

    when(validador.evaluarA2(CREEPER_ID, OBJETIVO_ID))
            .thenThrow(new ResourceAccessException(
                    "No se pudo conectar con A2"));

    mockMvc.perform(post("/api/minecraft/jugadas-cruzadas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(SOLICITUD))
            .andExpect(status().isServiceUnavailable());

    assertThat(repositorio.count()).isEqualTo(antes);

    verify(validador, never()).evaluarA3(any(), any());
}

@Test
void errorConexionA3Devuelve503YNoGuarda() throws Exception {
    long antes = repositorio.count();

    when(validador.evaluarA2(CREEPER_ID, OBJETIVO_ID))
            .thenReturn(new Decision(true, "Aceptado"));

    when(validador.evaluarA3(CREEPER_ID, OBJETIVO_ID))
            .thenThrow(new ResourceAccessException(
                    "No se pudo conectar con A3"));

    mockMvc.perform(post("/api/minecraft/jugadas-cruzadas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(SOLICITUD))
            .andExpect(status().isServiceUnavailable());

    assertThat(repositorio.count()).isEqualTo(antes);
}

}
