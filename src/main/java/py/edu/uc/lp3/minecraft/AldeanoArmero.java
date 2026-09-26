package py.edu.uc.lp3.minecraft;

public class AldeanoArmero extends Aldeano {

    private int nivelHerreria;

    public AldeanoArmero(Vector3D posicion) {
        super(posicion);
        setProfesion("Armero");
        this.nivelHerreria = 1;
    }

    public int getNivelHerreria() {
        return nivelHerreria;
    }

    public void setNivelHerreria(int nivelHerreria) {
        if (nivelHerreria < 1) {
            throw new IllegalArgumentException("El nivel de herrería debe ser al menos 1");
        }
        this.nivelHerreria = nivelHerreria;
    }

    public void forjarArmadura() {
        System.out.println("El aldeano armero está forjando una armadura de nivel " + nivelHerreria + ".");
    }

    @Override
    public String emitirSonido() {
        return "Hmm... ¡Echa un vistazo a estas armaduras de hierro!";
    }
}