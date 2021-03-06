
package edu.kit.pse.client.goapp.datamodels;

import java.util.List;

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
    
	public static GPS median(List<GPS> positions)
	{
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		for(GPS position : positions)
		{
			x+= position.getX();
			y+=position.getY();
			z+= position.getZ();
		}
		x/=positions.size();
		y/= positions.size();
		z/=positions.size();
		
		return new GPS(x, y, z);
		
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
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		// Class name is Employ & have lastname
		GPS g = (GPS) obj;
		 if(g.getX() == x && g.getY() == y && g.getZ() == z) {
			return true;
		}
		return false;
	}
}
