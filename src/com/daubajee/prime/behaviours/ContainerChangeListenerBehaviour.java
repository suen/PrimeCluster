package com.daubajee.prime.behaviours;

import java.util.Map;

import com.daubajee.prime.MasterAgent;

import jade.core.ContainerID;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.AddedContainer;
import jade.domain.introspection.Event;
import jade.domain.introspection.RemovedContainer;

public class ContainerChangeListenerBehaviour extends AMSSubscriber {

	public ContainerChangeListenerBehaviour() {
		
	}

	@Override
	protected void installHandlers(Map map) {
		map.put(AddedContainer.NAME, new ContainerChangeHandler());		
		map.put(RemovedContainer.NAME, new ContainerChangeHandler());		
	}

	public final class ContainerChangeHandler implements EventHandler {
		@Override
		public void handle(Event ev) {
		    System.out.println("[MASTER AGENT] ::: " + ev.getName());

		    if (ev.getName().equals("Added-Container")){
		    	
			    AddedContainer event = (AddedContainer) ev;
			    ContainerID addedContainer = event.getContainer();
			    
			    if (addedContainer.getName().contains("Main-Container"))
			    	return;
			    
			    MasterAgent masterAgent = (MasterAgent) myAgent;
			    

			    System.out.println("[MASTER AGENT] ::: " + addedContainer.getID());
		    } else {
			    RemovedContainer event = (RemovedContainer) ev;
			    ContainerID removedContainer = event.getContainer();
			    MasterAgent masterAgent = (MasterAgent) myAgent;
			    

			    System.out.println("[MASTER AGENT] ::: " + removedContainer.getID());
		    }
		}
	}
	
}
