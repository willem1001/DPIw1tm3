package mix.model.bank;

import java.io.Serializable;

/**
 *
 * This class stores all information about an request from a bank to offer
 * a loan to a specific client.
 */
public class BankInterestRequest implements Serializable {

    private int amount; // the requested loan amount
    private int time; // the requested loan period
    private String messageId;
    private String aggregationId;

    public BankInterestRequest() {
        super();
        this.amount = 0;
        this.time = 0;
    }

    public BankInterestRequest(int amount, int time, String messageId) {
        super();
        this.amount = amount;
        this.time = time;
        this.messageId = messageId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return " amount=" + amount + " time=" + time;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAggregationId() {
        return aggregationId;
    }

    public void setAggregationId(String aggregationId) {
        this.aggregationId = aggregationId;
    }
}
