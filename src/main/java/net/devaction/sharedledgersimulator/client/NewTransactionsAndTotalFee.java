package net.devaction.sharedledgersimulator.client;

import java.util.ArrayList;
import java.util.List;

import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.util.PropertiesProvider;

/**
 * @author Víctor Gil
 */
public class NewTransactionsAndTotalFee{
    private static final int TX_PER_BLOCK = Integer.parseInt(PropertiesProvider.get("transactions.per.block"));
    
    private final List<Transaction> transactions = new ArrayList<Transaction>(TX_PER_BLOCK);
    private long totalFee;

    public void add(TransactionWithFee transactionWithFee){
        transactions.add(transactionWithFee.getTransaction());
        totalFee = totalFee + transactionWithFee.getFee();
    }
    
    @Override
    public String toString() {
        return "NewTransactionsAndTotalFee [\n    total fee=" + totalFee + 
                "\n    transactions=" + transactions + "\n]";
    }

    public int size(){
       return transactions.size(); 
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public long getTotalFee() {
        return totalFee;
    }
}
