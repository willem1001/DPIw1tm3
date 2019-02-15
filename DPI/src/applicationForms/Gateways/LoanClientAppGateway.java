package applicationForms.Gateways;

import applicationForms.Gateways.Serializers.LoanSerializer;
import mix.model.loan.LoanReply;
import mix.model.loan.LoanRequest;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class LoanClientAppGateway {

    private MessageSenderGateway sender;
    private MessageReceiverGateway reciever;

    public LoanClientAppGateway() {
        sender = new MessageSenderGateway("ToClient");
       reciever = new MessageReceiverGateway("ToBrokerFromClient");
       reciever.setListner(message -> {
           TextMessage m = (TextMessage) message;
           try {
               LoanRequest loanRequest = LoanSerializer.requestFromString(m.getText());
               loanRequest.setMessageId(message.getJMSMessageID());
               onLoanRequestArrived(loanRequest);
           } catch (JMSException e) {
               e.printStackTrace();
           }
       });
    }

    public void sendLoanReply(LoanReply loanReply) {
        Message message = sender.createMessage(LoanSerializer.replyToString(loanReply));
        sender.send(message);
    }

    public void onLoanRequestArrived(LoanRequest loanRequest) { }
}

