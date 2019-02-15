package applicationForms.Gateways;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

import static applicationForms.Gateways.ConnectionGateway.getConnection;

public class MessageReceiverGateway {

    private Connection connection;
    private Session session;
    private Destination receiveDestination;
    private MessageConsumer consumer = null;

    public MessageReceiverGateway(String queueName) {

        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            props.put(("queue." + queueName), queueName);
            Context jndiContext = new InitialContext(props);
            connection = getConnection(jndiContext);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            receiveDestination = (Destination) jndiContext.lookup(queueName);
            consumer = session.createConsumer(receiveDestination);
            connection.start();

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    public void setListner(MessageListener listener) {
        try {
            if (consumer != null) {
                consumer.setMessageListener(listener);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
