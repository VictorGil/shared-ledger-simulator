package net.devaction.sharedledgersimulator.transaction;

import java.util.Arrays;
import java.util.Collection;

import net.devaction.util.IteratorWithoutRemove;

/**
 * @author Víctor Gil
 */
public class TransactionsInBlock{
    private Collection<Transaction> transactions;
    private byte[] minerAddress;
    
    public TransactionsInBlock(Collection<Transaction> transactions, byte[] minerAddress){
        this.transactions = transactions;
        this.minerAddress = minerAddress;
    }

    public IteratorWithoutRemove<Transaction> iterator(){
        return new IteratorWithoutRemove<Transaction>(transactions.iterator());
    }

    @Override
    public String toString() {
        return "TransactionsInBlock [transactions=" + transactions + "\n minerAddress=" + Arrays.toString(minerAddress)
                + "]";
    }
    
    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public byte[] getMinerAddress() {
        return minerAddress;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(minerAddress);
        result = prime * result + ((transactions == null) ? 0 : transactions.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TransactionsInBlock other = (TransactionsInBlock) obj;
        if (!Arrays.equals(minerAddress, other.minerAddress))
            return false;
        if (transactions == null){
            if (other.transactions != null)
                return false;
        } else if (!transactions.equals(other.transactions))
            return false;
        return true;
    }
}
