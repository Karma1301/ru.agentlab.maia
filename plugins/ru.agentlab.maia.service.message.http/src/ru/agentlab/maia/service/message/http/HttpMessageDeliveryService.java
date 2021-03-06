package ru.agentlab.maia.service.message.http;

import ru.agentlab.maia.service.message.INetworkProtocol;
import ru.agentlab.maia.service.message.impl.MessageDeliveryService;

public class HttpMessageDeliveryService extends MessageDeliveryService {

	private int port = 8080;

	@Override
	public INetworkProtocol getNetworkProtocol() {
		return new HttpNetworkProtocol();
	}

	@Override
	public void startServer() {
		serverBootstrap.bind(port);

	}

}
