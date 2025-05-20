package game.actors.products;

import game.core.objects.GameObject;

import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;
import org.dyn4j.collision.Fixture;

import processing.core.PGraphics;
import processing.core.PImage;

import game.util.Constants;
import game.core.Game;
import game.core.GameApplet;
import game.management.Scene;

/**
 * The general form of the product class. Used for things you can scan in the grocery store, or even restocking.
 * @author keyboard
 *
 */
public class Product extends GameObject {
	protected Body body;
	protected BodyFixture fixture;
	protected PImage image;
	protected PGraphics buffer;
	public Product() {
		this.buffer = GameApplet.getBuffer();
		this.image = Game.getImageService().emergencyGraphic();
	}
	
	
	/**
	 * Sets the display graphic.
	 * @param image
	 */
	public void setImage(PImage image) {
		this.image = image;
	}
	
	/**
	 * Change the buffer to which this GameObject draws to.
	 * @param buffer
	 */
	public void setDrawBuffer(PGraphics buffer) {
		this.buffer = buffer;
	}
	
	public Body getBody() {
		return this.body;
	}
	
	public BodyFixture getFixture() {
		return this.fixture;
	}
	
	public void update() {
		
	}
	
	public void draw() {
		if(this.image == null) {
			return;
		}
		
		this.buffer.pushMatrix();
		GameObject.applyTransform(buffer, body.getTransform());
		this.buffer.image(this.image, 0, 0);
		
		this.buffer.popMatrix();
		
	}
	
	public void clean() {
		this.buffer = null;
		this.body.setUserData(null);
		this.image = null;
	}

}
