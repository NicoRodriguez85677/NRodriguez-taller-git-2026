package py.edu.uc.lp3.minecraft;

import static org.assertj.core.api.Assertions.assertThat;
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
class PolimorfismoTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void emiteSonidoDistintoSegunLaRamaDeLaJerarquia() {
		Entidad zombie = new Zombie(new Vector3D(0, 64, 0));
		Entidad cerdo = new Cerdo(new Vector3D(10, 64, 0));

		assertThat(zombie.emitirSonido()).isEqualTo("Grrr...");
		assertThat(cerdo.emitirSonido()).isEqualTo("Oinc oinc");
		assertThat(zombie.emitirSonido()).isNotEqualTo(cerdo.emitirSonido());
	}

	@Test
	void elEndpointExponeElComportamientoPolimorfico() throws Exception {
		mockMvc.perform(get("/api/minecraft/polimorfismo/emitir-sonido"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nombre").value("Zombie"))
				.andExpect(jsonPath("$[0].tipo").value("ZOMBIE"))
				.andExpect(jsonPath("$[0].sonido").value("Grrr..."))
				.andExpect(jsonPath("$[1].nombre").value("Cerdo"))
				.andExpect(jsonPath("$[1].tipo").value("CERDO"))
				.andExpect(jsonPath("$[1].sonido").value("Oinc oinc"));
	}
@Test
void creeperYZombieImplementanAtacante() {
    Atacante creeper = new Creeper(
            new Vector3D(0, 64, 0));

    Atacante zombie = new Zombie(
            new Vector3D(10, 64, 0));

    assertThat(creeper.getFuerzaAtaque())
            .isEqualTo(17.0);

    assertThat(zombie.getFuerzaAtaque())
            .isEqualTo(3.0);

    assertThat(creeper.tipoAtaque())
            .isNotEqualTo(zombie.tipoAtaque());
}

@Test
void endpointExponeTiposDeAtaque() throws Exception {
    mockMvc.perform(get(
            "/api/minecraft/polimorfismo/tipos-ataque"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].clase")
                    .value("Creeper"))
            .andExpect(jsonPath("$[0].fuerzaAtaque")
                    .value(17.0))
            .andExpect(jsonPath("$[1].clase")
                    .value("Zombie"))
            .andExpect(jsonPath("$[1].fuerzaAtaque")
                    .value(3.0));
}
}