import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import jade.core.*;

import javax.swing.JPanel;


public class GenericAgent extends Agent {
	BaseReader baseReader;
	BufferedWriter writer;
	Socket baseSocket;
	JPanel panelCol = null;
	Grid grid;
	
	public static final String GO_HOME = "COME HOME";
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;
	
	boolean isReactive;
	boolean isDead;
	boolean canGoHome;
	
	int resourcesGathered = 0;
	
	double speed, radius;
	
	Base base;
	
	Coord coords;
	
	GenericAgent(Grid g, Base b, Coord c) {
		isReactive = true;
		isDead = false;
		canGoHome = false;
		coords = c;
		speed = 1;
		radius = 20;
		grid = g;
		base = b;
	}
	
	public Coord getCoords() {
		return coords;
	}
	
	public void draw(DrawScene drawer) {
		if (this.panelCol != null) {
			drawer.frmMain.remove(this.panelCol);
			drawer.frmMain.revalidate();
			drawer.frmMain.repaint();
		}
		
		Color c;

		if (isReactive) {
			c = new Color(0, 255, 0);
		} else {
			c = new Color(255, 0, 0);
		}

		if (!isDead) {
			this.panelCol = drawer.drawCircle(this.coords.x, this.coords.y, this.radius, c);
		}
	}
	
	private void sendMessage(String line) {
		try {
			this.writer.write(line);
			this.writer.newLine();
			this.writer.flush();
		} catch (IOException e) {
			System.out.println("Failed to send the message <" + line + "> to the associated client");
			return;
		}
	}
	
	public void connectToBase() {
		try {
			baseSocket = new Socket("localhost", 11111);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[connectToBase] connected to baseSocket");
		try {
			writer = new BufferedWriter(new OutputStreamWriter(baseSocket.getOutputStream()));
		} catch (IOException e) {
			System.out.println("Failed to create a writer for a client socket");
			e.printStackTrace();
			return;
		}
		baseReader = new BaseReader(this, baseSocket);
		sendMessage("Hello, base!");
	}
}

/** 
 * Reads messages from Base
 * @author Flo
 *
 */
class BaseReader implements Runnable {
	GenericAgent agent;
	Socket clientSocket;
	BufferedReader reader;
	BufferedWriter writer;
	
	public BaseReader(GenericAgent a, Socket socket) {
		agent = a;
		clientSocket = socket;
		new Thread(this).start();
	}


	
	@Override
	public void run() {
         try {
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         String fromBase;
         System.out.println("[BaseReader] run: waiting messages from base");
         try {
			while ((fromBase = reader.readLine()) != null) {
				 System.out.println("[BaseReader] received " + fromBase + " from base");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
