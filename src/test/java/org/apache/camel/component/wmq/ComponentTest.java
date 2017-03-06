package org.apache.camel.component.wmq;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import static org.apache.camel.component.wmq.WmqComponent.newWmqComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;

public class ComponentTest extends CamelTestSupport {

	@Before
	public void init() throws Exception {
		System.setProperty("javax.net.ssl.keyStore", "src/test/resources/APP1.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "mint");
		System.setProperty("javax.net.ssl.trustStore", "src/test/resources/APP1.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "mint");

		System.setProperty("javax.net.debug", "all");
		System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "false");
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
	}

	@Override
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = super.createCamelContext();
		context.addComponent("wmq", newWmqComponent("mint", 1414, "MQTest", "HPT5.CLNT.WL", "TLS_RSA_WITH_AES_128_CBC_SHA256")
				.excludeRFHeaders());
		return context;
	}

	@Test
	public void send_and_receive() throws Exception {
		template().sendBodyAndHeader("wmq:MQTestQueue?username=mquser&password=mquser", "aMessage", "JMS_IBM_MQMD_ApplIdentityData", "anyIdData");

		Exchange received = consumer().receive("wmq:MQTestQueue?username=mquser&password=mquser");
		Message in = received.getIn();

		assertEquals("aMessage", in.getBody());
		assertStartsWith(in.getHeader("JMS_IBM_MQMD_ApplIdentityData").toString(), "anyIdData");
	}

	private void assertStartsWith(String string, String starts) {
		assertTrue(string.startsWith(starts));
	}

}
