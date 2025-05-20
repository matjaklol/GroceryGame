package game.actors;

import org.dyn4j.dynamics.*;
import org.dyn4j.collision.*;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import game.core.Game;
import game.core.GameApplet;
import game.core.objects.GameObject;
import game.core.objects.Rectangle;
import game.util.BoxBuilder;
import game.util.Constants;
import processing.core.PImage;

public class GroceryBag extends GameObject {
	private PImage inner;
	private PImage outer;
	
	private final float width = 0.37f - (0.02f);
	private final float height = 0.5f;
	private boolean mouseOver = false;
	private Body body;
	public GroceryBag(float x, float y) {
		this.inner = Game.getImageService().getImage("Grocery Bag BACK.png");
		this.outer = Game.getImageService().getImage("Grocery Bag FRONT.png");
		
		this.body = BoxBuilder.createOpenBox(x, y, width, height, 0.02, 0.3);
		body.setUserData(this);
		this.body.setMass(MassType.NORMAL);
		//Bag needs 4 box colliders. 1 below, 2 on either side, 1 as a mouseover() event.
		
	}
	
	public Body getBody() {
		return this.body;
	}
	
	private void checkMouse() {
		Fixture f = body.getFixture(new Vector2(Game.get().worldMouseX, Game.get().worldMouseY));
		
		if(f == null) {
			mouseOver = false;
			return;
		}
		
		mouseOver = true;
		return;
	}
	
	private float bagAlpha = 0.0f;
	private void updateBagTransparency() {
		if(this.mouseOver) {
			if(bagAlpha < 0.25f) {
				bagAlpha += Game.get().delta;
			}
		} else {
			if(bagAlpha > 0f) {
				bagAlpha -= Game.get().delta;
			}
		}
	}
	
	public void update() {
		checkMouse();
		updateBagTransparency();
	}
	
	
	
	public void draw() {
		GameApplet.getBuffer().pushMatrix();
		GameObject.applyTransform(GameApplet.getBuffer(), this.body.getTransform());
		if(bagAlpha > 0f) {
			GameApplet.getBuffer().translate(0, 0, -0.01f);
			GameApplet.getBuffer().image(this.inner, -Constants.toPixels(width/2), -Constants.toPixels(height/2), Constants.toPixels(width), Constants.toPixels(height));
			GameApplet.getBuffer().translate(0, 0, 0.01f);
		}
		GameApplet.getBuffer().tint(255, 255, 255, (((bagAlpha)/0.25f) * -200) + 255);
		GameApplet.getBuffer().translate(0, 0, 0.01f);
		GameApplet.getBuffer().image(this.outer, -Constants.toPixels(width/2), -Constants.toPixels(height/2), Constants.toPixels(width), Constants.toPixels(height));
		GameApplet.getBuffer().translate(0, 0, -0.01f);
		GameApplet.getBuffer().noTint();
		GameApplet.getBuffer().popMatrix();
	}
}
