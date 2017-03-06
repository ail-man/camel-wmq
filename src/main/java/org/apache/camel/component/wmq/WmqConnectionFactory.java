package org.apache.camel.component.wmq;

import javax.jms.JMSException;

import com.ibm.mq.jms.MQConnectionFactory;

public class WmqConnectionFactory extends MQConnectionFactory {

	public WmqConnectionFactory(String hostname, Integer port, String queueManager, String channel) {
		try {
			setHostName(hostname);
			setPort(port);
			setQueueManager(queueManager);
			setChannel(channel);
			setTransportType(1);
		} catch (JMSException e) {
			throw new RuntimeException("Cannot create connection factory", e);
		}
	}
}
