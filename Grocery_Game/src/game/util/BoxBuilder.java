package game.util;

import org.dyn4j.geometry.*;
import org.dyn4j.dynamics.*;
public class BoxBuilder {

	public static Body createOpenBox(double x, double y, double width, double height, double thickness, double density) {
		Body box = new Body();
		
		//Left wall
		box.addFixture(Geometry.createRectangle(thickness, height));
		box.getFixture(0).getShape().translate(-width/2 + thickness/2, 0);
		box.getFixture(0).setDensity(density);
		
		//Right wall
		box.addFixture(Geometry.createRectangle(thickness, height));
		box.getFixture(1).getShape().translate(width/2 - thickness/2, 0);
		box.getFixture(1).setDensity(density);
		
		//Bottom wall
		box.addFixture(Geometry.createRectangle(width, thickness));
		box.getFixture(2).getShape().translate(0, height/2 - thickness/2);
		box.getFixture(2).setDensity(density);
		
		BodyFixture hover = new BodyFixture(Geometry.createRectangle(width, height));
		hover.setSensor(true);
		box.addFixture(hover);
		
		box.translate(x, y);
		
		return box;
	}
	
	public static Body createOpenBox(double x, double y, double width, double height, double thickness) {
		return createOpenBox(x, y, width, height, thickness, 1.0d);
	}
	
	public static Body createClosedBox(double x, double y, double width, double height, double thickness) {
		Body box = BoxBuilder.createOpenBox(x, y, width, height, thickness);
		
		//Add top wall
		box.addFixture(Geometry.createRectangle(width, thickness));
		box.getFixture(4).getShape().translate(0, -height/2 + thickness/2);
		
		return box;
	}

}
