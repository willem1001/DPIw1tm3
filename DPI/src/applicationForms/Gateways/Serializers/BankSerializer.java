package applicationForms.Gateways.Serializers;

import com.owlike.genson.Genson;
import mix.model.bank.BankInterestReply;
import mix.model.bank.BankInterestRequest;

public class BankSerializer {
    public static String requestToString(BankInterestRequest request) {
        return new Genson().serialize(request);
    }

    public static BankInterestRequest requestFromString(String string) {
        return new Genson().deserialize(string, BankInterestRequest.class);
    }

    public static String replyToString(BankInterestReply reply) {
        return new Genson().serialize(reply);
    }

    public static BankInterestReply replyFromString(String string) {
        return new Genson().deserialize(string, BankInterestReply.class);
    }
}
