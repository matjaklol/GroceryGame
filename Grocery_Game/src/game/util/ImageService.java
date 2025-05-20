package game.util;

import java.io.File;
import java.util.*;

import game.core.GameApplet;
import processing.core.*;
/**
 * Handles the loading of images for the game world (buildings, entities, etc.)
 * @author keyboard
 *
 */
public class ImageService {
	//Our collection of maps for each image type. (Shape, PImage, and PGraphic).
	private HashMap<String, PImage> imageMap = new HashMap<String, PImage>();
	private HashMap<String, PShape> shapeMap = new HashMap<String, PShape>();
	private HashMap<String, PGraphics> graphicMap = new HashMap<String, PGraphics>();
	
	//A list of shapes that should be loaded before the next draw starts.
	private ArrayList<String> shapeGraphicsLoad = new ArrayList<String>();
	private ArrayList<PGraphics> shapeGraphicsCache = new ArrayList<PGraphics>();
	
	//Emergency backup graphic:
	private PGraphics emergencyGraphic = null;
	
	
	private GameApplet applet = null;
	private String imagePath;
	
	public ImageService(GameApplet applet) {
		//Load world
		this.applet = applet;
		
		//Load paths
		this.imagePath = applet.dataPath("gameimages");
		prt("Image Path: "+this.imagePath);
		
		//Simple purple tile. 
		emergencyGraphic = applet.createGraphics(50, 50, GameApplet.P3D);
		emergencyGraphic.beginDraw();
		emergencyGraphic.background(255,0,255);
		emergencyGraphic.endDraw();
		
		
		applet.registerMethod("post", this);
		prt("Image Service Initialized.");
	}
	
	public PGraphics emergencyGraphic() {
		return this.emergencyGraphic;
	}
	
	/**
	 * Returns a saved shape, or loads if necessary.
	 * @param fileName to find.
	 * @return a PShape from a given filename.
	 */
	public PShape getShape(String fileName) {
		PShape toReturn = shapeMap.getOrDefault(fileName, null);
		if(toReturn == null) {
			prt("No Shape: ["+fileName+"], attempting to load from world.");
			toReturn = applet.loadShape(this.imagePath + "\\"+fileName);
			shapeMap.put(fileName, toReturn);
		}
		return toReturn;
	}
	
	/**
	 * Returns a saved image, or loads it if necessary.
	 * @param fileName the file to find.
	 * @return a PImage from a given filename.
	 */
	public PImage getImage(String fileName) {
		PImage toReturn = imageMap.getOrDefault(fileName, null);
		if(toReturn == null) {
			prt("No Image: ["+fileName+"], attempting to load from file system.");
			prt(this.imagePath.concat("\\"+fileName));
			toReturn = applet.loadImage(this.imagePath.concat("\\"+fileName));
			imageMap.put(fileName, toReturn);
		}
		return toReturn;
	}
	
	
	/**
	 * Returns a PGraphics object of a given shape or image. 
	 * @param fileName of the file to use.
	 * @return a PGraphics object to be displayed using World.image().
	 */
	public PGraphics getGraphicFromShape(String fileName) {
		PGraphics toReturn = graphicMap.getOrDefault(fileName, null);
		if(toReturn == null && !this.shapeGraphicsLoad.contains(fileName)) {
			prt("No PGraphics: ["+fileName+"], caching then loading later.");
			this.shapeGraphicsLoad.add(fileName);
			this.shapeGraphicsCache.add(toReturn);
			this.post();
			toReturn = graphicMap.getOrDefault(fileName, null);
		}
		return toReturn;
	}
	
	 
	
	
	public void post() {
		
		//If we have a list of graphics that need to load.
		if(this.shapeGraphicsLoad.size() > 0) {
			
			String fileName = shapeGraphicsLoad.get(shapeGraphicsLoad.size()-1);
			PShape shape = this.getShape(fileName);
			prt("Began loading PGraphics: ["+fileName+"]");
			PGraphics graphic = this.applet.createGraphics((int) shape.getWidth(), (int) shape.getHeight());
			graphic.beginDraw();
			graphic.shape(shape, 0, 0);
			graphic.endDraw();
			
			graphicMap.put(fileName, graphic);
			this.shapeGraphicsLoad.remove(shapeGraphicsLoad.size()-1);
			PGraphics p = this.shapeGraphicsCache.get(this.shapeGraphicsCache.size()-1);
			p = graphic;
			this.shapeGraphicsCache.remove(this.shapeGraphicsCache.size()-1);
			prt("Finished Loading PGraphic: ["+fileName+"]");
		}
	}
	
	private void prt(String val) {
		System.out.println("[IMAGE SERVICE] "+val);
	}

}
