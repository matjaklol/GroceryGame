package game.core.objects;

import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;
//import org.dyn4j.collision.Fixture;
//import org.dyn4j.geometry.Geometry;

import game.core.Game;
import game.core.GameApplet;
import game.management.Scene;
import game.util.Constants;
import processing.core.PImage;
import processing.core.PShape;
public class Rectangle extends GameObject {
	
	@Deprecated
	private float x, y;
	private float width, height;
	private Transform transform;
	private Transform prevTransform;
	private Body myBody;
	private PShape displayShape;
	private PImage displayImage;
	
	public Rectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		BodyFixture bodyFixture = new BodyFixture(Geometry.createRectangle(width, height));
		myBody = new Body();
		
		
		myBody.addFixture(bodyFixture);
		
		myBody.setMass(MassType.NORMAL);
		
		myBody.setUserData(this);
		
		myBody.translate(x, y);
	}
	
	public void setScene(Scene newScene) {
		this.parentScene = newScene;
	}
	
	public Body getBody() {
		return this.myBody;
	}
	
	public void setImage(PImage toDisplay) {
		this.displayImage = toDisplay;
	}
	
	public void setDisplayShape(PShape toDisplay) {
		this.displayShape = toDisplay;
	}
	
	public void saveTransform() {
		prevTransform = myBody.getTransform().copy();
	}
	
	public Transform getInterpolatedTransform(double alpha) {
        Vector2 interpPos = prevTransform.getTranslation().lerp(myBody.getTransform().getTranslation(), alpha);
//		Vector2 interpPos = prevTransform.getTranslation().copy().multiply(1.0 - alpha).add(transform.getTranslation().copy().multiply(alpha));
        double interpRot = lerpAngle(prevTransform.getRotationAngle(), myBody.getTransform().getRotationAngle(), alpha);
        Transform result = new Transform();
        result.setRotation(interpRot);
        result.setTranslation(interpPos);
        return result;
    }
	
	private double lerpAngle(double a, double b, double alpha) {
        double diff = b - a;
        diff = Math.atan2(Math.sin(diff), Math.cos(diff)); // Shortest angle difference
        return a + alpha * diff;
    }
	
	protected void updateGlowing() {
		if(!glowingChanged) {
			return;
		}
		if(this.glowing) {
			if(glowValue < 0.25f) {
				glowValue += Game.get().delta;
			}
		} else {
			if(glowValue >= 0f) {
				glowValue -= Game.get().delta;
			}
		}
	}
	public void update() {
		saveTransform();
		updateGlowing();
	}
	
	private boolean glowing = false;
	private boolean glowingChanged = false;
	public void setGlowing(boolean value) {
		glowingChanged = true;
		this.glowing = value;
	}
	
	private float glowValue = 0;
	
	public void draw() {
//		GameApplet.getBuffer().pushMatrix();
		this.transform = myBody.getTransform();
//		GameApplet.getBuffer().translate((float) transform.getTranslationX() * Constants.Meter, (float) transform.getTranslationY() * Constants.Meter);
//		GameApplet.getBuffer().rotate((float) transform.getRotationAngle());
		GameApplet.getBuffer().pushMatrix();
//		GameObject.applyTransform(GameApplet.getBuffer(), getInterpolatedTransform(GameApplet.getInstance().game.alpha));
		GameObject.applyTransform(GameApplet.getBuffer(), myBody.getTransform());
		//draw here:
		if(this.displayImage != null) {
			
//			GameApplet.getBuffer().stroke(41, 64, 255, (glowValue/0.25f) * 100);
			GameApplet.getBuffer().stroke(41, 64, 255, 200);
			GameApplet.getBuffer().strokeWeight((Math.max(glowValue,0)/0.15f) * 3);
			GameApplet.getBuffer().fill(0,0,0,0);
			GameApplet.getBuffer().rect(-width * Constants.Meter/2, -height * Constants.Meter/2, width * Constants.Meter, height * Constants.Meter);
			GameApplet.getBuffer().strokeWeight(0);
			GameApplet.getBuffer().noStroke();
			
//			GameApplet.getBuffer().shape(displayShape, -(width/2) * Constants.Meter, -(height/2) * Constants.Meter, width * Constants.Meter, height * Constants.Meter);
//			GameApplet.getBuffer().image(displayImage, -(width/2) * Constants.Meter, -(height/2) * Constants.Meter, width * Constants.Meter * 2, height * Constants.Meter * 2);
			GameApplet.getBuffer().image(displayImage, -width * Constants.Meter / 2, -height * Constants.Meter / 2, width * Constants.Meter, height * Constants.Meter);
//			GameApplet.getBuffer().stroke(237, 234, 19);
//			GameApplet.getBuffer().strokeWeight(2);
//			GameApplet.getBuffer().fill(0, 0, 0, 0);
//			GameApplet.getBuffer().rect(-(width) * Constants.Meter, -(height) * Constants.Meter, width * Constants.Meter/2, height * Constants.Meter/2);
//			GameApplet.getBuffer().rect(-width * Constants.Meter/2, -height * Constants.Meter/2, width * Constants.Meter, height * Constants.Meter);
//			
		} else {
			GameApplet.getBuffer().fill(255, 0, 255);
//			GameApplet.getBuffer().rect(-(width) * Constants.Meter, -(height) * Constants.Meter, width * Constants.Meter/2, height * Constants.Meter/2);
			GameApplet.getBuffer().rect(-width * Constants.Meter/2, -height * Constants.Meter/2, width * Constants.Meter, height * Constants.Meter);
		}
		GameApplet.getBuffer().popMatrix();
	}
			
}
