package org.apache.camel.component.wmq;

import javax.jms.JMSException;

import com.ibm.mq.jms.MQConnectionFactory;

public class WmqConnectionFactory extends MQConnectionFactory {

	public WmqConnectionFactory(String hostname, Integer port, String queueManager, String channel, String cipher) {
		try {
			setHostName(hostname);
			setPort(port);
			setQueueManager(queueManager);
			setChannel(channel);
			setTransportType(1);
			if (cipher != null) {
				setSSLCipherSuite(cipher);
			}
		} catch (JMSException e) {
			throw new RuntimeException("Cannot create connection factory", e);
		}
	}
}
