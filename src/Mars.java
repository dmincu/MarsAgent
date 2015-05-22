import java.awt.Color;

import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.ContainerController;

public class Mars {
	
	public static final int MAXLIMIT = 1;
	public static final int MAXSEARCHAGENTLIMIT = 5;
	
	static Base b;
	static Grid grid;
	static DrawScene drawer;
	
	public static void init() {
		drawer = new DrawScene();
		drawer.init(500, 180);
		
		grid = new Grid();
		grid.initGridFromFile("map.txt");
		grid.drawGrid(drawer);
		
		b = new Base(drawer, grid, MAXLIMIT, MAXSEARCHAGENTLIMIT);
		b.draw();
	}
	
	public static void runFirstPart() {
		boolean hasCarrier = false;
		while (!b.isResourceLevelAchieved()) {
			if (b.canSpawnSearchAgent()) {
				System.out.println("[runFirstPart] spawned");
				b.spawnSearchAgent();
			}
			
			if (!hasCarrier) {
				b.spawnCarrierAgent();
				hasCarrier = true;
			}
			
			b.moveSearchAgents();
			b.moveCarrierAgents();

			try {
			    Thread.sleep(200);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
			grid.drawGrid(drawer);
			b.draw();
		}
		
		for (int i = 0; i < b.reactiveAgents.size(); i++) {
			b.reactiveAgents.get(i).canGoHome = true;
			b.reactiveAgents.get(i).returnHome();
		}
	}
	
	public static void runSecondPart() {
		Base base = new Base();
		base.initServer();
		Grid g = new Grid();
		GenericAgent agent = new GenericAgent(g, base, new Coord(0, 0));
		agent.connectToBase();
	}
	
	
	public static void main(String[] args) {
		init();
		for (int i = 0; i < grid.searchAgents.size(); i++) 
			System.out.println(grid.searchAgents.get(i));
		
		runFirstPart();
		//runSecondPart();
	}
}
