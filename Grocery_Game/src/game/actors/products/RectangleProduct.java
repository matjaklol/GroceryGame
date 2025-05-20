package game.actors.products;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;

import game.core.Game;
import game.core.objects.GameObject;
import game.util.Constants;

/**
 * 
 * @author keyboard
 *
 */
public class RectangleProduct extends Product {
	
	private float width, height;
	private float displayWidth, displayHeight;
	public RectangleProduct(double x, double y, double width, double height) {
		this.width = Constants.toPixels(width);
		this.height = Constants.toPixels(height);
		this.displayWidth = -Constants.toPixels(width/2.0d);
		this.displayHeight = -Constants.toPixels(height/2.0d);
		
		
		this.fixture = new BodyFixture(Geometry.createRectangle(width, height));
		this.body = new Body();
		this.body.addFixture(fixture);
		
		this.body.translate(x, y);
		
		this.setImage(Game.getImageService().emergencyGraphic());
	}
	
	public void draw() {
		this.buffer.pushMatrix();
		GameObject.applyTransform(buffer, body.getTransform());
		this.buffer.image(this.image, displayWidth, displayHeight, width, height);
		
		this.buffer.popMatrix();
	}
	
	
	
}
