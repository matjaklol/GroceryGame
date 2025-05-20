package game.userinput;

import java.lang.reflect.Method;
import java.util.ArrayList;
import game.core.*;
import processing.event.MouseEvent;

/**
 * The data class used in the {@linkplain Mouse} class. 
 * If you need to subscribe a given object to mouse events please use {@linkplain Mouse#registerMethod(String, Object)}.
 * 
 * @author keyboard
 * @see Mouse
 */
public class MouseEventSubscriber {
	/**
	 * The name of the method subscribed.
	 */
	public String name;
	
	/**
	 * The list of individual methods used by each object (each object might be different so store each individual method).
	 */
	public ArrayList<Method> methods = new ArrayList<Method>();
	
	/**
	 * The list of each subscribed object.
	 */
	private ArrayList<Object> objects = new ArrayList<Object>();
	
	/**
	 * @deprecated Please use {@linkplain MouseEventSubscriber#MouseEventSubscriber(String)}
	 * 
	 * Used to both create and subscribe a given object.
	 * @param methodName the name of the method that this subscriber represents
	 * @param method the specific method of the object to register
	 * @param parent the specific object to subscribe
	 */
	public MouseEventSubscriber(String methodName, Method method, Object parent) {
		this.name = methodName;
		this.methods.add(method);
		this.objects.add(parent);
	}
	
	
	/**
	 * Constructor used by the {@linkplain Mouse} class to store a given method and all objects subscribed to that specific call.
	 * <p>
	 * Use {@linkplain MouseEventSubscriber#add(Method, Object)} to add a named method of the same name.
	 * @param methodName the name of the method that this subscriber represents.
	 */
	public MouseEventSubscriber(String methodName) {
		this.name = methodName;
	}
	
	/**
	 * Adds a given object and method to the list of objects/methods being used. 
	 * Do NOT directly call this method, instead call {@linkplain Mouse#registerMethod(String, Object)} instead.
	 * @param method the actual method to be linked
	 * @param object the specific object to call at
	 */
	public void add(Method method, Object object) {
		if(this.objects.contains(object)) {
			GameApplet.getInstance().die("Object/Method already being stored. "+this.name+"() "+object+", "+method);
			return;
		}
		
		this.methods.add(method);
		this.objects.add(object);
	}
	
	
	/**
	 * Removes a given object/method pair, without needing to directly get the method.
	 * @param o the object to remove from the method call list.
	 */
	public void remove(Object o) {
		int index = findIndex(o);
		if(index == -1) {
			GameApplet.getInstance().die("Object not found.");
			return;
		}
		
		this.methods.remove(index);
		this.objects.remove(index);
	}
	
	
	/**
	 * Finds the index of a given object/method pair.
	 * <p>
	 * @param o the object to find the index of.
	 * @return an integer index value, or -1 if the index does not exist.
	 */
	private int findIndex(Object o) {
		for(int i = 0; i < objects.size(); i++) {
			
			//In case object.equals() has been overridden use == to ensure it's the same object.
			if(objects.get(i) == o) {
				return i;
			}
		}
		
		return -1;
	}
	
	
	/**
	 * Upon a mouse event, invoke each subscribed method, letting them know of the specific
	 * event information allowing them to deal with it how they need.
	 * @param arg the {@link MouseEvent} event being passed.
	 */
	public void handle(MouseEvent arg) {
		//For each object, try and invoke the linked method.
		for(int i = 0; i < objects.size(); i++) {
			
			//IF ANY ERROR OCCURS WE MUST TERMINATE. THERE IS NO EXCEPTION HANDLING.
			try {
				methods.get(i).invoke(objects.get(i), arg);
			} catch(Exception e) {
				e.printStackTrace();
				GameApplet.getInstance().die("[MOUSE] Error in MouseEventSubscriber: "+e);
				return;
			}
			
		}
	}
	
}
