package com.daubajee.prime.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class CreateComputeAgentBehaviour extends TickerBehaviour {

	
	public CreateComputeAgentBehaviour(Agent agent, long period) {
		super(agent, period);
	}

	@Override
	protected void onTick() {

		//create a compute agent per tick
	    AgentContainer container = myAgent.getContainerController();
	    
	    long millis = System.currentTimeMillis();

	    try {
	    	AgentController a = container.createNewAgent(String.valueOf(millis) + "-compute", 
	    			"com.daubajee.prime.ComputeAgent", null);
	    	a.start();
	    	System.out.println("[MasterAgent] A new compute agent created");
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
		
		
	}

}
