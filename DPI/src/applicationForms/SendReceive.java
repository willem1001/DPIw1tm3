package applicationForms;

import mix.model.loan.LoanRequest;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.Properties;

public class SendReceive {
    public static String sendMessage(Serializable object, String queueName) {
        Connection connection; // to connect to the ActiveMQ
        Session session; // session for creating messages, producers and

        Destination sendDestination; // reference to a queue/topic destination
        MessageProducer producer; // for sending messages

        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            // connect to the Destination called “myFirstChannel”
            // queue or topic: “queue.myFirstDestination” or “topic.myFirstDestination”
            props.put(("queue." + queueName), queueName);

            Context jndiContext = new InitialContext(props);
            connection = getConnection(jndiContext);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // connect to the sender destination
            sendDestination = (Destination) jndiContext.lookup(queueName);
            producer = session.createProducer(sendDestination);

            ObjectMessage objMessage = session.createObjectMessage(); //or serialize an object!
            objMessage.setObject(object);
            // send the message
            producer.send(objMessage);
            return objMessage.getJMSMessageID();

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void receive(String queueName, MessageListener listener) {
        Connection connection; // to connect to the JMS
        Session session; // session for creating consumers

        Destination receiveDestination; //reference to a queue/topic destination
        MessageConsumer consumer = null; // for receiving messages

        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            // connect to the Destination called “myFirstChannel”
            // queue or topic: “queue.myFirstDestination” or “topic.myFirstDestination”
            props.put(("queue." + queueName), queueName);

            Context jndiContext = new InitialContext(props);
            connection = getConnection(jndiContext);
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // connect to the receiver destination
            receiveDestination = (Destination) jndiContext.lookup(queueName);
            consumer = session.createConsumer(receiveDestination);
            connection.start(); // this is needed to start receiving messages

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
        try {
            if (consumer != null) {
                consumer.setMessageListener(listener);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    private static Connection getConnection(Context jndiContext) throws NamingException, JMSException {
        ActiveMQConnectionFactory connectionFactory = (ActiveMQConnectionFactory) jndiContext.lookup("ConnectionFactory");
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory.createConnection();
    }
}
