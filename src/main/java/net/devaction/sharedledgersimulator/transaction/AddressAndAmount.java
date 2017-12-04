package net.devaction.sharedledgersimulator.transaction;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Víctor Gil
 */
public class AddressAndAmount implements Serializable{
    private static final long serialVersionUID = -6944400578533599709L;
    
    private byte[] address;
    //this is in the minimal denomination of the 
    //cryptocurrency, e.g., in Satoshis for Bitcoin 
    private long amount;
    
    public AddressAndAmount(byte[] address, long amount){
        this.address = address;
        this.amount = amount;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(address);
        result = prime * result + (int) (amount ^ (amount >>> 32));
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
        AddressAndAmount other = (AddressAndAmount) obj;
        if (!Arrays.equals(address, other.address))
            return false;
        if (amount != other.amount)
            return false;
        return true;
    }

    public byte[] getAddress() {
        return address;
    }
    
    public long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "AddressAndAmount["
                + "\n    address=" + Arrays.toString(address)  
                + "\n    amount=" + amount + "]";
    }    
}
