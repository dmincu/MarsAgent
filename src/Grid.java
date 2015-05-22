import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Class used for tracking the position of various objects on the map.
 * @author fbrad
 *
 */
public class Grid {
	ArrayList<Coord> obstacles;
	ArrayList<Coord> resources;
	HashMap<Coord, Integer> resourcesValues;
	ArrayList<Coord> searchAgents;
	ArrayList<Coord> carrierAgents;
	
	public Grid() {
		obstacles = new ArrayList<Coord>();
		resources = new ArrayList<Coord>();
		resourcesValues = new HashMap<Coord,Integer>();
		searchAgents = new ArrayList<Coord>();
		carrierAgents = new ArrayList<Coord>();
	}
	
	public void addObstacle(int x, int y) {
		obstacles.add(new Coord(x, y));
	}
	
	public boolean isObstacleAt(Coord target) {
		for (Coord c : obstacles) {
			if (c.x == target.x && c.y == target.y) 
				return true;
		}
		
		return false;
	}
	
	public boolean isResourceAt(Coord target) {
		for (Coord c : resources) {
			if (c.x == target.x && c.y == target.y) 
				return true;
		}
		
		return false;
	}
	
	public int getResourceValue(Coord target) {
		for (Map.Entry<Coord, Integer> entry : resourcesValues.entrySet()) {
			Coord c = entry.getKey();
			int value = entry.getValue().intValue();
			
			if (c.x == target.x && c.y == target.y)
				return value;
		}
		
		return 0;
	}
	
	/**
	 * Returns true if removal succeeded or false otherwise.
	 * @param target
	 * @return
	 */
	public boolean removeResource(Coord target) {
		for (Map.Entry<Coord, Integer> entry : resourcesValues.entrySet()) {
			Coord c = entry.getKey();
			if (c.x == target.x && c.y == target.y) {
				resourcesValues.remove(entry);
				return true;
			}		
		}
		return false;
	}
	
	public void addSearchAgent(Coord target) {
		searchAgents.add(target);
	}
	
	public void removeSearchAgent(Coord target) {
		Coord coord = null;
		int i;
		System.out.println("[removeSearch] " + target.x + ' ' + target.y);
		int searchAgentsCount = searchAgents.size();
		for (i = searchAgentsCount - 1; i >= 0; i--) {
			System.out.println("[removeSearch] i = " + i);
			coord = searchAgents.get(i);
			if (coord.x == target.x && coord.y == target.y) {
				searchAgents.remove(i);
				System.out.println("[removeSearch] removed i = " + i);
			}
		}
	}
	
	public void addCarrierAgent(Coord target) {
		carrierAgents.add(target);
	}
	
	public void removeCarrierAgent(Coord target) {
		Coord coord = null;
		int i;
		for (i = 0; i < searchAgents.size(); i++) 
			coord = carrierAgents.get(i);
			if (coord.x == target.x && coord.y == target.y) {
				carrierAgents.remove(i);
			}
	}

}
