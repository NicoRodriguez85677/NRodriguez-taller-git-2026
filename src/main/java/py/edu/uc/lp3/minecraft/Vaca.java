package py.edu.uc.lp3.minecraft;

public class Vaca extends Animal {

    private static final double CANTIDAD_CURACION_ALIMENTO = 2;

    public Vaca(Vector3D posicion) {
        super(TipoEntidad.OTRO, "Vaca", 10, posicion, false);
    }

    @Override
    public String emitirSonido() {
        return "Muuu";
    }

    @Override
    public boolean alimentar(Item alimento) {
        if (alimento == null) {
            throw new IllegalArgumentException("El alimento no puede ser nulo");
        }

        curar(CANTIDAD_CURACION_ALIMENTO);
        return true;
    }
}