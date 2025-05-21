package game.util;
import org.dyn4j.world.listener.ContactListener;

import game.core.GameApplet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.contact.Contact;
import org.dyn4j.dynamics.contact.SolvedContact;
import org.dyn4j.world.listener.ContactListener;
import org.dyn4j.world.ContactCollisionData;
import org.dyn4j.world.PhysicsWorld;
//import org.dyn4j.world.contact.Contact;
//import org.dyn4j.world.contact.ContactCollisionData;
public class CollisionService implements ContactListener<Body> {
	private Body targetBody;
	private Method method;
	private Object toInvoke;
	
	public CollisionService(Body targetBody, String methodName, Object toInvoke) {
//		System.out.println("Creating Collision Service...");
		this.targetBody = targetBody;
		this.toInvoke = toInvoke;
		Class<?> c = toInvoke.getClass();
		
		try {
			method = c.getMethod(methodName, Body.class);
		} catch(Exception e) {
			e.printStackTrace();
			GameApplet.getInstance().die("[CollisionService] Error in CollisionService Constructor Method: "+e);
			return;
		}
		
	}
	

	@Override
	public void begin(ContactCollisionData<Body> collision, Contact contact) {
		
		
	}
	
	private void onCollision(Body other) {
		try {
			method.invoke(toInvoke, other);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			GameApplet.getInstance().die("[CollisionService] Error in CollisionService.onCollision(): "+e);
			return;
		}
	}
	
	public void clean() {
		this.method = null;
		this.toInvoke = null;
	}

	@Override
	public void persist(ContactCollisionData<Body> collision, Contact oldContact, Contact newContact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end(ContactCollisionData<Body> collision, Contact contact) {
		// TODO Auto-generated method stub
		
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
		if(collision.getBody1().equals(targetBody) || collision.getBody2().equals(targetBody)) {
			Body other = (collision.getBody1() == targetBody) ? collision.getBody2() : collision.getBody1();
			onCollision(other);
		}
		
		
	}

}
