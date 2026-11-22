
package py.edu.uc.lp3.service;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.domain.Jugador;
import py.edu.uc.lp3.domain.Vector3D;

@Service
public class JugadorService {

    public Jugador crearJugador(
            String nombre, double x, double y, double z) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre no puede estar vacio");
        }

        if (!Double.isFinite(x) ||
            !Double.isFinite(y) ||
            !Double.isFinite(z)) {
            throw new IllegalArgumentException(
                    "Las coordenadas deben ser finitas");
        }

        return new Jugador(nombre, new Vector3D(x, y, z));
    }
}
