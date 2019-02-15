package applicationForms.Gateways;

import applicationForms.Gateways.Serializers.BankSerializer;
import mix.model.bank.BankInterestReply;
import mix.model.bank.BankInterestRequest;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class BankAppGateway {

    private MessageSenderGateway sender;
    private MessageReceiverGateway reciever;

    public BankAppGateway() {
        sender = new MessageSenderGateway("ToBank");
        reciever = new MessageReceiverGateway("ToBrokerFromBank");
        reciever.setListner(message -> {
            TextMessage m = (TextMessage) message;
            try {
                BankInterestReply bankInterestReply = BankSerializer.replyFromString(m.getText());
                onBankReplyArrived(bankInterestReply);
            }catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    public void requestBankInterest(BankInterestRequest bankInterestRequest) {
        Message message = sender.createMessage(BankSerializer.requestToString(bankInterestRequest));
        sender.send(message);
    }

    public void onBankReplyArrived(BankInterestReply bankInterestReply) {

    }
}
