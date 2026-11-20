
package py.edu.uc.lp3.domain;

public interface Atacante {

    double getFuerzaAtaque();

    void atacar(Entidad objetivo);

    String tipoAtaque();
}
