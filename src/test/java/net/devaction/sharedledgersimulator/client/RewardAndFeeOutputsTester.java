package net.devaction.sharedledgersimulator.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.transaction.AddressAndAmount;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.sharedledgersimulator.transaction.UnsignedTransaction;

/**
 * (Initial) @author Víctor Gil
 */
public class RewardAndFeeOutputsTester{
    private static final Log log = LogFactory.getLog(RewardAndFeeOutputsTester.class);
    
    public static void main(String[] args){
        byte[] minerAddress = new byte[20];        
        ThreadLocalRandom.current().nextBytes(minerAddress);
        
        AddressAndAmount rewardOutput = new AddressAndAmount(minerAddress, 13);
        AddressAndAmount feeOutput = new AddressAndAmount(minerAddress, 22);        
        List<AddressAndAmount> outputList =  new ArrayList<AddressAndAmount>(2);
        outputList.add(rewardOutput);
        outputList.add(feeOutput);
        UnsignedTransaction unsignedTransaction = new UnsignedTransaction(null, outputList, null);
        Transaction transaction = new Transaction(unsignedTransaction, null);
                
        RewardAndFeeOutputs rewardAndFeeOutputs = new RewardAndFeeOutputs();
        boolean correct = rewardAndFeeOutputs.add(transaction, minerAddress, 50);
        log.info("Are the reward output and the fee output correct?: " + correct);
    }
}
