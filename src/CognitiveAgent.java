import jade.core.*;

public class CognitiveAgent extends GenericAgent {

	CognitiveAgent(Grid g, Base b, Coord c) {
		super(g, b, c);
		
		this.isReactive = false;
	}
}
