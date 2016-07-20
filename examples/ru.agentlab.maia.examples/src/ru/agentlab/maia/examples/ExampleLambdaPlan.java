package ru.agentlab.maia.examples;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ru.agentlab.maia.IPlan;
import ru.agentlab.maia.IPlanBase;
import ru.agentlab.maia.agent.Plan;
import ru.agentlab.maia.event.BeliefAddedEvent;

public class ExampleLambdaPlan {

	@Inject
	IPlanBase planBase;

	@Inject
	Object service;

	@PostConstruct
	public void setup() {
		IPlan plan = new Plan(this, () -> {
			System.out.println(service);
			System.out.println(service.hashCode());
		});
		planBase.add(BeliefAddedEvent.class, plan);
	}

}