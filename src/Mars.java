import java.awt.Color;


public class Mars {
	
	public static final int MAXLIMIT = 4;
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
		
		b = new Base(grid, MAXLIMIT, MAXSEARCHAGENTLIMIT);
		b.draw(drawer);
	}
	
	public static void runFirstPart() {
		while (!b.isResourceLevelAchieved()) {
			if (b.canSpawnSearchAgent()) {
				b.spawnSearchAgent();
			}
			
			b.moveSearchAgents();
			
			try {
			    Thread.sleep(200);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
			grid.drawGrid(drawer);
			b.draw(drawer);
		}
		
		for (int i = 0; i < b.reactiveAgents.size(); i++) {
			b.reactiveAgents.get(i).canGoHome = true;
			b.reactiveAgents.get(i).returnHome();
		}
	}
	
	public static void main(String[] args) {
		init();
		
		runFirstPart();
	}
}
