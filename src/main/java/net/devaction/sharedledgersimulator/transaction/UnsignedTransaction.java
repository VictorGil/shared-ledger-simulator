package net.devaction.sharedledgersimulator.transaction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.devaction.util.format.DateUtil;

/**
 * @author Víctor Gil
 * 
 * this class is immutable
 */
public class UnsignedTransaction implements Serializable{
    private static final long serialVersionUID = 985483344321096501L;
    private byte[] publicKey;
    private byte[] sourceAddress;
    
    private Set<AddressAndAmount> targetAddressesAndAmounts;
    
    //this Java time, not Unix time
    private long timestamp;
    
    public UnsignedTransaction(byte[] sourceAddress, Collection<AddressAndAmount> addressesAndTransactionsCollection,
            byte[] publicKey){
        this.sourceAddress = sourceAddress;
        this.targetAddressesAndAmounts = Collections.unmodifiableSet(new HashSet<AddressAndAmount>(addressesAndTransactionsCollection));
        this.timestamp = new Date().getTime();
        this.publicKey = publicKey;
    }

    public boolean equals(Object object){
        if (object == this) 
            return true;
        if (object == null) 
            return false;
        if (!(object instanceof UnsignedTransaction)) 
            return false;
        
        UnsignedTransaction otherUT = (UnsignedTransaction) object;
        return Arrays.equals(sourceAddress, otherUT.sourceAddress) &&
                Arrays.equals(publicKey, otherUT.getPublicKey()) && 
                targetAddressesAndAmounts.containsAll(otherUT.getTargetAddressesAndAmounts()) &&
                otherUT.getTargetAddressesAndAmounts().containsAll(targetAddressesAndAmounts) &&
                timestamp == otherUT.timestamp;                
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(sourceAddress, targetAddressesAndAmounts, timestamp);
    }
    
    @Override
    public String toString(){
        int publicKeyLength  = 0;
        if (publicKey != null)
            publicKeyLength = publicKey.length;
        return "UnsignedTransaction[" 
                + "\n    sourceAddress=" + Arrays.toString(sourceAddress)
                + "\n    publicKey=" + Arrays.toString(publicKey)
                + "\n    public key length in bytes: " + publicKeyLength
                + "\n    targetAddressesAndAmounts=" + targetAddressesAndAmounts  
                + "\n    timestamp=" + DateUtil.getDateString(timestamp) 
                + "]";
    }

    byte[] getSourceAddress() {
        return sourceAddress;
    }

    long getTimestamp() {
        return timestamp;
    }

    Collection<AddressAndAmount> getTargetAddressesAndAmounts() {
        return targetAddressesAndAmounts;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }
}
