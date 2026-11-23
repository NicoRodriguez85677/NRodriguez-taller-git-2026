
package py.edu.uc.lp3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import py.edu.uc.lp3.domain.JugadaCruzada;
import py.edu.uc.lp3.repository.JugadaCruzadaRepository;
import py.edu.uc.lp3.service.JugadaCruzadaService.Decision;
import py.edu.uc.lp3.service.JugadaCruzadaService.ValidadorExterno;

class JugadaCruzadaServiceTest {

    private final JugadaCruzadaRepository repositorio =
            mock(JugadaCruzadaRepository.class);

    private final ValidadorExterno validador =
            mock(ValidadorExterno.class);

    private final JugadaCruzadaService service =
            new JugadaCruzadaService(repositorio, validador);

    @Test
    void noGuardaCuandoA2Rechaza() {
        UUID creeperId = UUID.randomUUID();
        UUID objetivoId = UUID.randomUUID();

        when(validador.evaluarA2(creeperId, objetivoId))
                .thenReturn(new Decision(false, "Rechazado por A2"));

        var resultado = service.ejecutar(creeperId, objetivoId);

        assertThat(resultado.guardada()).isFalse();
        assertThat(resultado.rechazoDe()).isEqualTo("A2");
        assertThat(resultado.motivo()).isEqualTo("Rechazado por A2");
        assertThat(resultado.jugada()).isNull();

        verify(validador).evaluarA2(creeperId, objetivoId);
        verify(validador, never()).evaluarA3(any(), any());
        verifyNoInteractions(repositorio);
    }

    @Test
    void noGuardaCuandoA3Rechaza() {
        UUID creeperId = UUID.randomUUID();
        UUID objetivoId = UUID.randomUUID();

        when(validador.evaluarA2(creeperId, objetivoId))
                .thenReturn(new Decision(true, "Aceptado"));

        when(validador.evaluarA3(creeperId, objetivoId))
                .thenReturn(new Decision(false, "Rechazado por A3"));

        var resultado = service.ejecutar(creeperId, objetivoId);

        assertThat(resultado.guardada()).isFalse();
        assertThat(resultado.rechazoDe()).isEqualTo("A3");
        assertThat(resultado.motivo()).isEqualTo("Rechazado por A3");
        assertThat(resultado.jugada()).isNull();

        verify(validador).evaluarA2(creeperId, objetivoId);
        verify(validador).evaluarA3(creeperId, objetivoId);
        verifyNoInteractions(repositorio);
    }

    @Test
    void guardaSolamenteCuandoAmbosAceptan() {
        UUID creeperId = UUID.randomUUID();
        UUID objetivoId = UUID.randomUUID();

        when(validador.evaluarA2(creeperId, objetivoId))
                .thenReturn(new Decision(true, "Aceptado"));

        when(validador.evaluarA3(creeperId, objetivoId))
                .thenReturn(new Decision(true, "Aceptado"));

        when(repositorio.save(any(JugadaCruzada.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        var resultado = service.ejecutar(creeperId, objetivoId);

        assertThat(resultado.guardada()).isTrue();
        assertThat(resultado.rechazoDe()).isNull();
        assertThat(resultado.jugada()).isNotNull();
        assertThat(resultado.jugada().getCreeperId())
                .isEqualTo(creeperId);
        assertThat(resultado.jugada().getObjetivoId())
                .isEqualTo(objetivoId);

        verify(validador).evaluarA2(creeperId, objetivoId);
        verify(validador).evaluarA3(creeperId, objetivoId);
        verify(repositorio).save(any(JugadaCruzada.class));
    }
}
