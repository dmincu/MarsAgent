import java.awt.Color;


public class Mars {
	
	public void init() {
		
	}
	
	public static void main(String[] args) {
		DrawScene drawer = new DrawScene();
		drawer.init(300, 400);
		drawer.drawCircle(100, 100, 50, new Color(0));
	}
}
