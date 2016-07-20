package ru.agentlab.maia.agent;

import static ru.agentlab.maia.annotation.belief.AxiomType.CLASS_ASSERTION;
import static ru.agentlab.maia.annotation.belief.AxiomType.DATA_PROPERTY_ASSERTION;
import static ru.agentlab.maia.annotation.belief.AxiomType.SUBCLASS_OF;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.vocab.Namespaces;

import de.derivo.sparqldlapi.QueryResult;
import ru.agentlab.maia.IAgent;
import ru.agentlab.maia.IContainer;
import ru.agentlab.maia.IEvent;
import ru.agentlab.maia.annotation.belief.OnBeliefAdded;
import ru.agentlab.maia.annotation.belief.WhenHaveBelief;
import ru.agentlab.maia.annotation.belief.InitialBelief;
import ru.agentlab.maia.container.Container;
import ru.agentlab.maia.event.BeliefAddedEvent;
import ru.agentlab.maia.exception.ContainerException;
import ru.agentlab.maia.exception.InjectorException;
import ru.agentlab.maia.exception.ResolveException;

public class Agent_AcceptanceTest {

	private static OWLOntologyManager manager = OWLManager.createConcurrentOWLOntologyManager();

	private static OWLDataFactory factory = manager.getOWLDataFactory();

	public static void main(String[] args)
			throws InjectorException, ContainerException, ResolveException, InterruptedException {
		IContainer container = new Container();
		container.put(ForkJoinPool.class,
				new ForkJoinPool(4, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true));
		container.put(String.class, "TEST");
		container.put(OWLOntologyManager.class, manager);
		container.put(OWLDataFactory.class, factory);
		List<Agent> agents = new ArrayList<>();

		for (int j = 0; j < 10; j++) {
			Agent agent = new Agent();
			agents.add(agent);
			agent.deployTo(container);
			agent.addRole(TestRole.class);
			IntStream.range(0, 500_000).forEach(i -> {
				IEvent<?> event = new BeliefAddedEvent(factory.getOWLDataPropertyAssertionAxiom(
						factory.getOWLDataProperty(IRI.create(Namespaces.RDF.toString(), "hasProperty")),
						factory.getOWLNamedIndividual(IRI.create(Namespaces.RDF.toString(), "ind")),
						factory.getOWLLiteral(2)));
				agent.eventQueue.offer(event);
			});
			// System.out.println(agent.eventQueue);
		}

		System.out.println("START AGENTS");
		for (IAgent agent : agents) {
			agent.start();
		}
		Thread.sleep(50_000);
		for (IAgent agent : agents) {
			agent.stop();
		}
		for (Agent agent : agents) {
			System.err.println();
			System.err.println(agent.uuid);
			System.err.println(agent.state);
			System.err.println(agent.i.get());
		}
		System.err.println(agents.stream().mapToInt(a -> a.i.get()).sum());

	}

	@InitialBelief(type = DATA_PROPERTY_ASSERTION, value = { "rdf:ind", "rdf:hasProperty", "2^^xsd:integer" })
	@InitialBelief(type = SUBCLASS_OF, value = { "rdf:Some", "owl:Thing" })
	@InitialBelief(type = CLASS_ASSERTION, value = { "rdf:Some", "rdf:ind" })
	public static class TestRole {

		@PostConstruct
		public void init(IAgent agent) {
			System.out.println(agent.getUuid());
		}

		@OnBeliefAdded(type = DATA_PROPERTY_ASSERTION, value = { "rdf:ind", "rdf:hasProperty", "2^^xsd:integer" })
		@WhenHaveBelief(type = SUBCLASS_OF, value = { "rdf:Some", "owl:Thing" })
		@WhenHaveBelief(type = CLASS_ASSERTION, value = { "rdf:ind", "rdf:Some" })
		public void exe() {
			System.out.println("WORKS");

		}

		@OnBeliefAdded(type = DATA_PROPERTY_ASSERTION, value = { "?ind", "?property", "2^^xsd:integer" })
		@WhenHaveBelief(type = SUBCLASS_OF, value = { "rdf:Some", "owl:Thing" })
		@WhenHaveBelief(type = CLASS_ASSERTION, value = { "rdf:ind", "?ind" })
		public void exe2(@Named("property") OWLDataProperty property, @Named("ind") OWLIndividual ind,
				QueryResult res) {
			System.out.println("WORKS2" + property.toString());
			System.out.println("WORKS2" + ind.toString());

		}

	}
}