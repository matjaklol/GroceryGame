package game.util;
import org.dyn4j.world.listener.ContactListener;

import java.util.ArrayList;
import game.core.GameApplet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.contact.Contact;
import org.dyn4j.dynamics.contact.SolvedContact;
import org.dyn4j.geometry.Vector2;
//import org.dyn4j.world.listener.ContactListener;
import org.dyn4j.world.ContactCollisionData;
//import org.dyn4j.world.PhysicsWorld;
//import org.dyn4j.world.contact.Contact;
//import org.dyn4j.world.contact.ContactCollisionData;
public class CollisionService implements ContactListener<Body> {
	private Body targetBody;
	private Method collisionMethod;
	private Method persistMethod;
	private Method endMethod;
	
	private Object collisionInvoke;
	private Object persistInvoke;
	private Object endInvoke;
	
	private ArrayList<Body> touchingBodies = new ArrayList<Body>();
	private double min = 0.01d;
	private SolvedContact contact;
	private boolean persist = false;
	
	public CollisionService(Body targetBody, String methodName, Object toInvoke) {
//		System.out.println("Creating Collision Service...");
		this.targetBody = targetBody;
		this.registerOnCollision(methodName, toInvoke);
	}
	
	public CollisionService(Body targetBody) {
		this.targetBody = targetBody;
	}
	
	
	/**
	 * <p>NOTE: Your method should be in this format:</p>
	 * <h1><u>method(Body other, double relativeStrength)</u></h1>
	 * <p>Registers the given object to receive events in the given method name.</p>
	 * 
	 * @param methodName the name of the method you would like to subscribe.
	 * @param toInvoke the specific object you're subscribing.
	 */
	public void registerOnPersist(String methodName, Object toInvoke) {
		persistInvoke = toInvoke;
		Class<?> c = toInvoke.getClass();
		try {
			persistMethod = c.getMethod(methodName, Body.class, double.class);
		} catch(Exception e) {
			e.printStackTrace();
			GameApplet.getInstance().die("[CollisionService] Error in CollisionService registerOnPersist(): "+e);
			return;
		}
	}
	
	public void registerOnCollision(String methodName, Object toInvoke) {
		collisionInvoke = toInvoke;
		Class<?> c = toInvoke.getClass();
		try {
			collisionMethod = c.getMethod(methodName, Body.class, ContactCollisionData.class);
		} catch(Exception e) {
			e.printStackTrace();
			GameApplet.getInstance().die("[CollisionService] Error in CollisionService registerOnCollision(): "+e);
			return;
		}
	}
	
	
	public void registerOnEnd(String methodName, Object toInvoke) {
		endInvoke = toInvoke;
		Class<?> c = toInvoke.getClass();
		try {
			endMethod = c.getMethod(methodName, Body.class, ContactCollisionData.class);
		} catch(Exception e) {
			e.printStackTrace();
			GameApplet.getInstance().die("[CollisionService] Error in CollisionService registerOnEnd(): "+e);
			return;
		}
	}

	@Override
	public void begin(ContactCollisionData<Body> collision, Contact contact) {
		
		
	}
	
	private void onCollision(Body other, ContactCollisionData<Body> info, SolvedContact contact) {
		try {
			collisionMethod.invoke(collisionInvoke, other, info);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			GameApplet.getInstance().die("[CollisionService] Error in CollisionService.onCollision(): "+e);
			return;
		}
	}
	
	private void onPersist(Body other, ContactCollisionData<Body> info, Contact contact) {
		try {
			double strength = getRelativeVelocity(other, this.targetBody).getMagnitude();
			persistMethod.invoke(persistInvoke, other, strength);
		} catch(Exception e) {
			e.printStackTrace();
			GameApplet.getInstance().die("[CollisionService] Error in CollisionService.onPersist(): "+e);
			return;
		}
	}
	
	private void onEnd(Body other, ContactCollisionData<Body> info, Contact contact) {
		try {
			endMethod.invoke(endInvoke, other, info);
		} catch(Exception e) {
			e.printStackTrace();
			GameApplet.getInstance().die("[CollisionService] Error in CollisionService.onEnd(): "+e);
			return;
		}
	}
	
