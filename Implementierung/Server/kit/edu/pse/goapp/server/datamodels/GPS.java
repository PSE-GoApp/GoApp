
package kit.edu.pse.goapp.server.datamodels;

public class GPS {
	private double x;
	private double y;
	private double z;

	public static double distance(GPS first, GPS second) {
		double distanceX = first.x - second.x;
		double distanceY = first.y - second.y;
		double distanceZ = first.z - second.z;
		double distanceTotal = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2) + Math.pow(distanceZ, 2));
		return distanceTotal;
	}

	public static boolean isClose(GPS first, GPS second, double radius) {
		if (distance(first, second) <= radius) {
			return true;
		}
		return false;
	}

	public GPS(double x, double y, double z) {
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

}
