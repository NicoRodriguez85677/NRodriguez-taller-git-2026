
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
    private Integer danio;

    // Constructor requerido por JPA
    protected JugadaCruzada() {
    }

    public JugadaCruzada(UUID creeperId, UUID objetivoId) {
        this(creeperId, objetivoId, null);
    }

    public JugadaCruzada(UUID creeperId, UUID objetivoId, Integer danio) {
        this.danio = danio;
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

    public Integer getDanio() {
        return danio;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