	private Vector2 getRelativeVelocity(ContactCollisionData<Body> info, Contact contact) {
		return getRelativeVelocity(info.getBody1(), info.getBody2(), contact);
	}
	
	private Vector2 getRelativeVelocity(Body body1, Body body2) {
		Vector2 velA = body1.getLinearVelocity();
		Vector2 velB = body2.getLinearVelocity();
		
		return velA.difference(velB);
	}
	
	private Vector2 getRelativeVelocity(Body body1, Body body2, Contact contact) {
		Vector2 contactPoint = contact.getPoint();
		Vector2 velA = body1.getLinearVelocity(contactPoint);
		Vector2 velB = body2.getLinearVelocity(contactPoint);
		
		return velA.difference(velB);
	}
	
	private double getRelativeTangentialSpeed(ContactCollisionData<Body> info, Contact contact) {
		return getRelativeTangentialSpeed(info.getBody1(), info.getBody2(), contact);
	}
	
	private double getRelativeTangentialSpeed(Body body1, Body body2, Contact contact) {
		Vector2 relativeVelocity = getRelativeVelocity(body1, body2, contact);
		Vector2 contactPoint = contact.getPoint().getNormalized();
		
		
		Vector2 tangent = contactPoint.getLeftHandOrthogonalVector();
		
		return relativeVelocity.dot(tangent);
		
	}
	
	public void clean() {
		this.collisionMethod = null;
		this.persistMethod = null;
		
		this.collisionInvoke = null;
		this.persistInvoke = null;
		
		
		this.touchingBodies.clear();
	}

	@Override
	public void persist(ContactCollisionData<Body> collision, Contact oldContact, Contact newContact) {
		if(this.persistInvoke == null || this.persistMethod == null) {
			return;
		}
		if(collision.getBody1() == targetBody || collision.getBody2() == targetBody) {
			if(collision.getFixture1().isSensor() || collision.getFixture2().isSensor()) {
				return;
			}
			Body other = collision.getBody1() == targetBody ? collision.getBody2() : collision.getBody1();
			if(!touchingBodies.contains(other)) {
				touchingBodies.add(other);
			}
			onPersist(other, collision, newContact);
		}
		
		
	}

	@Override
	public void end(ContactCollisionData<Body> collision, Contact contact) {
		if(this.endInvoke == null || this.endMethod == null) {
			return;
		}
		if((collision.getBody1() == targetBody || collision.getBody2() == targetBody)) {
			if(collision.getFixture1().isSensor() || collision.getFixture2().isSensor()) {
				return;
			}
			Body other = collision.getBody1() == targetBody ? collision.getBody2() : collision.getBody1();
			onEnd(other, collision, contact);
			if(touchingBodies.contains(other)) {
				touchingBodies.remove(other);
			}
		}
		
	}

	@Override
	public void destroyed(ContactCollisionData<Body> collision, Contact contact) {
		if(collision.getBody1() == targetBody || collision.getBody2() == targetBody) {
			this.clean();
		}
		
	}

	@Override
	public void collision(ContactCollisionData<Body> collision) {
		
		
	}

	@Override
	public void preSolve(ContactCollisionData<Body> collision, Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(ContactCollisionData<Body> collision, SolvedContact contact) {
		if(this.collisionInvoke == null || this.collisionMethod == null) {
			return;
		}
		if((Math.abs(contact.getNormalImpulse()) > min) && (collision.getBody1().equals(targetBody) || collision.getBody2().equals(targetBody))) {
			if(collision.getFixture1().isSensor() || collision.getFixture2().isSensor()) {
				return;
			}
			Body other = (collision.getBody1() == targetBody) ? collision.getBody2() : collision.getBody1();
			
			
			if(touchingBodies.contains(other)) {
				return;
			}
			
			onCollision(other, collision, contact);
			touchingBodies.add(other);
		}
		
		
	}

}
