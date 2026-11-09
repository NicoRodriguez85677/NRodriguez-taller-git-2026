
package py.edu.uc.lp3.minecraft;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Entidad {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TipoEntidad tipo;

    private String nombre;
    private double salud;

    @Embedded
    private Vector3D posicion;

    // Constructor requerido por JPA
    protected Entidad() {
    }

    protected Entidad(TipoEntidad tipo, String nombre,
                      double salud, Vector3D posicion) {
        this.id = UUID.randomUUID();
        this.tipo = Objects.requireNonNull(tipo,
                "El tipo de entidad no puede ser nulo");

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre no puede estar vacio");
        }

        if (!Double.isFinite(salud) || salud < 0) {
            throw new IllegalArgumentException(
                    "La salud debe ser finita y no negativa");
        }

        this.nombre = nombre;
        this.salud = salud;
        this.posicion = Objects.requireNonNull(posicion,
                "La posicion no puede ser nula");
    }

    public UUID getId() {
        return id;
    }

    public TipoEntidad getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSalud() {
        return salud;
    }

    public Vector3D getPosicion() {
        return posicion;
    }

    public void recibirDano(double cantidad) {
        if (!Double.isFinite(cantidad) || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "El dano debe ser positivo y finito");
        }
        if (estaViva()) {
            salud = Math.max(0, salud - cantidad);
        }
    }

    public void curar(double cantidad) {
        if (!Double.isFinite(cantidad) || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La curacion debe ser positiva y finita");
        }
        if (estaViva()) {
            salud += cantidad;
        }
    }

    public boolean estaViva() {
        return salud > 0;
    }

    public void moverse(Vector3D destino) {
        this.posicion = Objects.requireNonNull(
                destino, "El destino no puede ser nulo");
    }

    public abstract void tick();

    public abstract String emitirSonido();

    protected void cambiarSalud(double nuevaSalud) {
        if (!Double.isFinite(nuevaSalud) || nuevaSalud < 0) {
            throw new IllegalArgumentException(
                    "La salud debe ser finita y no negativa");
        }
        this.salud = nuevaSalud;
    }
}
