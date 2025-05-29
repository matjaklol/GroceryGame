/**
 * 
 */
/**
 * @author PC1
 *
 */
module Grocery_Game {
	//Rendering/processing.
	requires transitive core;
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
	requires transitive minim;
	
	//Physics
	requires transitive org.dyn4j;
	
	exports game.core to core;
	exports game.userinput to core;
	exports game.util to core;
	exports game.management to core;
	exports game.core.objects to core;
}