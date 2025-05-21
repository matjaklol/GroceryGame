package game.util;

import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.PinJoint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import ddf.minim.AudioPlayer;
import ddf.minim.AudioSample;
import ddf.minim.Minim;
import game.core.Game;
import game.core.GameApplet;
import game.core.objects.GameObject;
import game.core.objects.Rectangle;
import game.management.Scene;
import game.actors.GroceryBag;
import game.actors.products.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class TestScene extends Scene {
//	Rectangle testObject;
	Body mouseBody;
	PinJoint<Body> joint;
	GroceryBag bag;
	CircleProduct teto;
	private SceneFade fadeIn;
	
	private Minim minim;
	public AudioSample player;
	public TestScene(Game game) {
		super(game);
		GameApplet.getInstance().registerMethod("keyEvent", this);
		Game.getMouse().registerMethod("mouseEvent", this);
		applet.randomSeed(0);
		this.minim = Game.getMinim();
		
		player = minim.loadSample("sounds/squish.wav", 512);
//		player.loop();
//		player.play();
		
		fadeIn = new SceneFade(game);
		fadeIn.fadeIn(0.5f);
		
		teto = new CircleProduct(1, -1, 0.15, 0.15);
		teto.setImage(Game.getImageService().getImage("tetofumo.png"));
		addGameObject(teto);
		world.addBody(teto.getBody());
		bag = new GroceryBag(1f, 1f);
//		addGameObject(bag);
		world.addBody(bag.getBody());
//		fadeIn.setFadeColor(GameApplet.getInstance().color(255, 0, 0));
		
//		world.getSettings().setContinuousDetectionMode(ContinuousDetectionMode.ALL);
		world.getSettings().setStepFrequency(1.0/120.0);
		Rectangle floor = new Rectangle(1, 1.4f, 5, 0.3f);
		
		
		
		
		floor.getBody().setMass(MassType.INFINITE);
		floor.getBody().setGravityScale(0);
		this.world.addBody(floor.getBody());
		this.gameObjects.add(floor);
		
		for(int i = 0; i < 10; i++) {
			RectangleProduct box = new RectangleProduct(applet.random(0, 2.5f), applet.random(-5, 0), 0.15f, 0.30f, world);
			
//			Rectangle box = new Rectangle(applet.random(0, 2.5f), applet.random(-5, 0), 0.15f, 0.30f);
			box.setImage(Game.getImageService().getImage("CerealBox.png"));
//			box.getBody().setAngularDamping(0.4d);
//			box.getBody().setLinearDamping(0.2d);
//			box.getBody().getFixture(0).setRestitution(0.1);
//			box.getBody().getFixture(0).setFriction(0.5);
//			box.getBody().setMass(MassType.NORMAL);
//			box.getBody().setAtRestDetectionEnabled(false);
			gameObjects.add(box);
//			this.world.addBody(box.getBody());
		}
		
		for(int i = 0; i < 0; i++) {
			float size = applet.random(0.15f, 0.3f);
			CircleProduct tetoDynamic = new CircleProduct(applet.random(0f, 2f), applet.random(-5, 0), applet.random(0.10f, 0.30f), applet.random(0.10f, 0.30f));
//			CircleProduct tetoDynamic = new CircleProduct(applet.random(0f, 2f), applet.random(-5, 0), size, size);
			int value = (int) Math.floor(applet.random(0, 2));
			if(value == 0) {
				tetoDynamic.setImage(Game.getImageService().getImage("tetofumo.png"));
				tetoDynamic.getFixture().setDensity(50);
				tetoDynamic.getBody().setMass(MassType.NORMAL);
			} else {
				tetoDynamic.setImage(Game.getImageService().getImage("Orange.png"));
				tetoDynamic.getFixture().setDensity(30);
				tetoDynamic.getBody().setMass(MassType.NORMAL);
			}
			
			tetoDynamic.getBody().getTransform().setRotation(applet.random(-GameApplet.PI, GameApplet.PI));
			this.world.addBody(tetoDynamic.getBody());
			addGameObject(tetoDynamic);
		}
		
		
		
		
		
		this.world.setGravity(0, 9.8);
		this.addGameObject(fadeIn);
		System.out.println("Meter Width/Height: "+Constants.toMeters(640)+"m/"+Constants.toMeters(360)+"m.");
	}
	
	
	public void keyEvent(KeyEvent event) {
		if(event.getAction() == KeyEvent.RELEASE && event.getKeyCode() == 32) { 
			player.trigger();
		}
	}
	
	public void mouseEvent(MouseEvent event) {
		if(event.getAction() == MouseEvent.PRESS) {
			if(event.getButton() == GameApplet.LEFT) {
				Vector2 worldMouse = new Vector2(game.worldMouseX, game.worldMouseY);
				
				for(Body b : world.getBodies()) {
					if(b.getMass().isInfinite()) {
						continue;
					}
					
					
					Fixture f = b.getFixture(worldMouse);
					if(f == null) {
						continue;
					}
					
					//Mass is finite, and it currently collides at some point. 
					if(joint != null) {
						world.removeJoint(joint);
					}
					
					
					b.setBullet(true);
					b.setAngularDamping(1.0);
					
//					joint = new PinJoint<Body>(b, b.getTransform().getTranslation());
					joint = new PinJoint<Body>(b, worldMouse);
					joint.setSpringFrequency(9.0);
					joint.setSpringDampingRatio(0.7);
					world.addJoint(joint);
					return;
				}
			}
		} else if(event.getAction() == MouseEvent.RELEASE) {
			if(event.getButton() == GameApplet.LEFT) {
				if(joint == null) {
					return;
				}
				
				world.removeJoint(joint);
				joint.getBody().setBullet(false);
				joint.getBody().setAngularDamping(0.4d);
				
				joint = null;
			}
		}
	}
	
	public void update() {
		
		if(joint != null) {
			joint.setTarget(new Vector2(game.worldMouseX, game.worldMouseY));
		}
		
		bag.update();
		
		for(GameObject object : this.gameObjects) {
			object.update();
		}
		this.world.update(game.physicsTimestep);
//		testObject.getBody().setLinearVelocity(0, 0);
//		testObject.getBody().getTransform().setTranslation();
	}
	public void draw() {
		GameApplet.getBuffer().noStroke();
		for(GameObject object : this.gameObjects) {
			object.draw();
		}
		bag.draw();
		GameApplet.getBuffer().pushMatrix();
		GameApplet.getBuffer().translate(0, 0, 1);
		GameApplet.getBuffer().stroke(0);
		GameApplet.getBuffer().fill(255, 0, 0);
		GameApplet.getBuffer().strokeWeight(15);
		GameApplet.getBuffer().stroke(255, 0, 0);
		GameApplet.getBuffer().line(game.bufferMouseX, game.bufferMouseY, game.pBufferMouseX, game.pBufferMouseY);
		GameApplet.getBuffer().noStroke();
		GameApplet.getBuffer().ellipse(Constants.toPixels(game.worldMouseX), Constants.toPixels(game.worldMouseY), Constants.Cm * 5, Constants.Cm * 5);
		GameApplet.getBuffer().strokeWeight(1);
		if(joint != null) {
			GameApplet.getBuffer().stroke(0);
			GameApplet.getBuffer().line(game.bufferMouseX, game.bufferMouseY, Constants.toPixels(joint.getAnchor().x), Constants.toPixels(joint.getAnchor().y));
		}
		
		GameApplet.getBuffer().popMatrix();
		
	}

}
