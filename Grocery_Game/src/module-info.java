/**
 * 
 */
/**
 * @author PC1
 *
 */
module Grocery_Game {
	//Rendering/processing.
	requires core;
	requires gluegen.rt;
	requires java.desktop;
	
	//Noise Generator
	requires jnoise.core;
	requires jnoise.generators;
	requires jnoise.modifiers;
	requires jnoise.pipeline;
	
	
	//Sound
	requires tritonus.aos;
	requires mp3spi;
	requires minim;
	
	//Physics
	requires org.dyn4j;
	
	exports game.core to core;
	exports game.userinput to core;
	exports game.util to core;
}