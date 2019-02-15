package applicationForms.Gateways.Serializers;

import com.owlike.genson.Genson;
import mix.model.loan.LoanReply;
import mix.model.loan.LoanRequest;

public class LoanSerializer {
    public static String requestToString(LoanRequest request) {
        return new Genson().serialize(request);
    }

    public static LoanRequest requestFromString(String string) {
        return new Genson().deserialize(string, LoanRequest.class);
    }

    public static String replyToString(LoanReply reply) {
        return new Genson().serialize(reply);
    }

    public static LoanReply replyFromString(String string) {
        return new Genson().deserialize(string, LoanReply.class);
    }
}
