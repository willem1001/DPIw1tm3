package applicationForms.Gateways;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NamingException;

public class ConnectionGateway {
    public static Connection getConnection(Context jndiContext) throws NamingException, JMSException {
        ActiveMQConnectionFactory connectionFactory = (ActiveMQConnectionFactory) jndiContext.lookup("ConnectionFactory");
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory.createConnection();
    }
}
