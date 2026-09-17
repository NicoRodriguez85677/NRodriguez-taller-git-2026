package py.edu.uc.lp3.minecraft;

import java.util.Objects;
import java.util.UUID;

public class Item {

	private final UUID id;
	private final String nombre;

	public Item(String nombre) {
		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El nombre del ítem no puede ser nulo ni vacío");
		}
		this.id = UUID.randomUUID();
		this.nombre = nombre;
	}

	public UUID getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Item item)) {
			return false;
		}
		return id.equals(item.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return nombre;
	}
}