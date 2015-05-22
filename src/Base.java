import java.awt.Color;
import java.util.ArrayList;
import jade.core.*;
import javax.swing.JPanel;


public class Base extends Agent {

	public static final int RADIUS = 20;
	
	Coord coords;
	JPanel basePanel;
	Grid grid;
	
	ArrayList<ReactiveAgent> reactiveAgents = new ArrayList<>();
	ArrayList<CognitiveAgent> cognitiveAgents = new ArrayList<CognitiveAgent>();
	
	int resourceLimit;
	
	Base() {
		
	}
	
	Base(Grid g, int limit) {
		this.grid = g;
		this.resourceLimit = limit;
		
		this.coords = g.base;
		
		for (int i = 0; i < g.searchAgents.size(); i++) {
			reactiveAgents.add(new ReactiveAgent(g, g.searchAgents.get(i)));
		}
		
		for (int i = 0; i < g.carrierAgents.size(); i++) {
			cognitiveAgents.add(new CognitiveAgent(g, g.carrierAgents.get(i)));
		}
	}

	
	public void draw(DrawScene drawer) {
		if (this.basePanel != null) {
			drawer.frmMain.remove(this.basePanel);
			drawer.frmMain.revalidate();
			drawer.frmMain.repaint();
		}

		System.out.println("[Base][draw] ");
		
		Color c = new Color(255, 255, 0);

		this.basePanel = drawer.drawCircle(this.coords.x, this.coords.y, RADIUS, c);
		
		
		// TODO: maybe move this from here
		for (int i = 0; i < reactiveAgents.size(); i++) {
			reactiveAgents.get(i).draw(drawer);
		}
		
		for (int i = 0; i < cognitiveAgents.size(); i++) {
			cognitiveAgents.get(i).draw(drawer);
		}
	}
}