package game.util.audio;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.ContactCollisionData;
import org.dyn4j.world.World;

import ddf.minim.AudioSample;
import ddf.minim.Minim;
import game.core.Game;
import game.core.GameApplet;
import game.core.objects.GameObject;
import game.util.CollisionService;
public class PhysicsAudio extends GameObject {
	private Body body; 
	private World<Body> world;
	private AudioSample sample;
	private Minim minim; 
	
	private AudioSample friction;
	private AudioSample impact;
	
	private CollisionService collision;
	
	private boolean frictionPlaying = false;
	
	public PhysicsAudio(Body body, World<Body> world) {
		this.body = body;
		this.world = world;
		
		this.collision = new CollisionService(body);
//		collision.registerOnPersist("persist", this);
		collision.registerOnCollision("contact", this);
		collision.registerOnEnd("end", this);
		
		world.addContactListener(collision);
		
	}
	
	public void getDebugAudio() {
		friction = minim.loadSample("sounds/scrape.wav", 1028);
		impact = minim.loadSample("sounds/smack.wav", 512);
		System.out.println("F: "+friction.length()+", I: "+impact.length());
	}
	
	
	
	public void setMinim(Minim minim) {
		this.minim = minim;
	}
	
	public void setAudio(AudioSample sample) {
		this.sample = sample;
	}
	
	
	/**
	 * Trigger hit sound ONCE.
	 * @param other
	 * @param collision
	 */
	public void contact(Body other, ContactCollisionData<Body> collision) {
		impact.trigger();
	}
	
	
	private boolean frictionPlayerEnded;
	private double soundCounter = 0.0d;
	
	/**
	 * Trigger sliding/friction sound continiously. 
	 * @param other
	 * @param collision
	 */
	public void persist(Body other, double strength) {
//		if(strength <= 0.01) {
//			frictionPlayerEnded  = false;
//			soundCounter = 0.0d;
//			friction.stop();
//			return;
//		}
		Vector2 dif = other.getLinearVelocity().difference(this.body.getLinearVelocity());
		
//		System.out.println("STRENGTH: "+strength);
		
		float volume = (float) Math.min(0.2f, Math.abs(strength)/2d);
		if(volume <= 0.01f) {
			frictionPlayerEnded  = false;
			soundCounter = 0.0d;
			friction.stop();
			return;
		}
		
		float gain = 30f * (float) Math.log10(volume);
//		float gain = GameApplet.map(volume, 0.00f, 0.80f, -80.0f, 6.0206f);
		
		System.out.println("Strength: "+Math.abs(strength)+", Volume: "+volume+", Gain: "+gain);
		friction.setGain(gain);
		
		if(!frictionPlayerEnded) {
			println("playing");
			frictionPlayerEnded = true;
			
			friction.trigger();
		}
		
		
		
	}
	
	
	public void end(Body other, ContactCollisionData<Body> collision) {
		frictionPlayerEnded  = false;
		soundCounter = 0.0d;
		friction.stop();
		if(this.frictionPlaying) {
			frictionPlayerEnded  = false;
			soundCounter = 0.0d;
			friction.stop();
		}
	}
	
	private void println(Object o) {
		GameApplet.println(o);
	}
	
	private double playTime = 0.0d;
	public void update() {
		
		if(frictionPlayerEnded) {
			soundCounter+=Game.get().delta;
			if(soundCounter >= 1.5d) {
				println("Can play again");
				soundCounter = 0;
				frictionPlayerEnded = false;
			}
		}
		
	}
	
	
	public void clean() {
		if(this.world.containsBody(body)) {
			this.world.removeBody(body);
		}
		
		this.world.removeContactListener(collision);
		this.collision.clean();
		this.collision = null;
		
		this.world = null;
		this.body = null;
		this.sample = null;
		this.minim = null;
	}

}
