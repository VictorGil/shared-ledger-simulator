package net.devaction.sharedledgersimulator.transaction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import net.devaction.util.IteratorWithoutRemove;

/**
 * @author Víctor Gil
 * 
 * this class is immutable
 */
public class Transaction implements Comparable<Transaction>, Serializable{

    private static final long serialVersionUID = -1766050397589144684L;

    private UnsignedTransaction unsignedTransaction;
    
    private byte[] stampedSignature;
    
    public Transaction(UnsignedTransaction unsignedTransaction, byte[] stampedSignature){
        this.unsignedTransaction = unsignedTransaction;
        this.stampedSignature = stampedSignature;        
    }

    @Override
    public boolean equals(Object object){
        if (object == this) 
            return true;
        if (!(object instanceof Transaction)) 
            return false;
        
        Transaction otherTransaction = (Transaction) object;
        return Arrays.equals(stampedSignature, otherTransaction.stampedSignature)
                && unsignedTransaction.equals(otherTransaction.getUnsignedTransaction());
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(stampedSignature, unsignedTransaction);
    }
    
    @Override
    public int compareTo(Transaction transaction){
        return Long.compare(getTimestamp(), transaction.getTimestamp());
    }

    public IteratorWithoutRemove<AddressAndAmount> iterator(){
        return new IteratorWithoutRemove<AddressAndAmount>(unsignedTransaction.getTargetAddressesAndAmounts().iterator());
    }
    
    public int numberOfOutputs(){
        return unsignedTransaction.getTargetAddressesAndAmounts().size();
    }
    public byte[] getSourceAddress(){
        return unsignedTransaction.getSourceAddress();
    }

    public long getTimestamp() {
        return unsignedTransaction.getTimestamp();
    }
    
    public byte[] getStampedSignature() {
        return stampedSignature;
    }

    public UnsignedTransaction getUnsignedTransaction() {
        return unsignedTransaction;
    }
    
    @Override
    public String toString() {
        return "Transaction [" +
                "\n    unsignedTransaction=" + unsignedTransaction + 
                "\n    stampedSignature=" + Arrays.toString(stampedSignature) + "]";
    }
}
