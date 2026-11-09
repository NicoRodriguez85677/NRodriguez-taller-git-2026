
package py.edu.uc.lp3.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import py.edu.uc.lp3.minecraft.SimulacionExplosion;

public interface SimulacionExplosionRepository
        extends JpaRepository<SimulacionExplosion, UUID> {

    List<SimulacionExplosion> findByCreeperId(UUID creeperId);
}
