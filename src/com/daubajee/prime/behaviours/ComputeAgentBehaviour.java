package com.daubajee.prime.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.LinkedList;

import org.json.JSONObject;

import com.daubajee.prime.Pmessage;

public class ComputeAgentBehaviour extends TickerBehaviour {

	private MessageTemplate msgTemplate;
	private LinkedList<ACLMessage> msgQueue = new LinkedList<ACLMessage>();

	
	public ComputeAgentBehaviour(Agent agent, long period) {
		super(agent, period);

		msgTemplate = MessageTemplate.MatchConversationId("by-MasterAgent");		
	}

	@Override
	protected void onTick() {
		checkMessage();
		treatMessageQueue();
	}
	
	private void checkMessage(){
		ACLMessage message = myAgent.receive(msgTemplate);
		
		if (message!=null){
			msgQueue.add(message);
		}
		
	}
	
	private void treatMessageQueue(){
		if (msgQueue.size()==0)
			return;
		
		ACLMessage message = msgQueue.pop();
		
		if (message.getPerformative() == Pmessage.COMPUTE){
			
			String contentStr = message.getContent().toString();
			JSONObject contentJson = new JSONObject(contentStr);
			
			String startInterval = contentJson.get("startInterval").toString();
			String endInterval = contentJson.get("endInterval").toString();
			
			int start = Integer.valueOf(startInterval);
			int end = Integer.valueOf(endInterval);
			
			System.out.println("[ComputeAgent] Compute received, start: " + startInterval + " end: " + endInterval);

			myAgent.addBehaviour(new ComputeSumBehaviour(start, end));
			System.out.println("[ComputeAgent] ComputeSumBehaviour added");
			
		}

	}
	

}
