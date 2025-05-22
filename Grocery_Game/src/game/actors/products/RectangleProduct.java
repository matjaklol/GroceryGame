package game.actors.products;
import game.util.TestScene;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;

import ddf.minim.AudioSample;
import ddf.minim.ugens.*;
import game.core.Game;
import game.core.objects.GameObject;
import game.util.CollisionService;
import game.util.Constants;


/**
 * 
 * @author keyboard
 *
 */
public class RectangleProduct extends Product {
	
	private float width, height;
	private float displayWidth, displayHeight;
	private Vector2 vel, preVel;
	private CollisionService collision;
	private World<Body> world;
	
	public RectangleProduct(double x, double y, double width, double height) {
		this(x, y, width, height, Game.get().getCurrentScene().getWorld());
	}

	public RectangleProduct(double x, double y, double width, double height, World<Body> world) {
		this.width = Constants.toPixels(width);
		this.height = Constants.toPixels(height);
		this.displayWidth = -Constants.toPixels(width/2.0d);
		this.displayHeight = -Constants.toPixels(height/2.0d);
		
		this.setWorld(world);
		
		this.fixture = new BodyFixture(Geometry.createRectangle(width, height));
		this.body = new Body();
		this.body.addFixture(fixture);
		
		this.body.translate(x, y);
		this.body.setMass(MassType.NORMAL);
		this.vel = body.getLinearVelocity();
		
		collision = new CollisionService(this.body, "collide", this);
		
		world.addBody(body);
		world.addContactListener(collision);
		this.setImage(Game.getImageService().emergencyGraphic());
	}
	
	public void setWorld(World<Body> world) {
		this.world = world;
	}
	
	private double prevStrength = 0d;
	private double timeSinceLast = 0d;
	public void collide(Body other, double strength) {
		TestScene myScene = (TestScene) Game.get().getCurrentScene();
		double magChange = this.preVel.getMagnitude() - this.body.getLinearVelocity().getMagnitude();
		if(strength > 0.04 && magChange >= 1.0d) {
			timeSinceLast = 0d;
//			System.out.println(!(strength + 0.1d >= prevStrength && strength -0.1d <= prevStrength));
//			prevStrength = strength;
			
//			TestScene myScene = (TestScene) Game.get().getCurrentScene();
//			System.out.println(myScene.player.hasControl(AudioSample.GAIN));
			float volume = (float) Math.min(0.5f, ((strength-0.03)/0.5d));
			
			float gain = 30f * (float) Math.log10(volume);
//			System.out.println("VOLUME: "+volume+", GAIN: "+gain);
			
			float pan = (float)((this.body.getTransform().getTranslationX() - 1.25d)/2.0d);
			myScene.player.setPan(pan);
			myScene.player.setGain(gain);
//			myScene.player.shiftGain(myScene.player.getGain(), volume, 0);
//			myScene.player.setVolume();
			myScene.player.trigger();
			
		}
//		
//		if(magChange >= 2.0d) {
//			TestScene myScene = (TestScene) Game.get().getCurrentScene();
////		System.out.println(myScene.player.hasControl(AudioSample.GAIN));
//			float volume = (float) Math.min(0.5f, (strength/0.5d));
//		
//			float gain = 30f * (float) Math.log10(volume);
//			System.out.println("VOLUME: "+volume+", GAIN: "+gain);
//			myScene.player.setGain(gain);
////		myScene.player.shiftGain(myScene.player.getGain(), volume, 0);
////		myScene.player.setVolume();
//			myScene.player.trigger();
//		}
	}
	
	
	
	public void update() {
		this.preVel = body.getLinearVelocity().copy();
		timeSinceLast += Game.get().delta;
	}
	
	
	public void clean() {
		world.removeBody(this.body);
		collision.clean();
		world.removeContactListener(collision);
		
		body.removeAllFixtures();
		this.collision = null;
		this.image = null;
		this.buffer = null;
	}
	
	public void draw() {
		
		this.buffer.pushMatrix();
		GameObject.applyTransform(buffer, body.getTransform());
		this.buffer.image(this.image, displayWidth, displayHeight, width, height);
		
		this.buffer.popMatrix();
	}
	
	public void post() {
		this.vel = body.getLinearVelocity().copy();
	}
	
	
	
}
