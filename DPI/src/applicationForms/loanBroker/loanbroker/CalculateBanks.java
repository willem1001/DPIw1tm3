package applicationForms.loanBroker.loanbroker;

import mix.model.loan.LoanRequest;
import java.util.ArrayList;

abstract class CalculateBanks {
     static ArrayList<String> calculateBanks(LoanRequest loanRequest) {
        ArrayList<String> banks = new ArrayList<>();
        if(loanRequest.getAmount()  <= 100000 && loanRequest.getTime() <= 10){
            banks.add("ToING");
        }
        if(loanRequest.getAmount() <= 300000 && loanRequest.getAmount() >= 200000 && loanRequest.getTime() <=20) {
            banks.add("ToABN");
        }
        if(loanRequest.getAmount() <= 250000 && loanRequest.getTime() <= 15) {
            banks.add("ToRABO");
        }
        return banks;
    }
}
