package py.edu.uc.lp3.minecraft;

import java.util.Objects;

public class Bloque {

	private final String material;
	private final Vector3D posicion;

	public Bloque(String material, Vector3D posicion) {
		if (material == null || material.isBlank()) {
			throw new IllegalArgumentException("El material del bloque no puede ser nulo ni vacío");
		}
		this.material = material;
		this.posicion = Objects.requireNonNull(posicion, "La posición del bloque no puede ser nula");
	}

	public String getMaterial() {
		return material;
	}

	public Vector3D getPosicion() {
		return posicion;
	}
}