package game.userinput;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import game.core.GameApplet;
import processing.event.MouseEvent;


/**
 * Handles all inputs related to the user's mouse. Good for GUI interactions, game game interactions, etc.
 * 
 * @author keyboard
 * @see Mouse#registerMethod(String, Object)
 * @see Mouse#unregisterMethod(String, Object)
 */
public class Mouse {
	private GameApplet applet;
	
	private static Mouse instance;
	
	
	private Map<String, MouseEventSubscriber> registerMap = new ConcurrentHashMap<>();
	
	
	/**
	 * Initializes the mouse handler.
	 * @param applet the applet/direct screen being used. Must be the main screen/applet.
	 */
	public Mouse(GameApplet applet) {
		println("Initializing...");
		this.applet = applet;
		
		applet.registerMethod("mouseEvent", this);
		println("Initialized successfully; Registered events to handler.");
		instance = this;
	}
	
	
	public static Mouse get() {
		return instance;
	}
	
	/**
	 * Hooks into the GameApplet in order to register and deal with events.
	 * @param event
	 */
	public void mouseEvent(MouseEvent event) {
		if(event.getAction() == MouseEvent.MOVE) {
			
			return;
		}
		
		for(MouseEventSubscriber ms : registerMap.values()) {
			ms.handle(event);
		}
	}
	
	/**
	 * Debug print method.
	 * @param val
	 */
	private void println(String val) {
		System.out.println("[MOUSE] "+val);
	}
	
	/**
	 * Register a method to receive any event calls that might be received. Good for custom objects.
	 * We clean out 
	 * @see {@linkplain MouseEvent}
	 * @param methodName the name of the method in your object.
	 * @param toRegister the specific object being registered.
	 */
	public void registerMethod(String methodName, Object target) {
		this.registerObject(methodName, target);
	}
	
	/**
	 * Register a method to receive any event calls that might be received. Good for custom objects.
	 * We clean out 
	 * @see {@linkplain MouseEvent}
	 * @param methodName the name of the method in your object.
	 * @param toRegister the specific object to register.
	 * @param getMovementEvents receive events related to the mouse changing in position. 
	 */
	public void registerMethod(String methodName, Object target, boolean getMovementEvents) {
		this.registerObject(methodName, target);
	}
	
	
	/**
	 * Private method for registering a specific object to the map.
	 * Checks to see if there's already a method of the same name in the class, if not don't worry but if so just register it.
	 * @param name the name of the method to be linked.
	 * @param object the specific object to link to the method.
	 */
	private void registerObject(String name, Object object) {
		/**
		 * The class of the object, to make sure that the method we're calling actually exists.
		 */
		Class<?> c = object.getClass();
		
		/**
		 * Make sure that when we attempt to find the method we don't instantly crash.
		 */
		try {
			Method method = c.getMethod(name, MouseEvent.class);
			
			//Find the method in the map.
			MouseEventSubscriber sub = registerMap.get(name);
			
			//If it's a new method just create a new datapoint for it.
			if(sub == null) {
				sub = new MouseEventSubscriber(name);
				registerMap.put(name, sub);
			}
			
			//Add the current object and method so that we can call it whenever we want.
			sub.add(method, object);
			
			
		} catch(NoSuchMethodException e) {
			println("There is no method: "+name+"() in the class: "+c.getName());
			applet.die("There is no method: "+name+"() in the class: "+c.getName(), e);
		} catch(Exception e) {
			println("Could not register: "+name+"() for "+object+", cname: "+c.getName());
			applet.die("Could not register: "+name+"() for "+object+", cname: "+c.getName(), e);
		}
	}
	
	
	
	/**
	 * Removes a registered method from the mouse handler.
	 * @param name the name of the method to be removed.
	 * @param target the target object to remove specifically.
	 */
	public void unregisterMethod(String name, Object target) {
		Class<?> c = target.getClass();
		
		try {
			if(!registerMap.containsKey(name)) {
				println("Method: "+name+"() in class: "+c.getName()+" was not found in registery.");
				return;
			}
			
			registerMap.get(name).remove(target);
		} catch(Exception e) {
			println("Could not unregister: "+name+"() for "+target+", cname: "+c.getName());
			applet.die("Could not unregister: "+name+"() for "+target+", cname: "+c.getName(), e);
		}
	}
	
	
	
}
