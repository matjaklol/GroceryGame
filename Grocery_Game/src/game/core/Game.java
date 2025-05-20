package game.core;

import java.util.ArrayList;

import game.management.LogoScreen;
import game.management.Scene;
import game.userinput.Mouse;
import game.util.Constants;
import game.util.ImageService;
import processing.event.MouseEvent;

/**
 * This class handles the host game logic.
 * Here you can access user input, file loading, and physics info.
 * <p>This object runs by updating 1 {@linkplain Scene} at a time, using {@linkplain Game#setCurrentScene(Scene)}.</p>
 * <p>If you want to access inputs use {@linkplain Game#getImageService()}, {@linkplain Game#getMouse()}, etc.</p>
 * @author keyboard
 * @see Scene
 */
public class Game {
	/**
	 * The universal game instance.
	 */
	private static Game instance;
	
	/**
	 * The position of the user's mouse position in world coordinates. <p><b>DOES NOT ACCOUNT FOR DEPTH.</b></p>
	 */
	public double worldMouseX, worldMouseY;
	
	
	/**
	 * The position of the user's mouse coordinates in the rendering plane (in pixels).
	 */
	public int bufferMouseX, bufferMouseY, pBufferMouseX = 0, pBufferMouseY = 0;
	
	/**
	 * General image service object.
	 */
	private static ImageService imageService;
	
	/**
	 * General mouse input handler.
	 */
	private static Mouse mouse;
	
	/**
	 * Private reference to the GameApplet.
	 */
	private GameApplet applet;
	
	
	/**
	 * Currently unused list of scenes.
	 */
	private ArrayList<Scene> scenes = new ArrayList<Scene>();
	
	
	/**
	 * The current scene being utilized. Could be a menu, could be a game world.
	 */
	private Scene currentScene = null;
	
	
	//The time of the previous frame.
	private long previousTime = System.nanoTime();
	/**
	 * The % of the way we are between now and the next physics update. Value of 0-1.
	 */
	public double alpha = 0.0;
	
	/**
	 * We use this value to find the alpha and how many delta steps we need to do.
	 */
	private double accumulator = 0.0;
	
	/**
	 * The fixed timestep to use for physics calculations.
	 * <p>physicsTimestep * 5 = 5 seconds.</p>
	 */
	public final double physicsTimestep = 1.0d / 60.0d;
	
	/**
	 * The fixed timestep used for physics calculations. 
	 * <p>Delta * 5 = 5 seconds.</p>
	 */
	public final double delta = physicsTimestep;
	
	
	@Deprecated
	/**
	 * Use {@linkplain Game#delta} in your update method.
	 */
	public double oldDelta = 0;
	
	
	/**
	 * Creates the primary game object. This object calculates the delta, handles inputs, and manages all scenes.
	 * Call {@link Game#setCurrentScene(Scene)} to set the current "frame" / scene.
	 * @param applet
	 */
	public Game(GameApplet applet) {
		//Link the main applet, and instantiate all game utilies.
		this.applet = applet;
		instance = this;
		imageService = new ImageService(applet);
		mouse = new Mouse(applet);
		
		
		//Register primary methods to this object (for a cleaner GameApplet class itself).
		applet.registerMethod("post", this);
		applet.registerMethod("draw", this);
		applet.registerMethod("pre", this);
		applet.registerMethod("mouseEvent", this);
		
		//Set "pretty" window title.
		applet.getSurface().setTitle("Grocery Game v0.0.10");
		
		//Attempt preload of larger iomages (this barely works lmao).
		imageService.getImage("Grocery Bag FRONT.png");
		imageService.getImage("Grocery Bag BACK.png");
		
		//Calculate mouse information.
		calculateMouse();
		
		
		//Start the game by initiating with the intro black logo screen(s).
		this.setCurrentScene(new LogoScreen(this));
	}
	
