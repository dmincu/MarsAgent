import java.awt.Color;


public class Mars {
	
	public void init() {
		
	}
	
	public static void main(String[] args) {
		DrawScene drawer = new DrawScene();
		drawer.init(500, 180);
		Grid grid = new Grid();
		grid.initGridFromFile("map.txt");
		grid.drawGrid(drawer);
		
		//drawer.drawCircle(100, 100, 50, new Color(0));
		ReactiveAgent agent = new ReactiveAgent(drawer, grid);
		agent.move(2);
		//drawer.drawCircle(1, 0, 50, new Color(50));
	}
}
