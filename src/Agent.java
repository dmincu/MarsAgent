import java.awt.Color;

import javax.swing.JPanel;


public class Agent {
	
	JPanel panelCol = null;
	
	boolean isReactive;
	boolean isDead;
	
	double speed, x, y, radius;
	
	Agent() {
		isReactive = true;
		isDead = false;
		x = 0;
		y = 0;
		speed = 1;
		radius = 1;
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

		this.panelCol = drawer.drawCircle(this.x, this.y, this.radius, c);
	}

}
