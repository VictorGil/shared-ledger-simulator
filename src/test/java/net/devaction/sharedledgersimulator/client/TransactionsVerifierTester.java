package net.devaction.sharedledgersimulator.client;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.transaction.TransactionsInBlock;
import net.devaction.sharedledgersimulator.transaction.ValidTransactionsInChainProvider;

/**
 * @author Víctor Gil
 */
public class TransactionsVerifierTester{
    private static final Log log = LogFactory.getLog(TransactionsVerifierTester.class);
    
    public static void main(String[] args){
        List<TransactionsInBlock> txInBlockList = ValidTransactionsInChainProvider.provide();
        log.info("List of valid transactions in block: \n" + txInBlockList);
        log.info("\n\n\n");
        boolean verified = TransactionsVerifier.verify(txInBlockList);
        log.info("Were the transactions verified?: " + verified);
    }
}
