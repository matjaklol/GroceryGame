package game.management;

import game.core.Game;
import game.core.GameApplet;
import game.core.objects.GameObject;
import java.util.ArrayList;

import org.dyn4j.dynamics.Body;
import org.dyn4j.world.World;
public class Scene {
	protected GameApplet applet;
	protected Game game;
	protected World<Body> world;
	protected ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	
	public Scene(Game game) {
		this.game = game;
		this.applet = GameApplet.getInstance();
		this.world = new World<Body>();
	}
	
	public void update() {};
	
	public void draw() {};
	
	public void post() {
		cleanObjects();
	};
	
	/**
	 * Filters through all GameObjects to find which are alive and which are dead.
	 */
	protected void cleanObjects() {
		for(int i = gameObjects.size()-1; i >= 0; i--) {
			GameObject o = gameObjects.get(i);
			if(o.dead()) {
				o.clean();
				gameObjects.remove(i);
			}
		}
	}
	
	/**
	 * Called when this scene is being switched out for a new one. 
	 */
	public void sceneChange() {
		
	}
	
	public void addGameObject(GameObject o) {
		this.gameObjects.add(o);
	}
}
