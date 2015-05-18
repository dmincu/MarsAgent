import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReactiveAgent extends GenericAgent {
	
	ReactiveAgent() {
		super();
		
		this.isReactive = true;
	}
	
	protected void setup() {
		System.out.println("Hello. My name is " + this.getLocalName());
		
		addBehaviour(new ReactiveResponderBehaviour(this));
		
		while (!canGoHome) {
			move();
		}
		
		returnHome();
	}
	
	public void move() {
		
	}
	
	public boolean moveUp() {
		return false;
	}
	
	public boolean moveDown() {
		return false;
	}

	public boolean moveLeft() {
		return false;
	}
	
	public boolean moveRight() {
		return false;
	}
	
	public void returnHome() {
		
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
