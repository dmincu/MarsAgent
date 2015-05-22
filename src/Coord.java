
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

	/* Returns the direction an agent need to take in order
	 * to reach target from source 
	 0 - already there
	 * 1 - UP
	 * 2 - LEFT
	 * 3 - RIGHT
	 * 4 - DOWN
	 */
	public int getDirectionTo(Coord target) {
		if (x == target.x && y == target.y) {
			return 0;
		}
		if (x < target.x) {
			return 3;
		} else if (x > target.x) {
			return 2;
		} else if (y < target.y) {
			return 1;
		} else {
			return 4;
		}
	}
	
	public Coord getNextCoord(int direction) {
		/* UP */
		if (direction == 1) {
			return new Coord(x, y + 1);
		} else if (direction == 2) { // LEFT
			return new Coord(x - 1, y);
		} else if (direction == 3) { // RIGHT
			return new Coord(x + 1, y);
		} else if (direction == 4) { // DOWN
			return new Coord(x, y - 1);
		} else 
			return new Coord(x, y);
	}
	
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
}
