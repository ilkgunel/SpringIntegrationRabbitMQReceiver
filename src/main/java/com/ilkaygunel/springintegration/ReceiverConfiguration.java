package com.ilkaygunel.springintegration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class ReceiverConfiguration {

	@Bean
	public MessageChannel orderChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel invoiceChannel() {
		return new DirectChannel();
	}

	@Bean
	public AmqpInboundChannelAdapter inbound(SimpleMessageListenerContainer listenerContainer,
			@Qualifier("orderChannel") MessageChannel channel) {
		AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(listenerContainer);
		adapter.setOutputChannel(channel);
		return adapter;
	}

	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
		container.setQueueNames("orders");
		return container;
	}
}
