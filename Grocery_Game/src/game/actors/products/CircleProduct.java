package game.actors.products;

import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;
//import org.dyn4j.collision.Fixture;

import processing.core.PImage;

import game.util.Constants;
//import game.core.Game;
import game.core.GameApplet;
import game.core.objects.GameObject;
//import game.management.Scene;
/**
 * The generic product that uses an elliptical physics collision shape.
 * @author keyboard
 *
 */
public class CircleProduct extends Product {
	private float width, height;
	private float displayWidth, displayHeight;
	/**
	 * 
	 */
	public CircleProduct(double x, double y, double width, double height) {
		this.fixture = new BodyFixture(Geometry.createEllipse(width, height));
//		bodyFixture.setFr
		body = new Body();
		body.addFixture(fixture);
		body.setUserData(this);
		
		body.setMass(MassType.NORMAL);
		body.translate(x, y);
		this.width = Constants.toPixels(width);
		this.height = Constants.toPixels(height);
		this.displayWidth = -Constants.toPixels(width/2);
		this.displayHeight = -Constants.toPixels(height/2);
		
		this.buffer = GameApplet.getBuffer();
	}
	
	public void setImage(PImage newImage) {
		this.image = newImage;
	}
	
	public void update() {
		
	}
	
	public void draw() {
		GameApplet.getBuffer().pushMatrix();
		GameObject.applyTransform(GameApplet.getBuffer(), this.body.getTransform());
		GameApplet.getBuffer().image(this.image, displayWidth, displayHeight, width, height);
		GameApplet.getBuffer().popMatrix();
	}
	
	

}
