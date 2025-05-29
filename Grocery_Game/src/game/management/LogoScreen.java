package game.management;

import ddf.minim.AudioPlayer;
import ddf.minim.AudioSample;
import game.core.Game;
import game.core.GameApplet;
import game.util.TestScene;
import processing.core.PImage;
import processing.event.MouseEvent;

public class LogoScreen extends Scene {
	//182x50
	private PImage keyboardLogo;
	private AudioPlayer sample;
	
	private float timer = 0;
	private boolean shownLogo = false;
	private boolean fadeOut = false;
	public LogoScreen(Game game) {
		super(game);
		keyboardLogo = Game.getImageService().getImage("keyboard game logo.png");
		sample = Game.getMinim().loadFile("sounds/intro/chord2.wav");
		GameApplet.getBuffer().imageMode(GameApplet.CENTER);
		Game.getMouse().registerMethod("mouseEvent", this);
		
		float volume = 0.5f;
		
		float gain = 20f * (float) Math.log10(volume);
		sample.setGain(gain);
		sample.play();
	}
	
	public void mouseEvent(MouseEvent event) {
		if(event.getAction() == MouseEvent.CLICK) {
			this.timer += 5d;
			
			int newPosition = sample.position() + 5000;
			
			if(newPosition >= sample.length()) {
				//Can't play.
				sample.pause();
				return;
			}
			sample.cue(newPosition);
			sample.play();
		}
	}
	public void update() {
		//17.244s
		timer+=game.delta;
		//Fade in for 5 seconds
		if(timer >= 4.5 && !shownLogo) {
			//Fade in
			shownLogo = true;	
		}
		
		//Hold at full brightness for 3.5 seconds
		if(shownLogo == true && timer >= 8.5d) {
			//Fade out?
			fadeOut = true;
		}
		
		
		//Fade out for 5 seconds
		if(timer >= 13.5d) {
			//end
			Game.getMouse().unregisterMethod("mouseEvent", this);
			sample.pause();
			sample.close();
			sample = null;
			
			game.setCurrentScene(new TestScene(game));
			GameApplet.getBuffer().imageMode(GameApplet.CORNER);
			GameApplet.getBuffer().noTint();
			
			keyboardLogo = null;
			
			System.gc();
		}
	}
	
	
	public void draw() {
		GameApplet.getBuffer().background(0);
		if(fadeOut) {
			GameApplet.getBuffer().tint(255, 255, 255, (float) GameApplet.map(timer, 8.5f, 13.5f, 255f, 0f));
		} else {
			GameApplet.getBuffer().tint(255, 255, 255, (float) GameApplet.map(timer, 0f, 4.5f, 0f, 255f));
		}
		
		GameApplet.getBuffer().image(keyboardLogo, 320, 180, 546, 150);
		
	}

}
