
package py.edu.uc.lp3.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.domain.SimulacionExplosion;
import py.edu.uc.lp3.domain.Vector3D;
import py.edu.uc.lp3.domain.Zombie;
import py.edu.uc.lp3.exception.AccionIlegalException;
import py.edu.uc.lp3.exception.MobNoEncontradoException;
import py.edu.uc.lp3.repository.MobHostilRepository;
import py.edu.uc.lp3.repository.SimulacionExplosionRepository;

class ExplosionServiceTest {

    @Mock
    private MobHostilRepository mobs;

    @Mock
    private SimulacionExplosionRepository simulaciones;

    private ExplosionService service;

    @BeforeEach
    void preparar() {
        MockitoAnnotations.openMocks(this);
        service = new ExplosionService(mobs, simulaciones);

        when(simulaciones.save(any(SimulacionExplosion.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));
    }

    @Test
    void explosionExitosaAplicaDanoYGuardaSimulacion() {
        Creeper creeper = new Creeper(new Vector3D(0, 64, 0));
        Zombie zombie = new Zombie(new Vector3D(2, 64, 0));

        when(mobs.findById(creeper.getId()))
                .thenReturn(Optional.of(creeper));
        when(mobs.findById(zombie.getId()))
                .thenReturn(Optional.of(zombie));

        SimulacionExplosion resultado =
                service.simular(creeper.getId(), zombie.getId());

        assertEquals(2.0, resultado.getDistancia(), 0.001);
        assertEquals(8.5, resultado.getDanoAplicado(), 0.001);
        assertEquals(20.0, resultado.getSaludAntes(), 0.001);
        assertEquals(11.5, resultado.getSaludDespues(), 0.001);

        assertFalse(creeper.estaViva());
        assertTrue(zombie.estaViva());

        verify(mobs).save(creeper);
        verify(mobs).save(zombie);
        verify(simulaciones).save(any(SimulacionExplosion.class));
    }

    @Test
    void objetivoFueraDelRadioNoRecibeDano() {
        Creeper creeper = new Creeper(new Vector3D(0, 64, 0));
        Zombie zombie = new Zombie(new Vector3D(10, 64, 0));

        when(mobs.findById(creeper.getId()))
                .thenReturn(Optional.of(creeper));
        when(mobs.findById(zombie.getId()))
                .thenReturn(Optional.of(zombie));

        SimulacionExplosion resultado =
                service.simular(creeper.getId(), zombie.getId());

        assertEquals(10.0, resultado.getDistancia(), 0.001);
        assertEquals(0.0, resultado.getDanoAplicado(), 0.001);
        assertEquals(20.0, resultado.getSaludDespues(), 0.001);

        assertFalse(creeper.estaViva());
        assertTrue(zombie.estaViva());

        verify(simulaciones).save(any(SimulacionExplosion.class));
    }

    @Test
    void identificadoresNulosSonRechazados() {
        assertThrows(
                AccionIlegalException.class,
                () -> service.simular(null, UUID.randomUUID()));

        assertThrows(
                AccionIlegalException.class,
                () -> service.simular(UUID.randomUUID(), null));

        verifyNoInteractions(mobs, simulaciones);
    }

    @Test
    void origenQueNoEsCreeperEsRechazado() {
        Zombie origen = new Zombie(new Vector3D(0, 64, 0));
        Zombie objetivo = new Zombie(new Vector3D(2, 64, 0));

        when(mobs.findById(origen.getId()))
                .thenReturn(Optional.of(origen));
        when(mobs.findById(objetivo.getId()))
                .thenReturn(Optional.of(objetivo));

        assertThrows(
                AccionIlegalException.class,
                () -> service.simular(origen.getId(), objetivo.getId()));

        verify(mobs, never()).save(any());
        verify(simulaciones, never()).save(any());
    }

    @Test
    void mobInexistenteEsRechazado() {
        UUID idInexistente = UUID.randomUUID();

        when(mobs.findById(idInexistente))
                .thenReturn(Optional.empty());

        assertThrows(
                MobNoEncontradoException.class,
                () -> service.simular(idInexistente, UUID.randomUUID()));

        verify(mobs, never()).save(any());
        verify(simulaciones, never()).save(any());
    }

    @Test
    void creeperNoPuedeSerSuPropioObjetivo() {
        Creeper creeper = new Creeper(new Vector3D(0, 64, 0));

        when(mobs.findById(creeper.getId()))
                .thenReturn(Optional.of(creeper));

        assertThrows(
                AccionIlegalException.class,
                () -> service.simular(creeper.getId(), creeper.getId()));

        verify(mobs, never()).save(any());
        verify(simulaciones, never()).save(any());
    }

    @Test
    void creeperMuertoNoPuedeExplotar() {
        Creeper creeper = new Creeper(new Vector3D(0, 64, 0));
        Zombie objetivo = new Zombie(new Vector3D(2, 64, 0));

        creeper.recibirDano(20);

        when(mobs.findById(creeper.getId()))
                .thenReturn(Optional.of(creeper));
        when(mobs.findById(objetivo.getId()))
                .thenReturn(Optional.of(objetivo));

        assertThrows(
                AccionIlegalException.class,
                () -> service.simular(creeper.getId(), objetivo.getId()));

        verify(mobs, never()).save(any());
        verify(simulaciones, never()).save(any());
    }

    @Test
    void creeperDetonandoNoPuedeVolverAExplotar() {
        Creeper creeper = new Creeper(new Vector3D(0, 64, 0));
        Zombie objetivo = new Zombie(new Vector3D(2, 64, 0));

        creeper.comenzarDetonacion();

        when(mobs.findById(creeper.getId()))
                .thenReturn(Optional.of(creeper));
        when(mobs.findById(objetivo.getId()))
                .thenReturn(Optional.of(objetivo));

        assertThrows(
                AccionIlegalException.class,
                () -> service.simular(creeper.getId(), objetivo.getId()));

        verify(mobs, never()).save(any());
        verify(simulaciones, never()).save(any());
    }

    @Test
    void objetivoMuertoNoPuedeRecibirExplosion() {
        Creeper creeper = new Creeper(new Vector3D(0, 64, 0));
        Zombie objetivo = new Zombie(new Vector3D(2, 64, 0));

        objetivo.recibirDano(20);

        when(mobs.findById(creeper.getId()))
                .thenReturn(Optional.of(creeper));
        when(mobs.findById(objetivo.getId()))
                .thenReturn(Optional.of(objetivo));

        assertThrows(
                AccionIlegalException.class,
                () -> service.simular(creeper.getId(), objetivo.getId()));

        verify(mobs, never()).save(any());
        verify(simulaciones, never()).save(any());
    }

    @Test
    void creeperQueYaExplotoNoPuedeVolverAExplotar() {
        Creeper creeper = new Creeper(new Vector3D(0, 64, 0));
        Zombie objetivo = new Zombie(new Vector3D(2, 64, 0));

        creeper.comenzarDetonacion();
        creeper.explotar();

        when(mobs.findById(creeper.getId()))
                .thenReturn(Optional.of(creeper));
        when(mobs.findById(objetivo.getId()))
                .thenReturn(Optional.of(objetivo));

        AccionIlegalException error = assertThrows(
                AccionIlegalException.class,
                () -> service.simular(creeper.getId(), objetivo.getId()));

        assertEquals("El Creeper ya exploto", error.getMessage());

        verify(mobs, never()).save(any());
        verify(simulaciones, never()).save(any());
    }


}
