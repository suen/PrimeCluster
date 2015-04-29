package com.daubajee.prime;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import com.daubajee.prime.behaviours.AgentManagerListenerBehaviour;
import com.daubajee.prime.behaviours.HandleAgentManagerBehaviour;
import com.daubajee.prime.behaviours.ContainerChangeListenerBehaviour;
import com.daubajee.prime.behaviours.HandleComputeAgentBehaviour;

public class MasterAgent extends Agent {

	
	public List<AID> managerAgents = new ArrayList<AID>();
	public List<AID> computeAgents = new ArrayList<AID>();
	
	@Override
	protected void setup() {
		super.setup();
		registerWithDFService();

		addBehaviour(new HandleAgentManagerBehaviour(this, 500));
		
		addBehaviour(new HandleComputeAgentBehaviour(this, 500));
		
		
	    try {
		    AgentContainer container = this.getContainerController();
	    	AgentController a = container.createNewAgent("manager",
	    			"com.daubajee.prime.ComputeAgentManager", null);
	    	a.start();
  	
	    	System.out.println("[Master] Manager created");
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	}
	
	private void registerWithDFService(){
		String name = getName();
		ServiceDescription sd = new ServiceDescription();
		sd.setName(name);
		sd.setType("master-agent");

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
}
