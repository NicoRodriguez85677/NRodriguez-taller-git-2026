
package py.edu.uc.lp3.domain;

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
        this.tipo = tipo;
        this.nombre = nombre;
        this.salud = salud;
        this.posicion = posicion;
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
        if (estaViva() && Double.isFinite(cantidad) && cantidad > 0) {
            salud = Math.max(0, salud - cantidad);
        }
    }

    public void curar(double cantidad) {
        if (estaViva() && Double.isFinite(cantidad) && cantidad > 0) {
            salud += cantidad;
        }
    }

    public boolean estaViva() {
        return salud > 0;
    }

    public void moverse(Vector3D destino) {
        if (destino != null) {
            this.posicion = destino;
        }
    }

    public abstract void tick();

    public abstract String emitirSonido();

    protected void cambiarSalud(double nuevaSalud) {
        this.salud = nuevaSalud;
    }
}
