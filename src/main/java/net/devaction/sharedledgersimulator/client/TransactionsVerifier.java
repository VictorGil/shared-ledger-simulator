package net.devaction.sharedledgersimulator.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.key.Verifier;
import net.devaction.sharedledgersimulator.rsa.RsaVerifier;
import net.devaction.sharedledgersimulator.transaction.AddressAndAmount;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.sharedledgersimulator.transaction.TransactionsInBlock;
import net.devaction.util.PropertiesProvider;
import net.devaction.util.serialization.Serializer;

/**
 * @author Víctor Gil
 */
public class TransactionsVerifier{
    private static final Log log = LogFactory.getLog(TransactionsVerifier.class);
    
    private static final long REWARD = Long.parseLong(PropertiesProvider.get("reward.per.block"));

    public static Map<List<Byte>, Long> verify(List<TransactionsInBlock> transactionsInChain){
        Map<List<Byte>, Long> balances = new HashMap<List<Byte>, Long>();
        log.info("Transactions in chain size is " + transactionsInChain.size() +
                ":\n\n" + transactionsInChain + "\n\n");
        for (int i = transactionsInChain.size() - 1; i >= 0; i--){
        //for (int i = 0; i < transactionsInChain.size(); i++){
            RewardAndFeeOutputs rewardAndFeeOutputs = new RewardAndFeeOutputs();
            TransactionsInBlock transactionsInBlock = transactionsInChain.get(i);
            Iterator<Transaction> transactionIter = transactionsInBlock.iterator();
            long calculatedFee = 0L;
            
            while(transactionIter.hasNext()){
                Transaction signedTransaction = transactionIter.next();
                
                if (signedTransaction.getSourceAddress() == null){  
                    if (rewardAndFeeOutputs.add(signedTransaction, 
                            transactionsInBlock.getMinerAddress(), REWARD)){
                        List<Byte> minerAddressKey = BlockTreeConstructor.generateKey(transactionsInBlock.getMinerAddress());
                        
                        Long minerTargetBalance = balances.get(minerAddressKey);
                        if (minerTargetBalance == null)
                            minerTargetBalance = 0L; 
                        balances.put(minerAddressKey,minerTargetBalance + REWARD + rewardAndFeeOutputs.getFee());
                        continue;
                   } else{
                       //verification failed
                       return null;  
                   }                   
                }
                
                if (!verifySignature(signedTransaction)){
                    log.warn("The signature stamped on the transaction could not be verified.\n" + 
                            signedTransaction);
                    return null;
                } else{
                    log.debug("Transaction stamped signature has been verified: " + signedTransaction);
                }
                
                byte[] sourceAddress = signedTransaction.getSourceAddress();
                List<Byte> sourceAddressKey = BlockTreeConstructor.generateKey(sourceAddress);
                Long balance = balances.get(sourceAddressKey);
                if (balance == null)
                    balance = 0L;                    
                log.debug("\nBalance of the source address: " + balance);
                
                Iterator<AddressAndAmount> iterAandA = signedTransaction.iterator();
                long totalAmount = 0;
                AddressAndAmount addressAndAmount = null;
                while (iterAandA.hasNext()){
                    addressAndAmount = iterAandA.next();
                    totalAmount = totalAmount + addressAndAmount.getAmount();
                }
                log.debug("\nTotal amount of the regular transaction: " + totalAmount);
                
                if (balance < totalAmount){
                    log.warn("The total amount in the transaction is bigger than the available balance, "
                            + "balance: " + balance + ", total amount: " + totalAmount);
                    return null;
                }
                calculatedFee = calculatedFee + (balance - totalAmount);
                balances.put(sourceAddressKey, 0L);
                
                Iterator<AddressAndAmount> iterAandA2 = signedTransaction.iterator();
                while (iterAandA2.hasNext()){
                    addressAndAmount = iterAandA2.next();
                    long amount = addressAndAmount.getAmount();
                    List<Byte> targetAddressKey = BlockTreeConstructor.generateKey(addressAndAmount.getAddress());
                    Long targetBalance = balances.get(targetAddressKey);                    
                    if (targetBalance == null)
                        targetBalance = 0L; 
                    log.debug("Target balance: " + targetBalance);
                    long newBalance = targetBalance + amount;
                    log.debug("New balance for " + Arrays.toString(addressAndAmount.getAddress()) + " is\n" + newBalance);
                    balances.put(targetAddressKey, targetBalance + newBalance);
                    log.info("Balances:\n" + balances);
                }
            }
            if (calculatedFee != rewardAndFeeOutputs.getFee()){
                log.warn("\nThe calculated fee does not match the fee in the transaction."
                        + "\ncalculated fee: " + calculatedFee + 
                        "\nrewardAndFeeOutputs.getFee(): " + rewardAndFeeOutputs.getFee()); 
                return null;
            }
                
        }
        log.debug("The list of the block transactions has been verified");
        return balances;
    }
    
    public static boolean verifySignature(Transaction signedTransaction){
        Verifier verifier = RsaVerifier.getInstance();
        return verifier.verify(
                Serializer.serialize(signedTransaction.getUnsignedTransaction()), 
                signedTransaction.getStampedSignature(), 
                signedTransaction.getUnsignedTransaction().getPublicKey());                
    }
}
