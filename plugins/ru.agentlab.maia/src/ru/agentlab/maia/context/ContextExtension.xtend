package ru.agentlab.maia.context

import org.eclipse.e4.core.contexts.IEclipseContext
import org.slf4j.Logger

/**
 * Extension with util methods for add services and properties to context.
 * 
 * @author <a href='shishkin_dimon@gmail.com'>Shishkin Dmitriy</a> - Initial contribution.
 */
class ContextExtension {

	Logger logger

	new(Logger logger) {
		this.logger = logger
	}

	def void addContextProperty(IEclipseContext context, String key, Object value) {
		logger.debug("	Put Property [{}] with vale [{}]  to context...", key, value)
		context.set(key, value)
	}

	def <T> void addContextService(IEclipseContext context, Class<T> key, T value) {
		logger.debug("	Put Service [{}] with vale [{}]  to context...", key.name, value)
		context.set(key, value)
	}

}