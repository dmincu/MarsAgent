import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import jade.core.*;
import javax.swing.JPanel;


public class Base extends Agent {

	public static final int RADIUS = 20;
	
	Coord coords;
	JPanel basePanel;
	Grid grid;
	
	ArrayList<ReactiveAgent> reactiveAgents = new ArrayList<>();
	ArrayList<CognitiveAgent> cognitiveAgents = new ArrayList<CognitiveAgent>();
	
	int resourceLimit, searchAgentLimit;
	int currentNumberOfResources = 0;
	
	Base() {
		
	}
	
	Base(Grid g, int limit, int searchAgentLimit) {
		this.grid = g;
		this.resourceLimit = limit;
		this.searchAgentLimit = searchAgentLimit;
		
		this.coords = g.base;
		
		for (int i = 0; i < g.searchAgents.size(); i++) {
			reactiveAgents.add(new ReactiveAgent(g, g.searchAgents.get(i)));
		}
		
		for (int i = 0; i < g.carrierAgents.size(); i++) {
			cognitiveAgents.add(new CognitiveAgent(g, g.carrierAgents.get(i)));
		}
	}

	public boolean isResourceLevelAchieved() {
		return currentNumberOfResources >= resourceLimit;
	}
	
	public boolean canSpawnSearchAgent() {
		return reactiveAgents.size() <= searchAgentLimit;
	}
	
	public void spawnSearchAgent() {
		Coord up = new Coord(coords.x, coords.y + RADIUS);
		Coord down = new Coord(coords.x, coords.y - RADIUS);
		Coord left = new Coord(coords.x - RADIUS, coords.y);
		Coord right = new Coord(coords.x + RADIUS, coords.y);
		
		Random r = new Random();
		switch (r.nextInt() % 4) {
		case 0:
			if (!grid.isObstacleAt(up)) {
				reactiveAgents.add(new ReactiveAgent(grid, up));
			}
			break;
		case 1:
			if (!grid.isObstacleAt(down)) {
				reactiveAgents.add(new ReactiveAgent(grid, down));
			}
			break;
		case 2:
			if (!grid.isObstacleAt(left)) {
				reactiveAgents.add(new ReactiveAgent(grid, left));
			}
			break;
		case 3:
			if (!grid.isObstacleAt(right)) {
				reactiveAgents.add(new ReactiveAgent(grid, right));
			}
			break;
		}
	}
	
	public void spawnCarrierAgent() {
		Coord up = new Coord(coords.x, coords.y + RADIUS);
		cognitiveAgents.add(new CognitiveAgent(grid, up));
	}
	
	public void moveSearchAgents() {
		System.out.println("[moveSearchAgents] move");
		for (int i = 0; i < reactiveAgents.size(); i++) {
			reactiveAgents.get(i).move();
		}
	}
	
	public void moveCarrierAgents() {
		for (int i = 0; i < cognitiveAgents.size(); i++) {
			cognitiveAgents.get(i).move();
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
