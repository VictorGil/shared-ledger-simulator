package net.devaction.sharedledgersimulator.client;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.transaction.AddressAndAmount;
import net.devaction.sharedledgersimulator.transaction.Transaction;

/**
 * @author Víctor Gil
 */
public class RewardAndFeeOutputs{
    private static final Log log = LogFactory.getLog(RewardAndFeeOutputs.class);
    
    private long reward = -1L;
    private long fee = -1L;
    
    //if it returns false, it means that 
    //the transaction is not correct
    public boolean add(Transaction transaction, 
            byte[] minerAddress, long reward){
        log.debug("Going to add: " + transaction + "\n"
                + "miner address: " + Arrays.toString(minerAddress) + "\nreward: " + reward);
        if (transaction == null){
            log.warn("Transaction argument is null");
            return false;
        }
        if (!(this.reward == -1L && this.fee == -1L)) {
            log.warn("A mining and fee transaction was already added");
            return false;
        }            
        if (transaction.numberOfOutputs() > 2 || transaction.numberOfOutputs() == 0){
            log.warn("Incorrect number of outputs.\n" + transaction);
            return false;
        }
        AddressAndAmount addressAndAmount = null;
        Iterator<AddressAndAmount> iterAandA = transaction.iterator();
        while (iterAandA.hasNext()){
            addressAndAmount = iterAandA.next();
            if (!Arrays.equals(addressAndAmount.getAddress(), minerAddress)){
                log.warn("The target address does not match the miner address:\n" + 
                        "target address: " + addressAndAmount.getAddress() + 
                        "\n miner address: " + minerAddress);
                return false;
            }
            if (addressAndAmount.getAmount() == reward){
                if (this.reward == -1)
                    this.reward = reward;
                else {
                    this.fee = addressAndAmount.getAmount();
                } 
            } else{
                if (fee != -1) {
                    log.warn("Incorrect amounts: " + transaction);
                    return false;
                }
                fee = addressAndAmount.getAmount();
            }
        }
        if (fee == -1L)
            fee = 0L;
        return true;
    }

    public long getFee() {
        return fee;
    }
}
