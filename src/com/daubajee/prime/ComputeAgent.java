package com.daubajee.prime;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ComputeAgent extends Agent {

	@Override
	protected void setup() {
		super.setup();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//search for master agent
		AID masterAgent = searchMasterAgent();
		
		if (masterAgent==null){
			System.out.println("Master Agent not found");
			return;
		}

		System.out.println("[ComputeAgent] Master Agent = " + masterAgent.getName());
		
	}
	
	private AID searchMasterAgent() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("master-agent");
		template.addServices(sd);
		
		DFAgentDescription[] result;
		try {
			result = DFService.search(this, template);
		} catch (FIPAException e) {
			e.printStackTrace();
			return null;
		}
		
		if (result.length == 0){
			return null;
		}
		return result[0].getName();
	}
	
}
