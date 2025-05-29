package game.core.objects;

import org.dyn4j.geometry.Transform;

import game.management.Scene;
import game.util.Constants;
import processing.core.PGraphics;

public class GameObject {
	protected Scene parentScene;
	protected boolean dead = false;
	public static void applyTransform(PGraphics graphic, Transform transform) {
//		graphic.pushMatrix();
//		graphic.translate(Constants.toPixels(transform.getTranslationX()), (float) transform.getTranslationY() * Constants.Meter);
		graphic.translate(Constants.toPixels(transform.getTranslationX()), Constants.toPixels(transform.getTranslationY()));
		graphic.rotate((float) transform.getRotationAngle());
//		graphic.popMatrix();
	}
	
	/**
	 * Check if this GameObject is dead and should be removed via Garbage Collection.
	 * @return true if dead, false if still in use.
	 */
	public boolean dead() {
		return this.dead;
	}
	
	/**
	 * This method is called by the Scene cleaner when an object should begin
	 * cleaning all references to allow GC to do its job.
	 */
	public void clean() {
		
	}
	
	public GameObject() {
		
	}
	
	public void update() {
		
	}
	
	public void draw() {
		
	}
	
	public void post() {
		
	}
}
