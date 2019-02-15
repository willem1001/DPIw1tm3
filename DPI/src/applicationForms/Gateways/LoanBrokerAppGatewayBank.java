package applicationForms.Gateways;

import applicationForms.Gateways.Serializers.BankSerializer;
import mix.model.bank.BankInterestReply;
import mix.model.bank.BankInterestRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class LoanBrokerAppGatewayBank {
    private MessageSenderGateway bankSender;
    private MessageReceiverGateway bankReciever;

    public LoanBrokerAppGatewayBank () {
    bankSender = new MessageSenderGateway("ToBrokerFromBank");
    bankReciever = new MessageReceiverGateway("ToBank");
      bankReciever.setListner(message -> {
          TextMessage m = (TextMessage) message;
          try {
              BankInterestRequest request = BankSerializer.requestFromString(m.getText());
              onBankRequestArrived(request);
          } catch (JMSException e) {
              e.printStackTrace();
          }
      });
    }

    public void replyBankInterest(BankInterestReply bankInterestReply) {
        Message message = bankSender.createMessage(BankSerializer.replyToString(bankInterestReply));
        bankSender.send(message);
    }

    public void onBankRequestArrived(BankInterestRequest request) {}
}
