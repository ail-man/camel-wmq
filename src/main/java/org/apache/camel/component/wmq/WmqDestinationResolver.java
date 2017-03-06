package org.apache.camel.component.wmq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import com.ibm.mq.jms.MQDestination;
import static com.ibm.msg.client.wmq.common.CommonConstants.WMQ_MDCTX_SET_ALL_CONTEXT;
import static com.ibm.msg.client.wmq.common.CommonConstants.WMQ_MQMD_MESSAGE_CONTEXT;
import static com.ibm.msg.client.wmq.common.CommonConstants.WMQ_MQMD_READ_ENABLED;
import static com.ibm.msg.client.wmq.common.CommonConstants.WMQ_MQMD_WRITE_ENABLED;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

public class WmqDestinationResolver extends DynamicDestinationResolver {

	private final boolean excludeRFHeaders;

	public WmqDestinationResolver(boolean excludeRFHeaders) {
		this.excludeRFHeaders = excludeRFHeaders;
	}

	public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
		MQDestination destination = (MQDestination) super.resolveDestinationName(session, destinationName, pubSubDomain);
		destination.setBooleanProperty(WMQ_MQMD_WRITE_ENABLED, true);
		destination.setBooleanProperty(WMQ_MQMD_READ_ENABLED, true);
		destination.setIntProperty(WMQ_MQMD_MESSAGE_CONTEXT, WMQ_MDCTX_SET_ALL_CONTEXT);
		if (excludeRFHeaders) {
			destination.setTargetClient(1);
		}
		return destination;
	}

}
