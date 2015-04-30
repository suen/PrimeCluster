package com.daubajee.prime.behaviours;

import java.math.BigInteger;

import com.daubajee.prime.ComputeAgent;
import com.daubajee.prime.algo.PrimeGenerator;

import jade.core.behaviours.Behaviour;

public class TestPrimeBehaviour extends Behaviour{

	private BigInteger start;
	private BigInteger end;
	private boolean done;
	private ComputeAgent agent;
	private boolean stop = false;
	
	public TestPrimeBehaviour(ComputeAgent agent, BigInteger start, BigInteger end){
		this.agent = agent;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void action() {
		BigInteger rand = PrimeGenerator.randomBigIntBetween(start, end);
		done = PrimeGenerator.testPrime(rand);
		
		if (done){
			agent.sendResultToMaster(rand);
		}
	}

	public void stop(){
		this.stop = true;
	}
	
	@Override
	public boolean done() {
		return done || stop;
	}

}
