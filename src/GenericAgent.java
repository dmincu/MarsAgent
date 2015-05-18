import java.awt.Color;
import jade.core.*;

import javax.swing.JPanel;


public class GenericAgent extends Agent {
	
	JPanel panelCol = null;
	
	public static final String GO_HOME = "COME HOME";
	
	boolean isReactive;
	boolean isDead;
	boolean canGoHome;
	
	double speed, radius;
	
	Coord coords;
	
	GenericAgent() {
		isReactive = true;
		isDead = false;
		canGoHome = false;
		coords = new Coord(0, 0);
		speed = 1;
		radius = 1;
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

		if (isDead) {
			c = new Color(0, 0, 0);
		}

		this.panelCol = drawer.drawCircle(this.coords.x, this.coords.y, this.radius, c);
	}

}
