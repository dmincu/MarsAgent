import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class DrawScene {
	public static JFrame frmMain;
	public static double dim_x, dim_y;
	
	public JPanel drawCircle(final double x, final double y, final double r, final Color c) {
        JPanel panel = new JPanel() {

			private static final long serialVersionUID = -3455244477301304380L;

			public void paintComponent(Graphics g) {
        		g.setColor(c);
                g.drawOval((int)(x - r / 2), (int)(y - r / 2), (int)r, (int)r);
            }
        };
        
        frmMain.add(panel);
        frmMain.setVisible(true);
        
        return panel;
	}
	
	public void init(double dim_x1, double dim_y1) {
		dim_x = dim_x1;
		dim_y = dim_y1;
		
		frmMain = new JFrame();
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.setSize((int)dim_x + 100, (int)dim_y + 100);
	}

}
