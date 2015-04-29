package com.daubajee.prime.behaviours;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.ContainerID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.daubajee.prime.MasterAgent;
import com.daubajee.prime.Pmessage;

public class HandleAgentManagerBehaviour extends TickerBehaviour {

	MessageTemplate msgTemplate;
	private MasterAgent agent;
	
	private LinkedList<ACLMessage> msgQueue = new LinkedList<ACLMessage>();
	
	private Map<AID, Integer> computeAgentCount = new HashMap<AID, Integer>();
	
	
	public HandleAgentManagerBehaviour(MasterAgent agent, long period) {
		super(agent, period);
		this.agent = agent;
		msgTemplate = MessageTemplate.MatchConversationId("by-ComputeAgentManager");
	}

	@Override
	protected void onTick() {
		checkMessage();
		treatMessageQueue();
		createComputeAgent();
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
	
	private void createComputeAgent() {
		if (agent.managerAgents.size() == 0) {
			System.out.println("[MASTER] : No agent managers");
			return;
		}
		
		for (AID amanager: agent.managerAgents){
			if (!computeAgentCount.containsKey(amanager))
				computeAgentCount.put(amanager, 0);
			
			Integer count = computeAgentCount.get(amanager);
			if (count > 10 )
				return;
			
			sendCreateComputeAgentMessage(amanager);
			computeAgentCount.put(amanager, count + 1);
		}
	}
	
	
	private void sendCreateComputeAgentMessage(AID manager){
		String unique_id = String.valueOf(System.currentTimeMillis()) + "-compute";
		
		ACLMessage message = new ACLMessage(Pmessage.CREATE_COMPUTE_AGENT);
		message.addReceiver(manager);

		JSONObject msgContent = new JSONObject();
		msgContent.put("id", unique_id);

		message.setContent(msgContent.toString());
		message.setConversationId("by-MasterAgent");
		agent.send(message);
	}
	
	
	
	
	
	
	
	
	
	private void queryAMS() throws CodecException, OntologyException {
	    QueryPlatformLocationsAction query = new QueryPlatformLocationsAction();
	    Action action = new Action(myAgent.getAID(), query);

	    ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
	    message.addReceiver(myAgent.getAMS());
	    message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
	    message.setOntology(JADEManagementOntology.getInstance().getName());
	    myAgent.getContentManager().fillContent(message, action);
	    myAgent.send(message);
	}

	private void listenForAMSReply() throws UngroundedException, CodecException, 
	OntologyException {
	    ACLMessage receivedMessage = myAgent.blockingReceive(MessageTemplate
	            .MatchSender(myAgent.getAMS()));
	    ContentElement content = myAgent.getContentManager().extractContent(
	        receivedMessage);

	    // received message is a Result object, whose Value field is a List of
	    // ContainerIDs
	    Result result = (Result) content;
	    List listOfPlatforms = (List) result.getValue();

	    // use it
	    Iterator iter = listOfPlatforms.iterator();
	    while (iter.hasNext()) {
	        ContainerID next = (ContainerID) iter.next();
	        System.out.println(next.getID());
	    }
	}

	

}
