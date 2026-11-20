
package py.edu.uc.lp3.minecraft;

public interface Atacante {

    double getFuerzaAtaque();

    void atacar(Entidad objetivo);

    String tipoAtaque();
}
