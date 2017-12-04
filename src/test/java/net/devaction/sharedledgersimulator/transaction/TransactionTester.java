package net.devaction.sharedledgersimulator.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.key.Verifier;
import net.devaction.sharedledgersimulator.rsa.RsaVerifier;
import net.devaction.util.serialization.Serializer;

/**
 * @author Víctor Gil
 */
public class TransactionTester{
    private static final Log log = LogFactory.getLog(TransactionTester.class);
    
    public static void main(String[] args){
        Transaction transaction = RandomTransactionProvider.provide();
        log.info("Random transaction:\n" + transaction);
        log.info("Stamped signature length in bytes: " + transaction.getStampedSignature().length);
        
        Verifier verifier = RsaVerifier.getInstance();
        boolean isVerified = verifier.verify(
                Serializer.serialize(transaction.getUnsignedTransaction()), 
                transaction.getStampedSignature(), 
                transaction.getUnsignedTransaction().getPublicKey());
        log.info("Was the transaction successfully verified?: " + isVerified);
    }
}
