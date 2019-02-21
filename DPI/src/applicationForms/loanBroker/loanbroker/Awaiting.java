package applicationForms.loanBroker.loanbroker;

import mix.model.bank.BankInterestReply;

import java.util.ArrayList;

public class Awaiting {
    private String messageId;
    private int awaitingCount;
    private ArrayList<BankInterestReply> replies = new ArrayList<>();

    public Awaiting(String messageId, int awaitingCount) {
        this.messageId = messageId;
        this.awaitingCount = awaitingCount;
    }

    public void received(BankInterestReply reply) {
        if(!isReplaced(reply)) {
            replies.add(reply);
            awaitingCount--;
        }
    }

    public boolean isReplaced(BankInterestReply reply) {
        for (BankInterestReply r : replies
        ) {
            if (r.getQuoteId().equals(reply.getQuoteId())) {
                replies.remove(r);
                replies.add(reply);
                return true;
            }
        }
        return false;
    }

    public BankInterestReply getCheapest() {
        BankInterestReply cheapest = null;
        for (BankInterestReply r: replies
             ) {
            if(cheapest == null || r.getInterest() < cheapest.getInterest()) {
                cheapest = r;
            }
        }
        return cheapest;
    }

    public int getAwaitingCount() {
        return this.awaitingCount;
    }

    public String getMessageId() {
        return messageId;
    }

    public ArrayList<BankInterestReply> getReplies() {
        return replies;
    }
}
