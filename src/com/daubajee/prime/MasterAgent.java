package com.daubajee.prime;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import com.daubajee.prime.behaviours.CreateComputeAgentBehaviour;

public class MasterAgent extends Agent {

	@Override
	protected void setup() {
		super.setup();
		registerWithDFService();
		
		addBehaviour(new CreateComputeAgentBehaviour(this, 2000));
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
