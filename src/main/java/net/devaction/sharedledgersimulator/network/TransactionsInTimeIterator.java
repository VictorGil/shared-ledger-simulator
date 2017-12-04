package net.devaction.sharedledgersimulator.network;

import java.util.Iterator;

import net.devaction.sharedledgersimulator.transaction.Transaction;

/**
 * @author Víctor Gil
 */
public class TransactionsInTimeIterator implements Iterator<Transaction>{
    private final TransactionsInTime transactionsInTime;
    private int current;
    
    public TransactionsInTimeIterator(TransactionsInTime transactionsInTime){
        this.transactionsInTime = transactionsInTime;
    }
    
    @Override
    public boolean hasNext(){
        if (current <= transactionsInTime.size() - 1)
            return true;
        return false;
    }

    @Override
    public Transaction next(){
        if (hasNext()){
            Transaction next = transactionsInTime.get(current);
            current++;
            return next;
        }
        return null;
    }
}
