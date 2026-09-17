package py.edu.uc.lp3.minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Inventario {

	private final int capacidadMaxima;
	private final List<Item> items;

	public Inventario(int capacidadMaxima) {
		if (capacidadMaxima <= 0) {
			throw new IllegalArgumentException("La capacidad del inventario debe ser mayor que cero");
		}
		this.capacidadMaxima = capacidadMaxima;
		this.items = new ArrayList<>();
	}

	public boolean agregar(Item item) {
		Objects.requireNonNull(item, "El ítem no puede ser nulo");
		if (estaLleno()) {
			return false;
		}
		return items.add(item);
	}

	public boolean quitar(Item item) {
		Objects.requireNonNull(item, "El ítem no puede ser nulo");
		return items.remove(item);
	}

	public boolean estaLleno() {
		return items.size() >= capacidadMaxima;
	}

	public int getCapacidadMaxima() {
		return capacidadMaxima;
	}

	public List<Item> getItems() {
		return Collections.unmodifiableList(items);
	}
}