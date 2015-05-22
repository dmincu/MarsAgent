
public class Coord {
	
	int x;
	int y;
	
	Coord() {
		x = 0;
		y = 0;
	}
	
	Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return x * 1000 + y;
	}
	
	@Override
	public boolean equals(Object other) {
		Coord otherC = (Coord) other;
		
		return this.x == otherC.x && this.y == otherC.y;
	}

}
