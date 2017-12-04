package net.devaction.sharedledgersimulator.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.devaction.sharedledgersimulator.transaction.Transaction;

/**
 * @author Víctor Gil
 */
public class TransactionsInTime{
    private static TransactionsInTime INSTANCE;
    
    //It is only possible to add Transaction objects to this set
    //It is not allowed to remove any Transaction object from it
    private final List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
    
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
    
    public Iterator<Transaction> iterator(){
        return new TransactionsInTimeIterator(this);
    }
    
    Transaction get(int i){
        return transactions.get(i);
    }
    
    int size(){
        return transactions.size();
    }
    
    @Override
    public String toString() {
        return "Number of transactions: " + transactions.size() + 
                "\nTransactionsInTime [" + transactions + "]";
    }
}
