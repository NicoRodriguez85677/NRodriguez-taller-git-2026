package py.edu.uc.lp3.minecraft;

import java.util.Objects;

public class Vector3D {

	private final double x;
	private final double y;
	private final double z;

	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double distanciaA(Vector3D otro) {
		double dx = x - otro.x;
		double dy = y - otro.y;
		double dz = z - otro.z;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Vector3D otro)) {
			return false;
		}
		return Double.compare(otro.x, x) == 0 && Double.compare(otro.y, y) == 0 && Double.compare(otro.z, z) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public String toString() {
		return "Vector3D(" + x + ", " + y + ", " + z + ")";
	}
}