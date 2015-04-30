package com.daubajee.prime;

import java.math.BigInteger;

import org.json.JSONObject;

import com.daubajee.prime.behaviours.ComputeAgentBehaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class ComputeAgent extends Agent {

	public AID masterAgentAID = null;
	
	
	@Override
	protected void setup() {
		super.setup();
		registerWithMaster();
		
		addBehaviour(new ComputeAgentBehaviour(this, 100));

	}
	
	private void test(){
		masterAgentAID = searchMasterAgent();
		
		if (masterAgentAID==null){
			System.out.println("[ComputeAgent] Master Agent not found");
			return;
		}
		System.out.println("[ComputeAgent] Master Agent = " + masterAgentAID.getName());
	}
	
	private void registerWithMaster() {
		AID master = searchMasterAgent();
		ACLMessage message = new ACLMessage(Pmessage.REGISTER_COMPUTE_AGENT);
		message.addReceiver(master );
		message.setConversationId("by-ComputeAgent");
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
	
	public void sendResultToMaster(int result){
		AID master = searchMasterAgent();
		ACLMessage message = new ACLMessage(Pmessage.RESULT);
		
		JSONObject resultjson = new JSONObject();
		resultjson.put("result", String.valueOf(result));
		
		message.setContent(resultjson.toString());
		
		message.addReceiver(master);
		message.setConversationId("by-ComputeAgent");
		send(message);
		
		System.out.println("[COMPUTE] Result sent to master: " + result);
	}
	
	public void sendResultToMaster(BigInteger result){
		AID master = searchMasterAgent();
		ACLMessage message = new ACLMessage(Pmessage.RESULT_PRIME);
		
		JSONObject resultjson = new JSONObject();
		resultjson.put("result", result.toString());
		
		message.setContent(resultjson.toString());
		
		message.addReceiver(master);
		message.setConversationId("by-ComputeAgent");
		send(message);
		
		System.out.println("[COMPUTE] Result sent to master: " + result);
	}
	
}
