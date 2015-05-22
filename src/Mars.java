import java.awt.Color;


public class Mars {
	
	public static final int MAXLIMIT = 4;
	
	static Base b;
	static Grid grid;
	static DrawScene drawer;
	
	public static void init() {
		drawer = new DrawScene();
		drawer.init(500, 180);
		
		grid = new Grid();
		grid.initGridFromFile("map.txt");
		grid.drawGrid(drawer);
		
		b = new Base(grid, MAXLIMIT);
		b.draw(drawer);
	}
	
	public static void main(String[] args) {
		init();
	}
}
