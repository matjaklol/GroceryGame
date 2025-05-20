/**
 * 
 */
/**
 * @author PC1
 *
 */
module Grocery_Game {
	requires core;
	requires org.dyn4j;
	requires gluegen.rt;
	requires jnoise.core;
	requires jnoise.generators;
	requires jnoise.modifiers;
	requires jnoise.pipeline;
	requires java.desktop;
	
	exports game.core to core;
	exports game.userinput to core;
	exports game.util to core;
//	exports game.effects to core;
//	exports game.entities to core;
//	exports game.physics to core;
//	exports game.solarsystem to core;
//	exports game.userinput to core;
	
//	exports main to core;
}