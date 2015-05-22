import java.util.Random;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReactiveAgent extends GenericAgent {
	
	int capacity;
	
	Random r = new Random();
	
	ReactiveAgent(Grid g, Coord c) {
		super(g, c);
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
	
	public void pickUp() {
		this.resourcesGathered += this.grid.getResourceValue(this.coords);
		this.grid.removeResource(this.coords);
	}
	
	public void returnHome() {
		while (this.coords.x != this.base.x &&
				this.coords.y != this.base.y) {
			if (this.coords.x > this.base.x) {
				if (this.coords.y > this.base.y) {
					move(DOWN);
				} else if (this.coords.y < this.base.y) {
					move(UP);
				} else {
					move(LEFT);
				}
			} else {
				if (this.coords.y > this.base.y) {
					move(DOWN);
				} else if (this.coords.y < this.base.y) {
					move(UP);
				} else {
					move(RIGHT);
				}
			}
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
