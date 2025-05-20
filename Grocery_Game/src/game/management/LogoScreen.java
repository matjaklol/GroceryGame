package game.management;

import game.core.Game;
import game.core.GameApplet;
import game.util.TestScene;
import processing.core.PImage;
import processing.event.MouseEvent;

public class LogoScreen extends Scene {
	
	//182x50
	PImage keyboardLogo;
	
	private float timer = 0;
	private boolean shownLogo = false;
	private boolean fadeOut = false;
	public LogoScreen(Game game) {
		super(game);
		keyboardLogo = Game.getImageService().getImage("keyboard game logo.png");
		GameApplet.getBuffer().imageMode(GameApplet.CENTER);
		Game.getMouse().registerMethod("mouseEvent", this);
	}
	
	public void mouseEvent(MouseEvent event) {
		if(event.getAction() == MouseEvent.CLICK) {
			this.timer += 5;
		}
	}
	public void update() {
		timer+=game.delta;
		if(timer >= 5 && !shownLogo) {
			//Fade in
			shownLogo = true;	
		}
		
		if(shownLogo == true && timer >= 8) {
			//Fade out?
			fadeOut = true;
		}
		
		if(timer >= 13) {
			//end
			game.setCurrentScene(new TestScene(game));
			GameApplet.getBuffer().imageMode(GameApplet.CORNER);
			GameApplet.getBuffer().noTint();
			System.gc();
		}
	}
	
	
	public void draw() {
		GameApplet.getBuffer().background(0);
		if(fadeOut) {
			GameApplet.getBuffer().tint(255, 255, 255, (float) (((10.0 - timer)/1.0) * 255));
		} else {
			GameApplet.getBuffer().tint(255, 255, 255, (float) (((timer - 1)/1.0) * 255));
		}
		
		GameApplet.getBuffer().image(keyboardLogo, 320, 180, 546, 150);
		
	}

}
