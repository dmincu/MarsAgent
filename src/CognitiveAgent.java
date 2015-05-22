import java.util.Random;

import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

enum CarrierState {
	GOTO_RESOURCE, ROAM_AROUND
};

public class CognitiveAgent extends GenericAgent {
	CarrierState state; 
	Coord target;
	boolean comeHome;
	Random r;
	
	CognitiveAgent(Grid g) {
		super(g);
		this.state = CarrierState.ROAM_AROUND;
		this.isReactive = false;
		this.comeHome = false;
		this.target = null;
		this.r = new Random();
		System.out.println("[CognitiveAgent] constructor");
	}
	
	protected void registerCarrier() {
		/* bidder registers himself */
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("carrier");
		sd.setName("Carrier Agent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("[Bidder] Successfully registered");
	}
	
	protected void setup() {
		registerCarrier();
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				ACLMessage msgRx = receive();
					if (msgRx != null) {
						if (msgRx.getPerformative() == ACLMessage.REQUEST) {
							String content = msgRx.getContent();
							
							/* GOTO X Y */
							if (content.startsWith("GOTO")) {
								String[] toks = content.split(" ");
								int x = Integer.parseInt(toks[1]);
								int y = Integer.parseInt(toks[2]);
								target = new Coord(x, y);
								state = CarrierState.GOTO_RESOURCE;
								System.out.println("[Cognitive][action] going to " + target);
							} else if (content.compareTo("GO_HOME") == 0) {
								target = null;
								System.out.println("[Cognitive][action] going to base");
							}
						}
					}
			}
			
			public void performStep() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				switch (state) {
					case GOTO_RESOURCE:
						// remove oneself from grid
						grid.removeCarrierAgent(coords);
						
						// compute next coordinate
						int dir = coords.getDirectionTo(target);
						Coord next = coords.getNextCoord(dir);
						
						// add new coordinates to grid
						grid.addCarrierAgent(next);
						
						// redraw grid
						//draw(drawer);
						break;
					
					case ROAM_AROUND:
						move();
				}
			
				
			}
			
		}); 	
	}
	
	public void move(int direction) {
		this.grid.removeSearchAgent(this.coords);
		
		switch (direction) {
		case UP:
			moveUp();
			break;
		case LEFT:
			moveLeft();
			break;
		case RIGHT:
			moveRight();
			break;
		case DOWN:
			moveDown();
			break;
		}
		
		this.grid.addSearchAgent(this.coords);
		//test
		//this.draw(drawer);
	}
	
	public void move() {
		move(r.nextInt() % 4);
	}
	
	public boolean moveUp() {
		this.coords.y++;
		
		if (this.grid.isObstacleAt(this.coords)) {
			this.coords.y--;
			return false;
		}
		
		return true;
	}
	
	public boolean moveDown() {
		this.coords.y--;
		
		if (this.grid.isObstacleAt(this.coords)) {
			this.coords.y++;
			return false;
		}
		
		return true;
	}

	public boolean moveLeft() {
		this.coords.x--;
		
		if (this.grid.isObstacleAt(this.coords)) {
			this.coords.x++;
			return false;
		}
		
		return true;
	}
	
	public boolean moveRight() {
		this.coords.x++;
		
		if (this.grid.isObstacleAt(this.coords)) {
			this.coords.x--;
			return false;
		}
		
		return true;
	}
	
	// Put agent clean-up operations here
	protected void takeDown() {
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Printout a dismissal message
		System.out.println("Bidder "+getAID().getName()+" terminating.");
	}
	
}
