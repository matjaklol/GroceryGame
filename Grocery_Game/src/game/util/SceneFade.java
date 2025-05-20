package game.util;

import game.core.Game;
import game.core.GameApplet;
import game.core.objects.GameObject;
import processing.core.PGraphics;

/**
 * Helper class for rendering fade ins/outs for a given Buffer.
 * Can fade in/out over a duration of time, and with a given color. 
 * Call {@linkplain SceneFade#fadeEnded()} to determine if a fade is over. 
 * @author keyboard
 *
 */
public class SceneFade extends GameObject {
	private float fadeValue = 0.0f;
	private float fadeDelta = 0.0f;
	private float fadeTimer = 0.0f;
	
	private int color = 0;
	private boolean fadeEnded = false;
	private boolean fadingIn = false;
	private Game game;
	private PGraphics buffer;
	
	/**
	 * Call either {@linkplain SceneFade#fadeIn(float)} or {@linkplain SceneFade#fadeOut(float)} after initializing.
	 * @param game the primary game object used for timing logic.
	 */
	public SceneFade(Game game) {
		this.game = game;
		this.buffer = GameApplet.getBuffer();
		this.color = GameApplet.getInstance().color(0, 0, 0);
	}
	
	/**
	 * Start fading to clear (black -> clear).
	 * 
	 * @param timer the length of the fade (in seconds).
	 */
	public void fadeIn(float timer) {
		fadeValue = 255f;
		
		fadeTimer = Math.abs(timer);
		fadeDelta = fadeTimer;
		
		fadingIn = true;
	}
	
	
	/**
	 * Start fading to black (clear -> black).
	 * @param timer the length of the fade (in seconds).
	 */
	public void fadeOut(float timer) {
		fadeValue = 0f;
		fadeTimer = Math.abs(timer);
		fadeTimer = 0.0f;
		fadingIn = false;
	}
	
	/**
	 * Check if the fade is over yet.
	 * @return true if the fade has completed (can either have faded out or in).
	 */
	public boolean fadeEnded() {
		return fadeEnded;
	}
	
	/**
	 * Handles all fading in/out logic. To change the fade mode use either fade method.
	 * @see {@linkplain SceneFade#fadeIn(float)}
	 * @see {@linkplain SceneFade#fadeOut(float)}
	 */
	public void update() {
		if(fadingIn) {
			//255 -= 1?
			fadeDelta -= game.delta;
			if(fadeDelta <= 0) {
				//Fade finished.
				this.dead = true;
				fadeEnded = true;
			}
			this.fadeValue = (fadeDelta/fadeTimer) * 255f;
			return;
		}
		
		fadeDelta += game.delta;
		if(fadeDelta >= fadeTimer) {
			this.dead = true;
			fadeEnded = true;
		}
		this.fadeValue = (fadeDelta/fadeTimer) * 255f;
		return;
	}
	
	/**
	 * Set the base fade color. Pass a color value from the PApplet/GameApplet.
	 * @param color the color from {@linkplain GameApplet#color(int, int, int)}
	 */
	public void setFadeColor(int color) {
		this.color = color;
	}
	
	
	/**
	 * Set the PGraphics buffer to draw to. Useful if you have multiple scenes/custom
	 * rendering requirements.
	 * @param graphic the new graphic to render to.
	 */
	public void setGraphic(PGraphics graphic) {
		this.buffer = graphic;
		System.gc();
	}
	
	
	/**
	 * The generic draw method. Call this after calling {@linkplain SceneFade#update()}.
	 */
	public void draw() {
		buffer.noStroke();
		buffer.pushMatrix();
		buffer.translate(0, 0, 1);
		buffer.fill(this.color, this.fadeValue);
		buffer.rect(0, 0, buffer.width, buffer.height);
		buffer.popMatrix();
	}
	
	public void clean() {
		System.out.println("[SceneFade] Fade ended, clean() called.");
	}

}