	/**
	 * Basic util method for printing stuff.
	 * @param toPrint the information being printed.
	 */
	public void println(Object toPrint) {
		System.out.println("[GAME] " + toPrint);
	}
	
	
	/**
	 * Returns the current Game instance. Useful for external references.
	 * @return instance the current Game object.
	 */
	public static Game getInstance() {
		return instance;
	}
	
	/**
	 * Returns the current Game instance. Useful for external references.
	 * @return instance the current Game object.
	 */
	public static Game get() {
		return instance;
	}
	
	/**
	 * Returns a reference to the image service (useful for GameObject classes).
	 * @return imageService the currently instantiated image service.
	 */
	public static ImageService getImageService() {
		return imageService;
	}
	
	/**
	 * Returns a reference to the mouse event service.
	 * @return mouse the current Mouse object.
	 * @see Mouse
	 */
	public static Mouse getMouse() {
		return mouse;
	}
	
	/**
	 * Called before draw(), useful for calculating physics updates etc.
	 */
	public void pre() {
		this.update();
	}
	
	/**
	 * Recalculates where the mouse should be relative to the render buffer and the world itself.
	 */
	private void calculateMouse() {
		bufferMouseX = (int) ((applet.mouseX - GameApplet.offsetX) / GameApplet.bufferScaleFactor);
		bufferMouseY = (int) ((applet.mouseY - GameApplet.offsetY) / GameApplet.bufferScaleFactor);
		worldMouseX = Constants.toMeters(bufferMouseX);
		worldMouseY = Constants.toMeters(bufferMouseY);
	}
	
	/**
	 * Used to update mouse information. Minor performance improvement.
	 */
	public void mouseEvent(MouseEvent event) {
		calculateMouse();
	}
	
	
	/**
	 * Handles all physics updates, also calls {@link currentScene#update()}
	 */
	private void update() {
		//Update to the current time.
		long currentTime = System.nanoTime();
		//Get the time in seconds.
		double frameTime = (currentTime - previousTime) / 1_000_000_000.0;
		//Clamp the frametime to a minimum of 250ms in order to avoid spiral of death.
		frameTime = Math.min(frameTime, 0.25d);
		//Unused, variable deltatime.
		oldDelta = frameTime;
		
		//After clamping frameTime, reset the time measurement until next update().
		previousTime = currentTime;
		
		//Add total time to the accumulator.
		accumulator += frameTime;
		
		//If the current scene doesn't exist don't even bother with updating jack. 
		//TODO: Optimize this to actually just ignore even calculating the delta.
		if(currentScene == null) {
			accumulator = 0;
			return;
		}
		
		//Increment backwards by our base timestep (delta). 
		while(accumulator >= physicsTimestep) {
			currentScene.update();
			accumulator -= physicsTimestep;
		}
		
		//What's left over will be our alpha, so go ahead and save that.
		alpha = accumulator / physicsTimestep;
	}
	
	
	/**
	 * The generic draw method. Just sets the background to white and calls {@linkplain currentScene#draw()}.
	 */
	public void draw() {
		GameApplet.getBuffer().beginDraw();
		GameApplet.getBuffer().background(255,255,255);
		if(currentScene != null) {
			currentScene.draw();
		}
		GameApplet.getBuffer().endDraw();
	}
	
	
	/**
	 * After the render pass is finished we can call post(). Updates some minor things, especially with 
	 * {@linkplain ImageService#postDraw()}.
	 */
	public void post() {
		if(currentScene != null) {
			currentScene.post();
		}
		
		this.pBufferMouseX = bufferMouseX;
		this.pBufferMouseY = bufferMouseY;
	}
	
	
	/**
	 * Switches out the current scene for a new one.
	 * <p><b>ONLY CALL THIS WHEN YOU'RE SURE YOU'RE READY FOR A SCENE CHANGE</b></p>
	 * @param newScene the new scene we will be rendering/updating.
	 */
	public void setCurrentScene(Scene newScene) {
		if(this.currentScene != null) {
			//Send the scene change event to the old scene.
			this.currentScene.sceneChange();
		}
		this.currentScene = newScene;
	}
	
	
}
