
package py.edu.uc.lp3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.repository.JugadaCruzadaRepository;
import py.edu.uc.lp3.service.JugadaCruzadaService.Decision;
import py.edu.uc.lp3.service.JugadaCruzadaService.ValidadorExterno;

@SpringBootTest
@Transactional
class JugadaCruzadaH2Test {

    @Autowired
    private JugadaCruzadaRepository repositorio;

    @Test
    void rechazoA2NoModificaH2() {
        long cantidadAntes = repositorio.count();

        UUID creeperId = UUID.randomUUID();
        UUID objetivoId = UUID.randomUUID();

        ValidadorExterno validador =
                mock(ValidadorExterno.class);

        when(validador.evaluarA2(creeperId, objetivoId))
                .thenReturn(new Decision(false, "Rechazado por A2"));

        JugadaCruzadaService service =
                new JugadaCruzadaService(repositorio, validador);

        var resultado = service.ejecutar(creeperId, objetivoId);

        assertThat(resultado.guardada()).isFalse();
        assertThat(resultado.rechazoDe()).isEqualTo("A2");
        assertThat(repositorio.count()).isEqualTo(cantidadAntes);
    }

    @Test
    void rechazoA3NoModificaH2() {
        long cantidadAntes = repositorio.count();

        UUID creeperId = UUID.randomUUID();
        UUID objetivoId = UUID.randomUUID();

        ValidadorExterno validador =
                mock(ValidadorExterno.class);

        when(validador.evaluarA2(creeperId, objetivoId))
                .thenReturn(new Decision(true, "Aceptado"));

        when(validador.evaluarA3(creeperId, objetivoId))
                .thenReturn(new Decision(false, "Rechazado por A3"));

        JugadaCruzadaService service =
                new JugadaCruzadaService(repositorio, validador);

        var resultado = service.ejecutar(creeperId, objetivoId);

        assertThat(resultado.guardada()).isFalse();
        assertThat(resultado.rechazoDe()).isEqualTo("A3");
        assertThat(repositorio.count()).isEqualTo(cantidadAntes);
    }

    @Test
    void ambasAceptanYSeGuardaEnH2() {
        long cantidadAntes = repositorio.count();

        UUID creeperId = UUID.randomUUID();
        UUID objetivoId = UUID.randomUUID();

        ValidadorExterno validador =
                mock(ValidadorExterno.class);

        when(validador.evaluarA2(creeperId, objetivoId))
                .thenReturn(new Decision(true, "Aceptado"));

        when(validador.evaluarA3(creeperId, objetivoId))
                .thenReturn(new Decision(true, "Aceptado"));

        JugadaCruzadaService service =
                new JugadaCruzadaService(repositorio, validador);

        var resultado = service.ejecutar(creeperId, objetivoId);

        assertThat(resultado.guardada()).isTrue();
        assertThat(resultado.jugada()).isNotNull();
        assertThat(repositorio.count()).isEqualTo(cantidadAntes + 1);
        assertThat(repositorio.findById(
                resultado.jugada().getId())).isPresent();
    }
}
