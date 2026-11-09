
package py.edu.uc.lp3.minecraft;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SimulacionExplosion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID creeperId;
    private UUID objetivoId;
    private double distancia;
    private double danoAplicado;
    private double saludAntes;
    private double saludDespues;
    private LocalDateTime fecha;

    protected SimulacionExplosion() {
    }

    public SimulacionExplosion(
            UUID creeperId,
            UUID objetivoId,
            double distancia,
            double danoAplicado,
            double saludAntes,
            double saludDespues) {

        this.creeperId = creeperId;
        this.objetivoId = objetivoId;
        this.distancia = distancia;
        this.danoAplicado = danoAplicado;
        this.saludAntes = saludAntes;
        this.saludDespues = saludDespues;
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

    public double getDistancia() {
        return distancia;
    }

    public double getDanoAplicado() {
        return danoAplicado;
    }

    public double getSaludAntes() {
        return saludAntes;
    }

    public double getSaludDespues() {
        return saludDespues;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
