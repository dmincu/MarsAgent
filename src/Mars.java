import java.awt.Color;

public class Mars {
	
	public static final int MAXLIMIT = 4;
	public static final int MAXSEARCHAGENTLIMIT = 3;
	
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
				System.out.println("[runFirstPart] spawned");
				b.spawnSearchAgent();
			}
			
			b.moveSearchAgents();
			
			b.draw(drawer);
		}
	}
	
	public static void main(String[] args) {
		init();
		for (int i = 0; i < grid.searchAgents.size(); i++) 
			System.out.println(grid.searchAgents.get(i));
		
		runFirstPart();
	}
}
