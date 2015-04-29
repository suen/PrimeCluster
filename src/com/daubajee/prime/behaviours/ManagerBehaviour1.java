package com.daubajee.prime.behaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.util.LinkedList;

import org.json.JSONObject;

import com.daubajee.prime.Pmessage;

public class ManagerBehaviour1 extends Behaviour {

	private MessageTemplate msgTemplate;
	private LinkedList<ACLMessage> msgQueue = new LinkedList<ACLMessage>();
	
	public ManagerBehaviour1() {
		msgTemplate = MessageTemplate.MatchConversationId("by-MasterAgent");
	}
	
	@Override
	public void action() {
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
		
		if (message.getPerformative() == Pmessage.CREATE_COMPUTE_AGENT){
			AID sender = message.getSender();
			String contentStr = message.getContent().toString();
			//JSONObject contentjson = new JSONObject(contentStr);
			
			//String unique_id = contentjson.get("id").toString();
			String unique_id = String.valueOf(System.currentTimeMillis()) + "-compute";

			createComputeAgent(unique_id);
		}
		
	}
	
	
	private void createComputeAgent(String agentId){
	    AgentContainer container = myAgent.getContainerController();
	    try {

	    	AgentController a = container.createNewAgent(agentId,
	    			"com.daubajee.prime.ComputeAgent", null);
	    	a.start();
  	
	    	System.out.println("[ManagerAgent] A new compute agent created");
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
