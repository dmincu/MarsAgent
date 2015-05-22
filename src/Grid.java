import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;


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
	Coord base;
	
	HashMap<Coord, JPanel> resourcePanels = new HashMap<>();
	HashMap<Coord, JPanel> obstaclePanels = new HashMap<>();
	
	public static final char WALL = '#';
	public static final char RESOURCE = 'r';
	public static final char BASE = 'b';
	public static final char NOTHING = '0';
	public static final char SEARCH_AGENT = 's';
	public static final char CARRIER_AGENT = 'c';
	public static final int RADIUS = 20;
	
	public Grid() {
		obstacles = new ArrayList<Coord>();
		resources = new ArrayList<Coord>();
		resourcesValues = new HashMap<Coord,Integer>();
		searchAgents = new ArrayList<Coord>();
		carrierAgents = new ArrayList<Coord>();
	}
	
	public void drawGrid(DrawScene drawer) {
		for (Map.Entry<Coord, JPanel> entries : resourcePanels.entrySet()) {
			if (entries.getValue() != null) {
				drawer.frmMain.remove(entries.getValue());
				drawer.frmMain.revalidate();
				drawer.frmMain.repaint();
			}
			
			resourcePanels.remove(entries.getKey());
		}

		for (int i = 0; i < resources.size(); i++) {
			JPanel newResourcePanel = drawer.drawCircle(resources.get(i).x, resources.get(i).y, RADIUS, new Color(0, 0, 255));
			resourcePanels.put(resources.get(i), newResourcePanel);
		}
		
		for (int i = 0; i < obstacles.size(); i++) {
			if (obstaclePanels.get(obstacles.get(i)) == null) {
				JPanel newObstaclePanel = drawer.drawCircle(obstacles.get(i).x, obstacles.get(i).y, RADIUS, new Color(0, 255, 255));
				obstaclePanels.put(obstacles.get(i), newObstaclePanel);
			}
		}
	}
	
	public void initGridFromFile(String path) {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(path));
			
			int lineIndex = 0;
			String line = bf.readLine();
			while (line != null) {
				char[] parts = line.toCharArray();
				
				for (int i = 0; i < parts.length; i++) {
					Coord coordinates = new Coord(i * RADIUS + 10, lineIndex * RADIUS + 10);
					
					switch (parts[i]) {
					case WALL:
						obstacles.add(coordinates);
						break;
					case RESOURCE:
						resources.add(coordinates);
						resourcesValues.put(coordinates, 1);
						break;
					case SEARCH_AGENT:
						searchAgents.add(coordinates);
						break;
					case CARRIER_AGENT:
						carrierAgents.add(coordinates);
						break;
					case BASE:
						base = coordinates;
						break;
					}
				}
				
				line = bf.readLine();
				lineIndex++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
