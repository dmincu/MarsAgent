import java.util.Random;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReactiveAgent extends GenericAgent {
	
	int capacity;
	
	Random r = new Random();
	
	ReactiveAgent(Grid g, Base b, Coord c) {
		super(g, b, c);
		grid.addSearchAgent(this.coords);
		
		this.isReactive = true;
		this.capacity = 0;
	}
	
	protected void setup() {
		System.out.println("Hello. My name is " + this.getLocalName());
		
		addBehaviour(new ReactiveResponderBehaviour(this));
		
		while (!canGoHome) {
			if (this.grid.isResourceAt(this.coords)) {
				pickUp();
			} else {
				move();
			}
		}
		
		returnHome();
	}
	
	public boolean move(int direction) {
		this.grid.removeSearchAgent(this.coords);
		
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
		
		this.grid.addSearchAgent(this.coords);
		
		return canMove;
	}
	
	public boolean move() {
		if (this.grid.isResourceAt(this.coords)) {
			pickUp();
			if (this.resourcesGathered > this.capacity) {
				returnHome();
			}
		}
		
		if (!this.canGoHome)
			return move(r.nextInt() % 4);
		else 
			returnHome();
		
		return false;
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
	
	public void pickUp() {
		this.resourcesGathered += this.grid.getResourceValue(this.coords);
		this.grid.removeResource(this.coords);
		
		System.out.println("Picked up resource ... ");
	}
	
	public void giveResourcesToBase() {
		this.base.receiveResources(this.resourcesGathered);
		this.resourcesGathered = 0;
	}
	
	public void returnHome() {
		System.out.println("Returning home ... ");
		
		while (this.coords.x != this.base.coords.x &&
				this.coords.y != this.base.coords.y) {
			boolean canPerformMove = false;
			
			if (this.coords.x > this.base.coords.x) {
				if (this.coords.y > this.base.coords.y) {
					canPerformMove = move(DOWN);
				} else if (this.coords.y < this.base.coords.y) {
					canPerformMove = move(UP);
				} else {
					canPerformMove = move(LEFT);
				}
			} else {
				if (this.coords.y > this.base.coords.y) {
					canPerformMove = move(DOWN);
				} else if (this.coords.y < this.base.coords.y) {
					canPerformMove = move(UP);
				} else {
					canPerformMove = move(RIGHT);
				}
			}
			
			if (!canPerformMove) {
				move(r.nextInt() % 4);
			}
			
			this.base.draw();
		}
		
		System.out.println("Home ... ");
		
		giveResourcesToBase();
		
		// Agents will be removed sequentially
		if (this.canGoHome) {
			this.isDead = true;
			this.base.reactiveAgents.remove(this);
			this.base.draw();
		}
	}

}

class ReactiveResponderBehaviour extends CyclicBehaviour {
	
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
	
	ReactiveAgent agent;
	
	public ReactiveResponderBehaviour(ReactiveAgent agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		ACLMessage aclMessage = myAgent.receive(mt);

		if (aclMessage != null) {
			System.out.println(myAgent.getLocalName() + ": I receive message.\n" + aclMessage);
			
			String content = aclMessage.getContent();
			
			if (content.equals(agent.GO_HOME)) {
				agent.canGoHome = true;
			}
		}
		
	}
	
}
