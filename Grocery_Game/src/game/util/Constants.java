package game.util;

import org.dyn4j.geometry.Vector2;

/**
 * The general values used for converting from pixel measurements to real world
 * values. The meter is the most common value seen, but there is also the option for
 * the kilometer.
 * @author keyboard
 *
 */
public class Constants {
	//M = 256
	public static final float Meter = 256f;
	public static final float Dekameter = 10 * Meter;
	public static final float Hectometer = 100 * Meter;
	public static final float Kilometer = 1000 * Meter;
	
	public static final float Cm = Meter / 100f;
	
	public static float toPixels(float meters) {
		return meters * Meter;
	}
	
	public static double toMeters(float pixels) {
		return pixels / Meter;
	}
	
	public static Vector2 toPixels(Vector2 meters) {
		return meters.copy().multiply(Meter);
	}
	
	public static Vector2 toMeters(Vector2 pixels) {
		return pixels.copy().divide(Meter);
	}

	public static float toPixels(double meters) {
		
		return (float) meters * Meter;
	}

	public static Vector2 toMeters(float x, float y) {
		return new Vector2(x / Meter, y / Meter);
	}
	
}
