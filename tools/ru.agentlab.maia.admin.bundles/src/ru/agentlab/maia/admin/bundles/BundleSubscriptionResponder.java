package ru.agentlab.maia.admin.bundles;

import static ru.agentlab.maia.FIPAPerformativeNames.AGREE;
import static ru.agentlab.maia.FIPAPerformativeNames.CANCEL;
import static ru.agentlab.maia.FIPAPerformativeNames.FAILURE;
import static ru.agentlab.maia.FIPAPerformativeNames.INFORM;
import static ru.agentlab.maia.FIPAPerformativeNames.REFUSE;
import static ru.agentlab.maia.FIPAPerformativeNames.SUBSCRIBE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import ru.agentlab.maia.IMessage;
import ru.agentlab.maia.annotation.belief.AddedBelief;
import ru.agentlab.maia.annotation.belief.AxiomType;
import ru.agentlab.maia.annotation.belief.HaveBelief;
import ru.agentlab.maia.annotation.belief.Prefix;
import ru.agentlab.maia.messaging.AclMessage;
import ru.agentlab.maia.messaging.IMessageDeliveryService;
import ru.agentlab.maia.plan.message.AddedMessage;

@Prefix(name = "osgi", namespace = "http://www.agentlab.ru/ontologies/osgi")
public class BundleSubscriptionResponder {

	private static final String FIPA_SUBSCRIBE = "FIPA_SUBSCRIBE";

	private Map<UUID, String> conversationIds = new HashMap<>();

	private Map<String, List<UUID>> subscriberProperties = new HashMap<>();

	@Inject
	UUID uuid;

	@Inject
	IMessageDeliveryService mts;

	@AddedMessage(performative = SUBSCRIBE, protocol = FIPA_SUBSCRIBE)
	public void onSubscribe(IMessage message) {
		UUID sender = message.getSender();
		String conversationId = message.getConversationId();
		conversationIds.put(sender, conversationId);
		String property = message.getContent();
		if (property == null) {
			reply(message, REFUSE);
		}
		List<UUID> receivers = subscriberProperties.get(property);
		if (receivers == null) {
			receivers = new ArrayList<>();
			subscriberProperties.put(property, receivers);
		}
		receivers.add(sender);
		reply(message, AGREE);
	}

	@AddedMessage(performative = CANCEL, protocol = FIPA_SUBSCRIBE)
	public void onCancel(IMessage message) {
		UUID sender = message.getSender();
		if (conversationIds.get(sender) == message.getConversationId()) {
			conversationIds.remove(sender);
			reply(message, INFORM);
		} else {
			reply(message, FAILURE);
		}
	}

	@AddedBelief(value = { "?bundle", "?property", "?value" }, type = AxiomType.DATA_PROPERTY_ASSERTION)
	@HaveBelief(value = { "?bundle", "osgi:Bundle" }, type = AxiomType.CLASS_ASSERTION)
	public void onPropertyChanged(@Named("bundle") String bundle, @Named("property") String property,
			@Named("value") String value) {
		IMessage message = new AclMessage();
		message.setPerformative(INFORM);
		List<UUID> receivers = subscriberProperties.get(property);
		message.setReceivers(receivers);
		message.setSender(uuid);
		message.setContent(bundle + " " + property + " " + value);
		mts.send(message);
	}

	private void reply(IMessage message, String performative) {
		IMessage reply = message.createReply();
		reply.setPerformative(performative);
		mts.send(reply);
	}

}