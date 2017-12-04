package net.devaction.sharedledgersimulator.block;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.transaction.AddressAndAmount;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.sharedledgersimulator.transaction.UnsignedTransaction;
import net.devaction.util.PropertiesProvider;
import net.devaction.util.format.DateUtil;

/**
 * @author Víctor Gil
 * 
 * this class is mutable because of the "setNounce" method
 */
public class Block implements Serializable, Comparable<Block>{
    
    private static transient final Log log = LogFactory.getLog(Block.class);
    
    private static transient final long REWARD = Long.parseLong(PropertiesProvider.get("reward.per.block"));
    
    private static final long serialVersionUID = -3608477095371744350L;
    
    //this Java time, not Unix time
    private long timestamp;
    private Set<Transaction> transactions;
    //This is obviously not the "Java hash code" of the previous block
    private byte[] previousBlockByteHashcode; 
    private byte[] minerAddress;
    private long nounce;

    public Block(Collection<Transaction> transactionsCollection, byte[] previousBlockByteHashcode, 
            byte[] minerAddress){
        if (transactionsCollection == null){
            String errMessage = "The transactions collection cannot be null";
            log.error(errMessage);
            throw new IllegalArgumentException(errMessage);            
        }
        transactions = Collections.unmodifiableSet(new HashSet<Transaction>(transactionsCollection));
        this.previousBlockByteHashcode = previousBlockByteHashcode;
        this.minerAddress = minerAddress;
        this.timestamp = new Date().getTime();        
        nounce = ThreadLocalRandom.current().nextLong();
    }
    
    public Block(Collection<Transaction> transactionsCollectionWithoutFeeNorReward, byte[] previousBlockByteHashcode, 
            byte[] minerAddress, long fee){
        if (transactionsCollectionWithoutFeeNorReward == null){
            transactionsCollectionWithoutFeeNorReward = new ArrayList<Transaction>();
        }
        transactions = Collections.unmodifiableSet(
                addFeeAndRewardTransaction(transactionsCollectionWithoutFeeNorReward, minerAddress, fee));
        this.previousBlockByteHashcode = previousBlockByteHashcode;
        this.minerAddress = minerAddress;
        this.timestamp = new Date().getTime();        
        nounce = ThreadLocalRandom.current().nextLong();
    }
    
    static Set<Transaction> addFeeAndRewardTransaction(
            Collection<Transaction> transactionsCollection, byte[] minerAddress, long fee){
        Set<Transaction> transactions = new HashSet<Transaction>(transactionsCollection);
        AddressAndAmount output1 = new AddressAndAmount(minerAddress, REWARD);
        AddressAndAmount output2 = new AddressAndAmount(minerAddress, fee);
        List<AddressAndAmount> addressesAndAmounts = new ArrayList<AddressAndAmount>();
        addressesAndAmounts.add(output1);
        addressesAndAmounts.add(output2);
        UnsignedTransaction unsignedTx = new UnsignedTransaction(
                null, //the source address is null
                addressesAndAmounts,
                null); //the public key is also null
        //The stamped signature is also null
        Transaction tx = new Transaction(unsignedTx, null);
        transactions.add(tx);
        return Collections.unmodifiableSet(transactions);
    }

    @Override
    public int compareTo(Block otherBlock){
        return Long.compare(timestamp, otherBlock.getTimestamp());
    }
    
    //This is the "Java hash code" (to be used by Java collections such as HashSet
    // or HashMap), not to be confused with the "byte(s) hash code" which is 
    //the hash code used for the block chain
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(minerAddress);
        result = prime * result + (int) (nounce ^ (nounce >>> 32));
        if (previousBlockByteHashcode != null)
            result = prime * result + Arrays.hashCode(previousBlockByteHashcode);
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        //actually, transactions cannot be null
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
        Block other = (Block) obj;
        if (!Arrays.equals(minerAddress, other.minerAddress))
            return false;
        if (nounce != other.nounce)
            return false;
        if (!Arrays.equals(previousBlockByteHashcode, other.previousBlockByteHashcode))
            return false;
        if (timestamp != other.timestamp)
            return false;
        if (transactions == null){
            if (other.transactions != null)
                return false;
        } else if (!transactions.equals(other.transactions))
            return false;
        return true;
    }

    @Override
    public String toString() {        
        String previousBlockByteHashcodeString = null;
        if (previousBlockByteHashcode != null)
            previousBlockByteHashcodeString = ByteHashcodeProvider.byteHashcodeToString(previousBlockByteHashcode) + 
                    "  " + Arrays.toString(previousBlockByteHashcode);
        
        return "Block [timestamp=" + DateUtil.getDateString(timestamp) + 
                "\n    transactions=" + transactions + 
                "\n    previousBlockByteHashcode=" + previousBlockByteHashcodeString + 
                "\n    minerAddress=" + Arrays.toString(minerAddress) +
                "\n    nounce=" + nounce + "]";
    }

    public long getNounce(){
        return nounce;
    }

    public void setNounce(long nounce){
        this.nounce = nounce;
    }

    public long getTimestamp(){
        return timestamp;
    }
    
    public String getTimestampString(){
        return DateUtil.getDateString(timestamp);
    }
    
    public Collection<Transaction> getTransactions(){
        return transactions;
    }

    public byte[] getPreviousBlockHashcode(){
        return previousBlockByteHashcode;
    }

    public byte[] getMinerAddress(){
        return minerAddress;
    }    
}
