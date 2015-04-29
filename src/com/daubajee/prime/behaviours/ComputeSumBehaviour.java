package com.daubajee.prime.behaviours;

import java.util.LinkedList;
import java.util.Random;

import com.daubajee.prime.ComputeAgent;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class ComputeSumBehaviour extends Behaviour {

	private LinkedList<Integer> numbers = new LinkedList<Integer>();
	private int start;
	private int end;
	private Random rand;
	private boolean done = false;
	private int sum = 0;
	
	public ComputeSumBehaviour(int start, int end) {
		this.start = start;
		this.end = end;
		rand = new Random();
	}
	
	@Override
	public void action() {
		int randInt = rand.nextInt(100);

		try {
			Thread.sleep(randInt);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (start < end) {
			sum += start;
			start++;
		} else {
			done = true;
			ComputeAgent computeAgent = (ComputeAgent)myAgent;
			computeAgent.sendResultToMaster(sum);
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return done;
	}

	
	
}
