package net.devaction.sharedledgersimulator.client;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.transaction.AddressAndAmount;
import net.devaction.sharedledgersimulator.transaction.Transaction;

/**
 * @author Víctor Gil
 */
public class TransactionWithFeeConstructor{
    private static final Log log = LogFactory.getLog(TransactionWithFeeConstructor.class);
    
    //This code is very similar to the one in TransactionsVerifier class
    //TO DO: create a common method
    public static TransactionWithFee contruct(Transaction transaction, 
            Map<List<Byte>, Long> balances){
        
        byte[] sourceAddress = transaction.getSourceAddress();
        List<Byte> sourceAddressKey = BlockTreeConstructor.generateKey(sourceAddress);
        Long balance = balances.get(sourceAddressKey);
        if (balance == null)
            balance = 0L;                    
        log.debug("\nBalance of the source address: " + balance);
        
        Iterator<AddressAndAmount> iterAandA = transaction.iterator();
        long totalAmount = 0L;
        while (iterAandA.hasNext()){
            AddressAndAmount addressAndAmount = iterAandA.next();
            totalAmount = totalAmount + addressAndAmount.getAmount();
        }
        log.debug("\nTotal amount of the transaction: " + totalAmount);
        
        if (balance < totalAmount){
            log.warn("The calculated fee does not much the fee in the transaction, "
                    + "balance: " + balance + ", total amount: " + totalAmount);
            return null;
        }
        long calculatedFee = balance - totalAmount;
        log.debug("The calculated fee is: " + calculatedFee);
        return new TransactionWithFee(transaction, calculatedFee);
    }
}
