package ru.agentlab.maia.launcher

import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext
import org.slf4j.LoggerFactory
import ru.agentlab.maia.context.IMaiaContext
import ru.agentlab.maia.context.IMaiaContextFactory
import ru.agentlab.maia.context.initializer.IMaiaContextInitializerService
import ru.agentlab.maia.context.typing.agent.MaiaAgentContextInitializer
import ru.agentlab.maia.context.typing.behaviour.IMaiaBehaviourContextFactory
import ru.agentlab.maia.context.typing.container.MaiaContainerContextInitializer
import ru.agentlab.maia.execution.lifecycle.IMaiaContextLifecycleService
import ru.agentlab.maia.execution.lifecycle.fipa.FipaLifecycleScheme
import ru.agentlab.maia.context.typing.behaviour.MaiaBehaviourContextInitializer

class Activator implements BundleActivator {

	val static LOGGER = LoggerFactory.getLogger(Activator)

	static BundleContext context

	def static package BundleContext getContext() {
		return context
	}

	def IMaiaContext createContainer(IMaiaContext parent) {
		return parent.get(IMaiaContextFactory).createContext => [
			get(IMaiaContextInitializerService).initService(MaiaContainerContextInitializer)
		]
	}

	def IMaiaContext createAgent(IMaiaContext parent) {
		return parent.get(IMaiaContextFactory).createContext => [
			get(IMaiaContextInitializerService).initService(MaiaAgentContextInitializer)
		]
	}

	def IMaiaContext createBehaviour(IMaiaContext parent) {
		return parent.get(IMaiaContextFactory).createContext => [
			get(IMaiaContextInitializerService).initService(MaiaBehaviourContextInitializer)
		]
	}

	/*
	 * (non-Javadoc)
	 * @see BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	override void start(BundleContext ctx) throws Exception {
		context = ctx
		val rootContextRef = context.getServiceReference(IMaiaContext)
		val rootContext = context.getService(rootContextRef)

		LOGGER.info("CREATE CONTAINER...")
		val container = rootContext.createContainer
		val container2 = rootContext.createContainer
		val container3 = rootContext.createContainer

		LOGGER.info("CREATE AGENT...")
		val agent = container.createAgent => [
			get(IMaiaContextInitializerService).initService(AgentExample)
		]
		container.createAgent
		container.createAgent
		container.createAgent
		container.createAgent

//		LOGGER.info("CREATE BEHAVIOUR...")
//		val behaviour = agent.createBehaviour
//		agent.createBehaviour
//		agent.createBehaviour
//		agent.createBehaviour
//		LOGGER.info("CREATE BEHAVIOUR_2...")
//		val behaviour2 = agent.createBehaviour

//		agent => [
//			get(IMaiaContextLifecycleService).state = FipaLifecycleScheme.STATE_ACTIVE
//
//		]
//		LOGGER.info("CREATE BEHAVIOUR2...")
//		val behaviour2 = agent.get(IMaiaBehaviourContextFactory).createBehaviour(null)
//		LOGGER.info(behaviour.dump)
//
//		LOGGER.info("CREATE CONTAINER FACTORY...")
//		val containerFactory = serviceManager.createService(osgiContext, IContainerFactory)
//
//
//		LOGGER.info("CREATE AGENT...")
//		val agent = agentFactory.createAgent(container)
//		LOGGER.info(agent.dump)
//		agent.get(IMaiaContextInitializerService).addInitializer(agent, AgentExample)
//		LOGGER.info(agent.dump)
//		platformFactory.createDefault(null) => [
//			get(IContainerFactory) => [
//				LOGGER.info("CREATE container1")
//				createDefault(null) => [
//					get(IAgentFactory) => [
//						LOGGER.info("CREATE agent1")
//						createDefault(null) => [
//							LOGGER.info("ADD contributor for agent1")
//							get(IInitializerService).addInitializer(AgentExample)
//							LOGGER.info("INVOKE agent1")
//							get(ILifecycleService).invokeTransition = "INVOKE"
//						]
//					]
//				]
//				LOGGER.info("CREATE container2")
//				createDefault("Container2")
//			]
//		]
//		LOGGER.info("CREATE agent2")
//		val agent2 = agentFactory.createDefault(null, "Agent2")
//		LOGGER.info("INVOKE agent2")
//		agentLyfecycleService.invoke(agent2)
//		Thread.sleep(5000)
//		
//		LOGGER.info("Change Scheduler")
//		val sch = schedulerFactory.create(agent1)
//		val port = Integer.parseInt(System.getProperty("port", "8899"))
//		if(port == 8888){
//			val behaviour = behaviourFactory.createOneShot(agent1, "beh")
//			contributionService.addContributor(behaviour, BehaviourExample2)
//		}
//		
//		Thread.sleep(3000)
//		LOGGER.info("SUSPEND agent1")
//		agentLyfecycleService.suspend(agent1)
//
//		Thread.sleep(3000)
//		LOGGER.info("RESUME agent1")
//		agentLyfecycleService.resume(agent1)
//
//		Thread.sleep(3000)
//		LOGGER.info("SUSPEND agent2")
//		agentLyfecycleService.suspend(agent2)
//
//		Thread.sleep(3000)
//		LOGGER.info("RESUME agent2")
//		agentLyfecycleService.resume(agent2)
	}

	/*
	 * (non-Javadoc)
	 * @see BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	override void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null
	}

}
