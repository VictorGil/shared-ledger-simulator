package net.devaction.sharedledgersimulator.network;

import java.util.concurrent.ConcurrentSkipListSet;

import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.util.IteratorWithoutRemove;

/**
 * @author Víctor Gil
 */
public class TransactionsInTime{
    private static TransactionsInTime INSTANCE;
    
    //It is only possible to add Transaction objects to this set
    //It is not allowed to remove any Transaction object from it
    private final ConcurrentSkipListSet<Transaction> transactions = new ConcurrentSkipListSet<Transaction>();
    
    public static TransactionsInTime getInstance(){
        if (INSTANCE == null)
            INSTANCE = new TransactionsInTime();
        return INSTANCE;        
    }
    
    private TransactionsInTime(){
    }
    
    public void add(Transaction transaction){
        transactions.add(transaction);
    }
    
    public IteratorWithoutRemove<Transaction> iterator(){
        return new IteratorWithoutRemove<Transaction>(transactions.iterator());
    }

    @Override
    public String toString() {
        return "Number of transactions: " + transactions.size() + 
                "\nTransactionsInTime [" + transactions + "]";
    }
}
