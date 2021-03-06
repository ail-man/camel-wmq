package org.apache.camel.component.wmq;

import org.apache.camel.Endpoint;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;

import javax.jms.ConnectionFactory;
import java.util.Map;

public class WmqComponent extends JmsComponent {

    private final String queueManager;
    private final String hostname;
    private final String channel;
    private final Integer port;
    private boolean excludeRFHeaders;

    public WmqComponent() {
        this(null, null, null, null);
    }

    public WmqComponent(String hostname, Integer port, String queueManager, String channel) {
        super(WmqEndpoint.class);
        this.queueManager = queueManager;
        this.hostname = hostname;
        this.channel = channel;
        this.port = port;
    }

    public static WmqComponent newWmqComponent(String hostname, Integer port, String queueManager, String channel) {
        return new WmqComponent(hostname, port, queueManager, channel);
    }

    public WmqComponent excludeRFHeaders() {
        excludeRFHeaders = true;
        return this;
    }

    @Override
    protected JmsConfiguration createConfiguration() {
        JmsConfiguration configuration = new JmsConfiguration();
        configuration.setConnectionFactory(new WmqConnectionFactory(hostname, port, queueManager, channel));
        configuration.setDestinationResolver(new WmqDestinationResolver(excludeRFHeaders));
        return configuration;
    }

    @Override
    public Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        WmqEndpoint endpoint = new WmqEndpoint(this, uri, remaining);
        JmsConfiguration configuration = endpoint.getConfiguration();
        String username = this.getAndRemoveParameter(parameters, "username", String.class);
        String password = this.getAndRemoveParameter(parameters, "password", String.class);
        configuration.setConnectionFactory(createUserCredentialsConnectionFactoryAdapter(configuration.getConnectionFactory(), username, password));
        return endpoint;
    }

    private UserCredentialsConnectionFactoryAdapter createUserCredentialsConnectionFactoryAdapter(ConnectionFactory connectionFactory, String username, String password) {
        UserCredentialsConnectionFactoryAdapter adapter = new UserCredentialsConnectionFactoryAdapter();
        if (username != null && password != null) {
            adapter.setTargetConnectionFactory(connectionFactory);
            adapter.setPassword(password);
            adapter.setUsername(username);
        } else {
            throw new IllegalArgumentException("The JmsComponent\'s username or password is null");
        }
        return adapter;
    }
}
