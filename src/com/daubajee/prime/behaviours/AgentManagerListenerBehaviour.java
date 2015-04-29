package com.daubajee.prime.behaviours;

import java.util.LinkedList;

import com.daubajee.prime.MasterAgent;
import com.daubajee.prime.Pmessage;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentManagerListenerBehaviour extends Behaviour {

	MessageTemplate msgTemplate;
	MasterAgent agent;
	
	private LinkedList<ACLMessage> msgQueue = new LinkedList<ACLMessage>();
	
	public AgentManagerListenerBehaviour(MasterAgent agent) {
		this.agent = agent;
		msgTemplate = MessageTemplate.MatchConversationId("by-ComputeAgentManager");
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
		
		if (message.getPerformative() == Pmessage.REGISTER_MANAGER){
			AID sender = message.getSender();
			agent.managerAgents.add(sender);
		}
		
	}

	@Override
	public boolean done() {
		return false;
	}

	
	
}
