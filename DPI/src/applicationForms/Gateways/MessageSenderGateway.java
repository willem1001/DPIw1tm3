package applicationForms.Gateways;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.Properties;

public class MessageSenderGateway {

    private Connection connection;
    private Session session;

    private Destination sendDestination;
    private MessageProducer producer;

    public MessageSenderGateway(String queueName) {
        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            props.put(("queue." + queueName), queueName);

            Context jndiContext = new InitialContext(props);
            connection = ConnectionGateway.getConnection(jndiContext);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            sendDestination = (Destination) jndiContext.lookup(queueName);
            producer = session.createProducer(sendDestination);
        }catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    public String send(Message message) {
        try {
            producer.send(message);
            return message.getJMSMessageID();
        } catch (JMSException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message createMessage(String message) {
        try {
           Message m = session.createTextMessage(message);
            return m;
        } catch (JMSException e) {
            e.printStackTrace();
            return null;
        }
    }
}
