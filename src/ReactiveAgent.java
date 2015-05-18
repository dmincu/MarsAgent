import jade.core.*;
import jade.core.behaviours.SimpleBehaviour;

public class ReactiveAgent extends GenericAgent {
	
	protected void setup() {
		System.out.println("Hello. My name is " + this.getLocalName());
		
		addBehaviour(new ReactiveResponderBehaviour());
	}

}

class ReactiveResponderBehaviour extends SimpleBehaviour {

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
