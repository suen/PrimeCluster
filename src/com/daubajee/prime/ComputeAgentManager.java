package com.daubajee.prime;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import com.daubajee.prime.behaviours.ManagerBehaviour1;

public class ComputeAgentManager extends Agent {
	
	@Override
	protected void setup() {
		super.setup();
		
		registerWithMaster();
		
		addBehaviour(new ManagerBehaviour1());
		
		System.out.println("Manager created");
	}
	
	private void registerWithMaster() {
		
		AID master = searchMasterAgent();

		ACLMessage message = new ACLMessage(Pmessage.REGISTER_MANAGER);
		message.addReceiver(master );
		message.setConversationId("by-ComputeAgentManager");
		send(message);
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
