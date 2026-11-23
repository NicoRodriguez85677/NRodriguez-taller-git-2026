
package py.edu.uc.lp3.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class JugadaCruzada {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID creeperId;
    private UUID objetivoId;
    private LocalDateTime fecha;

    // Constructor requerido por JPA
    protected JugadaCruzada() {
    }

    public JugadaCruzada(UUID creeperId, UUID objetivoId) {
        this.creeperId = creeperId;
        this.objetivoId = objetivoId;
        this.fecha = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getCreeperId() {
        return creeperId;
    }

    public UUID getObjetivoId() {
        return objetivoId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
