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
	
	CognitiveAgent(Grid g, Base b, Coord c) {
		super(g, b, c);
		this.state = CarrierState.ROAM_AROUND;
		this.isReactive = false;
		this.comeHome = false;
		this.target = null;
		this.r = new Random();
		grid.addCarrierAgent(c);
		System.out.println("[CognitiveAgent] constructor");
	}
	
	protected void registerCarrier() {
		/* agent registers himself */
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
		System.out.println("[registerCarrier] Successfully registered");
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
					performStep();
			}
			
			public void performStep() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int dir;
				
				switch (state) {
					case GOTO_RESOURCE:						
						// compute next coordinate
						dir = coords.getDirectionTo(target);
						
						if (dir == 0) {
							//we've reached destination, collect resource
							resourcesGathered += grid.getResourceValue(coords);
							grid.removeResource(coords);
							state = CarrierState.ROAM_AROUND;
						} else if (!move(dir + 1)) {
							//if cannot move towards target, move randomly
							move(r.nextInt() % 4);
						}
						
						break;
					
					case ROAM_AROUND:
						if (comeHome) { 
							dir = coords.getDirectionTo(base.coords);
							if (dir == 0) {
								// we are home, remove agent
								System.out.println("[CognitiveAgent] we've reached /home");
								base.receiveResources(resourcesGathered);
								resourcesGathered = 0;
								grid.removeCarrierAgent(coords);
							} else if (!move(dir + 1)) {
								move(r.nextInt() % 4);
							}
						} else {
							move(r.nextInt() % 4);
						}
				}
			}
			
		}); 	
	}
	
	public boolean move(int direction) {
		this.grid.removeCarrierAgent(this.coords);
		
		boolean canMove = false;
		
		switch (direction) {
		case UP:
			canMove = moveUp();
			break;
		case LEFT:
			canMove = moveLeft();
			break;
		case RIGHT:
			canMove = moveRight();
			break;
		case DOWN:
			canMove = moveDown();
			break;
		}
		
		this.grid.addCarrierAgent(this.coords);
		
		return canMove;
	}
	
	public void move() {
		move(r.nextInt() % 4);
	}
	
	public boolean moveUp() {
		this.coords.y += this.radius;
		
		if (this.grid.isObstacleAt(this.coords)) {
			this.coords.y -= this.radius;
			return false;
		}
		
		return true;
	}
	
	public boolean moveDown() {
		this.coords.y -= this.radius;
		
		if (this.grid.isObstacleAt(this.coords)) {
			this.coords.y += this.radius;
			return false;
		}
		
		return true;
	}

	public boolean moveLeft() {
		this.coords.x -= this.radius;
		
		if (this.grid.isObstacleAt(this.coords)) {
			this.coords.x += this.radius;
			return false;
		}
		
		return true;
	}
	
	public boolean moveRight() {
		this.coords.x += this.radius;
		
		if (this.grid.isObstacleAt(this.coords)) {
			this.coords.x -= this.radius;
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
