package com.daubajee.prime.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.math.BigInteger;
import java.util.LinkedList;

import org.json.JSONObject;

import com.daubajee.prime.ComputeAgent;
import com.daubajee.prime.Pmessage;

public class ComputeAgentBehaviour extends TickerBehaviour {

	private MessageTemplate msgTemplate;
	private LinkedList<ACLMessage> msgQueue = new LinkedList<ACLMessage>();
	private ComputeAgent agent;
	private TestPrimeBehaviour primeBehaviour = null;
	
	
	public ComputeAgentBehaviour(ComputeAgent agent, long period) {
		super(agent, period);

		this.agent = agent;
		
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
		
		if (message.getPerformative() == Pmessage.COMPUTE_SUM){
			
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
		
		if (message.getPerformative() == Pmessage.COMPUTE_PRIME){

			String contentStr = message.getContent().toString();
			JSONObject contentJson = new JSONObject(contentStr);
			
			String startInterval = contentJson.get("startInterval").toString();
			String endInterval = contentJson.get("endInterval").toString();
			
			BigInteger start = new BigInteger(startInterval);
			BigInteger end = new BigInteger(endInterval);
			
			System.out.println("[ComputeAgent] Compute received, start: [" + startInterval + "] end: [" + endInterval + "]");

			if(primeBehaviour == null){
				primeBehaviour = new TestPrimeBehaviour(agent, start, end);
				System.out.println("[ComputeAgent] Prime number generation started");
				myAgent.addBehaviour(primeBehaviour);
			}
			else {
				System.out.println("[ComputeAgent] Prime number generation ALREADY in progress");
			}
		}

		if (message.getPerformative() == Pmessage.STOP_PRIME ){
			if (primeBehaviour == null)
				return;
			primeBehaviour.stop();
			primeBehaviour = null;
			
			System.out.println("[ComputeAgent] Stopping TestPrime " + myAgent.getAID().getName());
		}
		
	}
	

}
