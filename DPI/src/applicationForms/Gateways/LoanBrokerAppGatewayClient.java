package applicationForms.Gateways;

import applicationForms.Gateways.Serializers.LoanSerializer;
import mix.model.loan.LoanReply;
import mix.model.loan.LoanRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class LoanBrokerAppGatewayClient {

    private MessageSenderGateway loanSender;
    private MessageReceiverGateway loanReceiver;

    public LoanBrokerAppGatewayClient() {
        loanSender = new MessageSenderGateway("ToBrokerFromClient");
        loanReceiver = new MessageReceiverGateway("ToClient");
        loanReceiver.setListner(message -> {
            TextMessage m = (TextMessage) message;
            try {
                LoanReply reply = LoanSerializer.replyFromString(m.getText());
                onLoanReplyArrived(reply);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    public String applyForLoan(LoanRequest loanRequest) {
        Message message = loanSender.createMessage(LoanSerializer.requestToString(loanRequest));
        return loanSender.send(message);
    }

    public void onLoanReplyArrived(LoanReply loanReply) {}
}
