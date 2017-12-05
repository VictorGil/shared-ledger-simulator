package net.devaction.sharedledgersimulator.client;

import java.util.Arrays;

import net.devaction.sharedledgersimulator.transaction.Transaction;

/**
 * @author Víctor Gil
 */
public class TransactionWithFee implements Comparable<TransactionWithFee>{
    private Transaction transaction;
    private long fee;
    
    public TransactionWithFee(Transaction transaction, long fee){
        this.transaction = transaction;
        this.fee = fee;
    }

    @Override
    public int compareTo(TransactionWithFee other){
        //return Long.compare(fee, other.getFee());
        //reverse order, transactions with higher fee first
        //because we are greedy :-)
        return Long.compare(other.getFee(), fee);
    }
    
    @Override
    public boolean equals(Object object){
        if (object == this) 
            return true;
        if (object == null) 
            return false;
        if (!(object instanceof TransactionWithFee)) 
            return false;
        
        TransactionWithFee otherTransactionWithFee = (TransactionWithFee) object;
        byte[] otherSourceAddress = otherTransactionWithFee.getTransaction().getSourceAddress();
        //There can be only one transaction per source address in a block
        return Arrays.equals(transaction.getSourceAddress(), otherSourceAddress);
    }
    
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (fee ^ (fee >>> 32));
        result = prime * result + ((transaction == null) ? 0 : transaction.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "TransactionWithFee[\n    fee=" + fee + "\n    transaction=" + transaction + "\n]";
    }
    
    public Transaction getTransaction(){
        return transaction;
    }

    public long getFee(){
        return fee;
    }
}
