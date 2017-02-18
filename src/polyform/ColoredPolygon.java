package polyform;
import java.awt.*;

public class ColoredPolygon {
	Color color;
	Polygon polygon;
	
	ColoredPolygon(int[] xcoords, int[] ycoords, Color color){
		assert xcoords.length==ycoords.length;
		this.color=color;
		this.polygon=new Polygon(xcoords, ycoords, xcoords.length);
	}
	
}
