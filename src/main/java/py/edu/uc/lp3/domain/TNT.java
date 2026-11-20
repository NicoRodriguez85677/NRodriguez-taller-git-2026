
package py.edu.uc.lp3.domain;

public class TNT implements Explosivo {

    private boolean encendida;
    private boolean explotada;

    public TNT() {
        this.encendida = false;
        this.explotada = false;
    }

    public void encender() {
        if (!explotada) {
            this.encendida = true;
        }
    }

    @Override
    public boolean puedeExplotar() {
        return encendida && !explotada;
    }

    @Override
    public boolean explotar() {
        if (!puedeExplotar()) {
            return false;
        }

        explotada = true;
        encendida = false;
        return true;
    }

    public boolean yaExploto() {
        return explotada;
    }
}
