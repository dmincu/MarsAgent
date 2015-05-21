import java.util.Random;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReactiveAgent extends GenericAgent {
	
	Random r = new Random();
	DrawScene drawer;
	
	ReactiveAgent(DrawScene d, Grid g) {
		super(g);
		grid.addSearchAgent(this.coords);
		drawer = d;
		
		this.isReactive = true;
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
		//test
		this.draw(drawer);
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
	
	public void pickUp() {
		this.resourcesGathered++;
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
