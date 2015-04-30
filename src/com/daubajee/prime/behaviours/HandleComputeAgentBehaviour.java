package com.daubajee.prime.behaviours;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

import org.json.JSONObject;

import com.daubajee.prime.MasterAgent;
import com.daubajee.prime.Pmessage;
import com.daubajee.prime.algo.PrimeGenerator;

public class HandleComputeAgentBehaviour extends TickerBehaviour {
	
	private MessageTemplate msgTemplate;
	private LinkedList<ACLMessage> msgQueue = new LinkedList<ACLMessage>();
	private MasterAgent masterAgent;
	
	public HandleComputeAgentBehaviour(MasterAgent agent, long period) {
		super(agent, period);
		masterAgent= agent;
		msgTemplate = MessageTemplate.MatchConversationId("by-ComputeAgent");
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
		
		if (message.getPerformative() == Pmessage.REGISTER_COMPUTE_AGENT){
			AID sender = message.getSender();
			masterAgent.computeAgents.add(sender);
			
			System.out.println("[MASTER] " + masterAgent.computeAgents.size() +
					" compute Agents in the platform(s)");
//			testSum();
			distributePrimeFinder();

		}
		
		if (message.getPerformative()== Pmessage.RESULT){
			JSONObject contentJson = new JSONObject(message.getContent().toString());
			String result = contentJson.getString("result");
			System.out.println("[MASTER] Result from " + message.getSender().getName() + " : " + result);
			
		}
		
		if (message.getPerformative() == Pmessage.RESULT_PRIME){
			JSONObject contentJson = new JSONObject(message.getContent().toString());
			String result = contentJson.getString("result");
			System.out.println("[MASTER] Result from " + message.getSender().getName() + " : " + result);
			System.out.println("[MASTER] Stopping all compute Agents ");
			
			for(int i=0; i<masterAgent.computeAgents.size(); i++){
				testStopMsg(masterAgent.computeAgents.get(i));
			}
			
		}
	}
	
	private void testStopMsg(AID aid) {
		ACLMessage message = new ACLMessage(Pmessage.STOP_PRIME);
		message.setConversationId("by-MasterAgent");
		masterAgent.send(message);
	}

	private void distributePrimeFinder(){
		if (masterAgent.computeAgents.size()<11)
			return;

		int nbComptAgents = masterAgent.computeAgents.size();
		BigInteger[] startEnd = PrimeGenerator.randomBigIntsOfBitSize(1024);
		
		BigInteger diff = startEnd[1].subtract(startEnd[0]);
		BigInteger chunk = diff.divide(new BigInteger(String.valueOf(nbComptAgents)));
		
		
		BigInteger start = startEnd[0];
		BigInteger end;
		for(int i=0; i<nbComptAgents; i++){
			end = start.add(chunk);
			sendPrimeMessage(start, end, masterAgent.computeAgents.get(i));
			start = end;
		}
		System.out.println("[MASTER] Prime Generator distributed");
	}
	
	private void sendPrimeMessage(BigInteger start, BigInteger end, AID ca){
		JSONObject computeMessage = new JSONObject();
		computeMessage.put("startInterval", start.toString());
		computeMessage.put("endInterval", end.toString());
		
		ACLMessage message = new ACLMessage(Pmessage.COMPUTE_PRIME);
		message.addReceiver(ca);
		message.setContent(computeMessage.toString());
		message.setConversationId("by-MasterAgent");
		masterAgent.send(message);
	}
	
	private void testSum(){
	
		if (masterAgent.computeAgents.size()<11)
			return;

		int start = 0;
		int end = 0;
		Random rand = new Random();
		
		for(int i=0; i<masterAgent.computeAgents.size(); i++){
			
			end = start + rand.nextInt(100);

			testSumMsg(start,end, masterAgent.computeAgents.get(i));
			
			start = end;
		}
	}
	
	private void testSumMsg(int start, int end, AID ca){
		JSONObject computeMessage = new JSONObject();
		computeMessage.put("startInterval", String.valueOf(start));
		computeMessage.put("endInterval", String.valueOf(end));
		
		ACLMessage message = new ACLMessage(Pmessage.COMPUTE_SUM);
		message.addReceiver(ca);
		message.setContent(computeMessage.toString());
		message.setConversationId("by-MasterAgent");
		masterAgent.send(message);
	}
	
	


}
