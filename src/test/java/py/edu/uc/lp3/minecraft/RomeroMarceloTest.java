package py.edu.uc.lp3.minecraft;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
class RomeroMarceloTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void aplicaEnfriamientoDespuesDeAtacar() {
		Ghast ghast = new Ghast(new Vector3D(0, 64, 0));
		MobHostil atacante = ghast;
		Entidad objetivo = new Zombie(new Vector3D(1, 64, 0));

		atacante.atacar(objetivo);

		assertThat(objetivo.getSalud()).isEqualTo(14);
		assertThat(ghast.getTicksEnfriamientoAtaque()).isEqualTo(3);
		assertThatThrownBy(() -> atacante.atacar(objetivo))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("El ataque del Ghast aun esta en enfriamiento");

		ghast.tick();
		ghast.tick();
		ghast.tick();

		assertThat(ghast.puedeAtacar()).isTrue();
	}

	@Test
	void exponeElAtaqueConLaPosicionRecibidaPorUrl() throws Exception {
		mockMvc.perform(get("/api/minecraft/ghast/simular-ataque")
				.param("x", "3")
				.param("y", "70")
				.param("z", "-2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.atacante").value("Ghast"))
				.andExpect(jsonPath("$.tipo").value("GHAST"))
				.andExpect(jsonPath("$.posicion.x").value(3))
				.andExpect(jsonPath("$.posicion.y").value(70))
				.andExpect(jsonPath("$.posicion.z").value(-2))
				.andExpect(jsonPath("$.objetivo").value("Zombie"))
				.andExpect(jsonPath("$.saludObjetivo").value(14))
				.andExpect(jsonPath("$.ticksEnfriamiento").value(3));
	}
}
