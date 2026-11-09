
package py.edu.uc.lp3.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import py.edu.uc.lp3.minecraft.MobHostil;

public interface MobHostilRepository
        extends JpaRepository<MobHostil, UUID> {
}
