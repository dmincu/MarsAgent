import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import jade.core.*;

import javax.swing.JPanel;


public class Base extends Agent {

	public static final int RADIUS = 20;
	
	ServerSocket serverSocket;
	Coord coords;
	JPanel basePanel;
	Grid grid;
	DrawScene drawer;
	
	ArrayList<ReactiveAgent> reactiveAgents = new ArrayList<>();
	ArrayList<CognitiveAgent> cognitiveAgents = new ArrayList<CognitiveAgent>();
	
	int resourceLimit, searchAgentLimit;
	int currentNumberOfResources = 0;
	
	Base() {
		try 
        { 
            serverSocket = new ServerSocket(11111); 
        } 
        catch(IOException ioe) 
        { 
            System.out.println("Could not create server socket on port 11111. Quitting."); 
            System.exit(-1); 
        } 
	}
	
	Base(DrawScene d, Grid g, int limit, int searchAgentLimit) {
		this.drawer = d;
		this.grid = g;
		this.resourceLimit = limit;
		this.searchAgentLimit = searchAgentLimit;
		
		this.coords = g.base;
		
		for (int i = 0; i < g.searchAgents.size(); i++) {
			reactiveAgents.add(new ReactiveAgent(g, this, g.searchAgents.get(i)));
		}
		
		for (int i = 0; i < g.carrierAgents.size(); i++) {
			cognitiveAgents.add(new CognitiveAgent(g, this, g.carrierAgents.get(i)));
		}
	}

	public boolean isResourceLevelAchieved() {
		return currentNumberOfResources >= resourceLimit;
	}
	
	public boolean canSpawnSearchAgent() {
		return reactiveAgents.size() < searchAgentLimit;
	}
	
	public void spawnSearchAgent() {
		Coord up = new Coord(coords.x, coords.y + RADIUS);
		Coord down = new Coord(coords.x, coords.y - RADIUS);
		Coord left = new Coord(coords.x - RADIUS, coords.y);
		Coord right = new Coord(coords.x + RADIUS, coords.y);
		
		Random r = new Random();
		switch (r.nextInt() % 4) {
		case 0:
			if (!grid.isObstacleAt(up)) {
				reactiveAgents.add(new ReactiveAgent(grid, this, up));
			}
			break;
		case 1:
			if (!grid.isObstacleAt(down)) {
				reactiveAgents.add(new ReactiveAgent(grid, this, down));
			}
			break;
		case 2:
			if (!grid.isObstacleAt(left)) {
				reactiveAgents.add(new ReactiveAgent(grid, this, left));
			}
			break;
		case 3:
			if (!grid.isObstacleAt(right)) {
				reactiveAgents.add(new ReactiveAgent(grid, this, right));
			}
			break;
		}
	}
	
	public void spawnCarrierAgent() {
		Coord up = new Coord(coords.x, coords.y + RADIUS);
		cognitiveAgents.add(new CognitiveAgent(grid, this, up));
	}
	
	public void moveSearchAgents() {
		System.out.println("[moveSearchAgents] move");
		for (int i = 0; i < reactiveAgents.size(); i++) {
			reactiveAgents.get(i).move();
		}
	}
	
	public void moveCarrierAgents() {
		for (int i = 0; i < cognitiveAgents.size(); i++) {
			cognitiveAgents.get(i).move();
		}
}
			
	public void receiveResources(int value) {
		this.currentNumberOfResources += value;
	}
	
	public void draw() {
		if (this.basePanel != null) {
			drawer.frmMain.remove(this.basePanel);
			drawer.frmMain.revalidate();
			drawer.frmMain.repaint();
		}
		
		Color c = new Color(255, 255, 0);

		this.basePanel = drawer.drawCircle(this.coords.x, this.coords.y, RADIUS, c);
		
		
		// TODO: maybe move this from here
		for (int i = 0; i < reactiveAgents.size(); i++) {
			reactiveAgents.get(i).draw(drawer);
		}
		
		for (int i = 0; i < cognitiveAgents.size(); i++) {
			cognitiveAgents.get(i).draw(drawer);
		}
	}
	
	public void initServer() {
		System.out.println("[initServer] waiting for connection");
		Socket client = null;
		try {
			client = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Accepted a connection");
		new AgentSocketThread(this, client);
	}
}

/* socket for base-agents communication */
class AgentSocketThread implements Runnable {
	Socket socket;
	Base base;
	BufferedReader reader;
	BufferedWriter writer;
	
	public AgentSocketThread(Base b, Socket sk) {
		base = b;
		socket = sk;
		new Thread(this).start();
	}
	
	private void sendMessage (String line) {
		try {
			this.writer.write(line);
			this.writer.newLine();
			this.writer.flush();
		} catch (IOException e) {
			System.out.println("Failed to send the message <" + line + "> to the associated client");
			return;
		}
	}

	@Override
	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Failed to create a reader for a client socket");
			e.printStackTrace();
			return;
		}
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			System.out.println("Failed to create a writer for a client socket");
			e.printStackTrace();
			return;
		}

		String line = null;
		boolean connected = true;
		while (connected) {
			try {
				line = reader.readLine();
			} catch (IOException e) {
				System.out.println("An exception was caught while trying to read a message from the client socket");
				e.printStackTrace();
				break;
			}
			System.out.println("[Base] Received " + line);
		}
	}
	
}
