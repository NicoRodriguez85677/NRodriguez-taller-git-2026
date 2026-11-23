package py.edu.uc.lp3.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.domain.Vector3D;
import py.edu.uc.lp3.domain.Zombie;
import py.edu.uc.lp3.repository.JugadaCruzadaRepository;
import py.edu.uc.lp3.repository.MobHostilRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class JugadaCruzadaEscenariosHttpTest {

    private static final UUID OBJETIVO = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final String URL_AMIR = "GET /entidad/zombie?danio=";
    private static final String URL_SOFI = "GET /api/entidades/" + OBJETIVO;
    private static final List<String> CONSULTAS = new CopyOnWriteArrayList<>();
    private static final HttpServer AMIR = iniciarServidor();
    private static final HttpServer SOFI = iniciarServidor();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JugadaCruzadaRepository jugadas;

    @Autowired
    private MobHostilRepository mobs;

    private UUID creeperId;
    private long cantidadInicial;

    @DynamicPropertySource
    static void urls(DynamicPropertyRegistry registro) {
        registro.add("poo09.a2.base-url", () -> "http://127.0.0.1:" + AMIR.getAddress().getPort());
        registro.add("poo09.a3.base-url", () -> "http://127.0.0.1:" + SOFI.getAddress().getPort());
    }

    @BeforeEach
    void preparar() {
        CONSULTAS.clear();
        creeperId = mobs.save(new Creeper(new Vector3D(0, 0, 0))).getId();
        cantidadInicial = jugadas.count();
    }

    @AfterAll
    static void cerrarServidores() {
        AMIR.stop(0);
        SOFI.stop(0);
    }

    @Test
    void ambasAceptanDevuelve201YGuarda() throws Exception {
        responder(AMIR, 200, "{\"entidad\":{\"viva\":true}}");
        responder(SOFI, 200, "{\"tipo\":\"ZOMBIE\",\"salud\":20}");

        ejecutar(7)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.guardada").value(true))
                .andExpect(jsonPath("$.jugada.creeperId").value(creeperId.toString()))
                .andExpect(jsonPath("$.jugada.objetivoId").value(OBJETIVO.toString()))
                .andExpect(jsonPath("$.jugada.danio").value(7));

        assertThat(CONSULTAS).containsExactly(URL_AMIR + 7, URL_SOFI);
        assertThat(jugadas.count()).isEqualTo(cantidadInicial + 1);
        mockMvc.perform(get("/api/minecraft/jugadas-cruzadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.creeperId == '%s')].danio", creeperId)
                        .value(org.hamcrest.Matchers.hasItem(7)));
    }

    @Test
    void rechazoA2ConHttp200Devuelve409YSofiNoSeConsulta() throws Exception {
        responder(AMIR, 200, "{\"entidad\":{\"viva\":false}}");

        ejecutar(25)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.guardada").value(false))
                .andExpect(jsonPath("$.rechazoDe").value("A2"));

        assertThat(CONSULTAS).containsExactly(URL_AMIR + 25);
        assertThat(jugadas.count()).isEqualTo(cantidadInicial);
    }

    @Test
    void objetivoAusenteDevuelve409ComoRechazoA3() throws Exception {
        verificarRechazoA3(404, "{}");
    }

    @Test
    void objetivoDeOtroTipoDevuelve409ComoRechazoA3() throws Exception {
        verificarRechazoA3(200, "{\"tipo\":\"CREEPER\",\"salud\":20}");
    }

    @Test
    void zombieSinSaludDevuelve409ComoRechazoA3() throws Exception {
        verificarRechazoA3(200, "{\"tipo\":\"ZOMBIE\",\"salud\":0}");
    }

    @Test
    void desconexionA2Devuelve503YNoGuarda() throws Exception {
        responder(AMIR, 0, "");

        ejecutar(0).andExpect(status().isServiceUnavailable());

        assertThat(CONSULTAS).isNotEmpty().containsOnly(URL_AMIR + 0);
        assertThat(jugadas.count()).isEqualTo(cantidadInicial);
    }

    @Test
    void desconexionA3Devuelve503YNoGuarda() throws Exception {
        responder(AMIR, 200, "{\"entidad\":{\"viva\":true}}");
        responder(SOFI, 0, "");

        ejecutar(0).andExpect(status().isServiceUnavailable());

        assertThat(CONSULTAS).startsWith(URL_AMIR + 0, URL_SOFI);
        assertThat(jugadas.count()).isEqualTo(cantidadInicial);
    }

    @Test
    void errorHttpDeSofiDevuelve503YNoGuarda() throws Exception {
        responder(AMIR, 200, "{\"entidad\":{\"viva\":true}}");
        responder(SOFI, 500, "{}");

        ejecutar(0).andExpect(status().isServiceUnavailable());

        assertThat(CONSULTAS).containsExactly(URL_AMIR + 0, URL_SOFI);
        assertThat(jugadas.count()).isEqualTo(cantidadInicial);
    }

    @Test
    void creeperLocalInexistenteDevuelve404() throws Exception {
        creeperId = UUID.randomUUID();

        ejecutar(0).andExpect(status().isNotFound());

        assertThat(CONSULTAS).isEmpty();
        assertThat(jugadas.count()).isEqualTo(cantidadInicial);
    }

    @Test
    void mobLocalDeOtroTipoDevuelve400() throws Exception {
        creeperId = mobs.save(new Zombie(new Vector3D(0, 0, 0))).getId();

        ejecutar(0).andExpect(status().isBadRequest());

        assertThat(CONSULTAS).isEmpty();
        assertThat(jugadas.count()).isEqualTo(cantidadInicial);
    }

    @Test
    void danioNegativoDevuelve400() throws Exception {
        ejecutar(-1).andExpect(status().isBadRequest());

        assertThat(CONSULTAS).isEmpty();
        assertThat(jugadas.count()).isEqualTo(cantidadInicial);
    }

    @Test
    void openApiPublicaElEscenarioYLosCodigosDeRespuesta() throws Exception {
        String operacion = "$.paths['/api/minecraft/jugadas-cruzadas'].post";
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(operacion + ".requestBody").exists())
                .andExpect(jsonPath(operacion + ".responses['201']").exists())
                .andExpect(jsonPath(operacion + ".responses['409']").exists())
                .andExpect(jsonPath(operacion + ".responses['503']").exists());
    }

    private void verificarRechazoA3(int codigo, String respuesta) throws Exception {
        responder(AMIR, 200, "{\"entidad\":{\"viva\":true}}");
        responder(SOFI, codigo, respuesta);

        ejecutar(0)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.guardada").value(false))
                .andExpect(jsonPath("$.rechazoDe").value("A3"));

        assertThat(CONSULTAS).containsExactly(URL_AMIR + 0, URL_SOFI);
        assertThat(jugadas.count()).isEqualTo(cantidadInicial);
    }

    private ResultActions ejecutar(int danio) throws Exception {
        return mockMvc.perform(post("/api/minecraft/jugadas-cruzadas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"creeperId":"%s","objetivoId":"%s","danio":%d}
                        """.formatted(creeperId, OBJETIVO, danio)));
    }

    private static HttpServer iniciarServidor() {
        try {
            var servidor = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            servidor.createContext("/", intercambio -> {
                CONSULTAS.add(intercambio.getRequestMethod() + " " + intercambio.getRequestURI());
                intercambio.sendResponseHeaders(500, -1);
                intercambio.close();
            });
            servidor.start();
            return servidor;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo iniciar el servidor de prueba", e);
        }
    }

    private static void responder(HttpServer servidor, int codigo, String respuesta) {
        servidor.removeContext("/");
        servidor.createContext("/", intercambio -> {
            try (intercambio) {
                CONSULTAS.add(intercambio.getRequestMethod() + " " + intercambio.getRequestURI());
                if (codigo != 0) {
                    byte[] cuerpo = respuesta.getBytes(StandardCharsets.UTF_8);
                    intercambio.getResponseHeaders().set("Content-Type", "application/json");
                    intercambio.sendResponseHeaders(codigo, cuerpo.length);
                    intercambio.getResponseBody().write(cuerpo);
                }
            }
        });
    }
}
