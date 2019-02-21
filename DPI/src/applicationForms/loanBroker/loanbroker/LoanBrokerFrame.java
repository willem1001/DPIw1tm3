package applicationForms.loanBroker.loanbroker;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import applicationForms.Gateways.BankAppGateway;
import applicationForms.Gateways.LoanClientAppGateway;
import mix.model.bank.*;
import mix.model.loan.LoanReply;
import mix.model.loan.LoanRequest;


public class LoanBrokerFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultListModel<JListLine> listModel = new DefaultListModel<JListLine>();
    private JList<JListLine> list;
    private LoanClientAppGateway loanClientAppGateway;
    private BankAppGateway bankAppGateway;
    private ArrayList<Awaiting> awaiting = new ArrayList<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoanBrokerFrame frame = new LoanBrokerFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Create the frame.
     */
    public LoanBrokerFrame() {

        loanClientAppGateway = new LoanClientAppGateway() {
            @Override
            public void onLoanRequestArrived(LoanRequest loanRequest) {
                add(loanRequest);
                ArrayList<String> avaliableBanks = CalculateBanks.calculateBanks(loanRequest);

                awaiting.add(new Awaiting(loanRequest.getMessageId(), avaliableBanks.size()));

                for (String bankQueue : avaliableBanks
                ) {
                    bankAppGateway.requestBankInterest(new BankInterestRequest(loanRequest.getAmount(), loanRequest.getTime(), loanRequest.getMessageId()), bankQueue);
                }
            }
        };

        bankAppGateway = new BankAppGateway() {
            @Override
            public void onBankReplyArrived(BankInterestReply bankInterestReply) {

                String messageId = bankInterestReply.getMessageId();

                for (Awaiting a: awaiting
                     ) {
                    if(a.getMessageId().equals(messageId)){
                        a.received(bankInterestReply);
                        if(a.getAwaitingCount() <= 0) {
                            BankInterestReply cheapest = a.getCheapest();
                            LoanRequest loanRequest = getRequestReplyByMessageId(messageId).getLoanRequest();
                            add(loanRequest, cheapest);
                            LoanReply loanReply = new LoanReply();
                            loanReply.setInterest(cheapest.getInterest());
                            loanReply.setQuoteID(cheapest.getQuoteId());
                            loanReply.setMessageId(messageId);
                            loanClientAppGateway.sendLoanReply(loanReply);
                            awaiting.remove(a);
                            return;
                        }
                    }
                }
            }
        };

        setTitle("Loan Broker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{46, 31, 86, 30, 89, 0};
        gbl_contentPane.rowHeights = new int[]{233, 23, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 7;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        contentPane.add(scrollPane, gbc_scrollPane);

        list = new JList<JListLine>(listModel);
        scrollPane.setViewportView(list);
    }

    private JListLine getRequestReply(LoanRequest request) {

        for (int i = 0; i < listModel.getSize(); i++) {
            JListLine rr = listModel.get(i);
            if (rr.getLoanRequest() == request) {
                return rr;
            }
        }
        return null;
    }

    private JListLine getRequestReplyByMessageId(String messageId) {

        for (int i = 0; i < listModel.getSize(); i++) {
            JListLine rr = listModel.get(i);
            if (rr.getLoanRequest().getMessageId().equals(messageId)) {
                return rr;
            }
        }
        return null;
    }

    public void add(LoanRequest loanRequest) {
        listModel.addElement(new JListLine(loanRequest));
    }


    public void add(LoanRequest loanRequest, BankInterestRequest bankRequest) {
        JListLine rr = getRequestReply(loanRequest);
        if (rr != null && bankRequest != null) {
            rr.setBankRequest(bankRequest);
            list.repaint();
        }
    }

    public void add(LoanRequest loanRequest, BankInterestReply bankReply) {
        JListLine rr = getRequestReply(loanRequest);
        if (rr != null && bankReply != null) {
            rr.setBankReply(bankReply);
            list.repaint();
        }
    }
}
