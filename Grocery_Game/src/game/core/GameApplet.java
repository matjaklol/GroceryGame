package game.core;


import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;

//import java.awt.Dimension;
//import java.awt.Frame;
//import java.util.ArrayList;

//import game.management.*;

public class GameApplet extends PApplet{
	private static GameApplet instance;
	public Game game;
	private static PGraphics gameBuffer;
	//640x360
	public static final float BUFFER_WIDTH = 640;
	public static final float BUFFER_HEIGHT = 360;
	public static float BUFFER_WIDTH_RATIO;
	public static float BUFFER_HEIGHT_RATIO;
	
//	private ArrayList<Scene> gameScenes = new ArrayList<Scene>();
	
	/**
	 * The primary applet object used for rendering/user input. 
	 * @see {@linkplain processing.core.PApplet}
	 * @param args
	 */
	public GameApplet(String[] args) {
		instance = this;
		
		println("[GameApplet] Instancing GameApplet.");
		String[] processingArgs = {"gameApplet"};
		GameApplet gameApplet = this;
		PApplet.runSketch(processingArgs, gameApplet);
		
		println("[GameApplet] Completed instancing.");
	}
	
	/**
	 * Returns the global instance of the GameApplet currently in use.
	 * @return instance the super GameApplet currently running.
	 */
	public static GameApplet getInstance() {
		return instance;
	}
	
	
	
	/**
	 * Returns the generic draw buffer for the GameApplet. You can make draw
	 * calls to this buffer during your object's draw() method. 
	 * 
	 * This buffer is NOT affected by camera changes, and is instead a generic 2d graphic.
	 * @return PGraphic gameBuffer the generic buffer drawn every frame.
	 */
	public static PGraphics getBuffer() {
		return gameBuffer;
	}
	
	public void settings() {
		this.size(1280, 720, P3D);
		
		w = width;
		h = height;
		GameApplet.BUFFER_WIDTH_RATIO = width/BUFFER_WIDTH;
		GameApplet.BUFFER_HEIGHT_RATIO = height/BUFFER_HEIGHT;
		
		this.noSmooth();
		
		
		
		
	}
	
	
	public void setup() {
		this.frameRate(1000);
//		Frame frame = (Frame) ((processing.awt.PSurfaceAWT.SmoothCanvas) surface.getNative()).getFrame();
//		frame.setMinimumSize(new Dimension(640, 360));
		
		bufferScaleFactor = min((float) width / BUFFER_WIDTH, (float) height / BUFFER_HEIGHT);
		int displayW = (int) (BUFFER_WIDTH * bufferScaleFactor);
		int displayH = (int) (BUFFER_HEIGHT * bufferScaleFactor);
		offsetX = (width - displayW) / 2;
		offsetY = (height - displayH) / 2;
		
		gameBuffer = generateBuffer();
		gameBuffer.hint(PGraphics.ENABLE_DEPTH_SORT);
		this.game = new Game(this);
		surface.setResizable(true);
		registerMethod("post", this);
		
		
		
	}
	
	
	public static int offsetX = 0;
	public static int offsetY = 0;
	public static float bufferScaleFactor = 0;
	public void draw() {
		background(0);
		bufferScaleFactor = min((float) width / BUFFER_WIDTH, (float) height / BUFFER_HEIGHT);
		int displayW = (int) (BUFFER_WIDTH * bufferScaleFactor);
		int displayH = (int) (BUFFER_HEIGHT * bufferScaleFactor);
		offsetX = (width - displayW) / 2;
		offsetY = (height - displayH) / 2;

		  // Use nearest-neighbor scaling
		imageMode(CORNER);
		noSmooth(); // ensure no smoothing when scaling

		image(gameBuffer, offsetX, offsetY, displayW, displayH);
//		image(gameBuffer, 0, 0, Math.max(BUFFER_WIDTH, width), Math.max(BUFFER_HEIGHT, height));
//		surface.setLocation(250 + floor(cos(frameCount/25) * 25), 250 + floor(sin(frameCount/25) * 50));
	}
	
	
	private int w, h;
	
	public void post() {
		if(w != width || h != height) {
			//Window was resized.
			w = width;
			h = height;
			
			GameApplet.BUFFER_WIDTH_RATIO = w/BUFFER_WIDTH;
			GameApplet.BUFFER_HEIGHT_RATIO = h/BUFFER_HEIGHT;
//			println("[GameApplet] WINDOW WAS RESIZED. "+GameApplet.BUFFER_WIDTH_RATIO+"w, "+GameApplet.BUFFER_HEIGHT_RATIO+"h.");
			
		}
	}
	
	/**
	 * Generates a buffer of default width and height, along with the proper values to keep
	 * the pixilation style effect.
	 * @return graphic the PGraphic object instanced.
	 */
	public PGraphics generateBuffer() {
		PGraphics graphic = createGraphics(ceil(BUFFER_WIDTH), ceil(BUFFER_HEIGHT), P3D);
		((PGraphicsOpenGL) graphic).textureSampling(2);
		graphic.rectMode(RADIUS);
		
		return graphic;
	}
	
	
}
