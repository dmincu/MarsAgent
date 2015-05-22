import java.awt.Color;
import jade.core.*;

import javax.swing.JPanel;


public class GenericAgent extends Agent {
	JPanel panelCol = null;
	Grid grid;
	
	public static final String GO_HOME = "COME HOME";
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;
	
	boolean isReactive;
	boolean isDead;
	boolean canGoHome;
	
	int resourcesGathered = 0;
	
	double speed, radius;
	
	Base base;
	
	Coord coords;
	
	GenericAgent(Grid g, Base b, Coord c) {
		isReactive = true;
		isDead = false;
		canGoHome = false;
		coords = c;
		speed = 1;
		radius = 20;
		grid = g;
		base = b;
	}
	
	public Coord getCoords() {
		return coords;
	}
	
	public void draw(DrawScene drawer) {
		if (this.panelCol != null) {
			drawer.frmMain.remove(this.panelCol);
			drawer.frmMain.revalidate();
			drawer.frmMain.repaint();
		}
		
		Color c;

		if (isReactive) {
			c = new Color(0, 255, 0);
		} else {
			c = new Color(255, 0, 0);
		}

		if (!isDead) {
			this.panelCol = drawer.drawCircle(this.coords.x, this.coords.y, this.radius, c);
		}
	}

}
