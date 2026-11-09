
package py.edu.uc.lp3.minecraft;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SerVivo extends Entidad {

    private double saludMaxima;
    private boolean envenenado;
    private double regeneracion;

    // Constructor requerido por JPA
    protected SerVivo() {
        super();
    }

    protected SerVivo(TipoEntidad tipo, String nombre,
                      double salud, double saludMaxima,
                      Vector3D posicion) {
        super(tipo, nombre, salud, posicion);

        if (!Double.isFinite(saludMaxima) || saludMaxima <= 0) {
            throw new IllegalArgumentException(
                    "La salud maxima debe ser positiva y finita");
        }

        if (salud > saludMaxima) {
            throw new IllegalArgumentException(
                    "La salud no puede superar la salud maxima");
        }

        this.saludMaxima = saludMaxima;
        this.envenenado = false;
        this.regeneracion = 0;
    }

    public double getSaludMaxima() {
        return saludMaxima;
    }

    public boolean estaEnvenenado() {
        return envenenado;
    }

    public void envenenar() {
        this.envenenado = true;
    }

    public void curarEnvenenamiento() {
        this.envenenado = false;
    }

    public void aplicarRegeneracion(double cantidadPorTick) {
        if (!Double.isFinite(cantidadPorTick) || cantidadPorTick < 0) {
            throw new IllegalArgumentException(
                    "La regeneracion debe ser finita y no negativa");
        }
        this.regeneracion = cantidadPorTick;
    }

    @Override
    public void curar(double cantidad) {
        if (!Double.isFinite(cantidad) || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La curacion debe ser positiva y finita");
        }

        cambiarSalud(Math.min(getSalud() + cantidad, saludMaxima));
    }

    @Override
    public void tick() {
        if (envenenado && estaViva()) {
            recibirDano(1);
        }

        if (estaViva() && regeneracion > 0) {
            curar(regeneracion);
        }
    }

    @Override
    public String emitirSonido() {
        return "Sonido generico de un ser vivo";
    }
}
