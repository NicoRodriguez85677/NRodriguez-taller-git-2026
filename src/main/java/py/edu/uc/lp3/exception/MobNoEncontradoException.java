
package py.edu.uc.lp3.exception;

import java.util.UUID;

public class MobNoEncontradoException
        extends MinecraftException {

    public MobNoEncontradoException(UUID id) {
        super("No existe el mob con ID: " + id);
    }
}
