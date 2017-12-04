package net.devaction.sharedledgersimulator.client;

import java.util.Iterator;
import java.util.List;

import net.devaction.sharedledgersimulator.network.TransactionsInTime;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.sharedledgersimulator.transaction.TransactionsInBlock;

/**
 * @author Víctor Gil
 */
public class NewTxProvider{
    private final TransactionsInTime transactionsInTime;
    
    public NewTxProvider(){
        transactionsInTime = TransactionsInTime.getInstance();
    }
    
    public List<Transaction> provideThoseWithHighestFee(ChainVerificationResult 
            chainVerificationResult){
        
        final Iterator<Transaction> txIterator = transactionsInTime.iterator();
        List<TransactionsInBlock> txInChain = chainVerificationResult.getTransactionsInChain();
        while (txIterator.hasNext()){
            Transaction transaction = txIterator.next();
            if (!TransactionsVerifier.verifySignature(transaction))
                continue;
            if (isAlreadyPresentInChain(transaction, txInChain))
                continue;
            
        }        
        return null;
    }
    boolean isAlreadyPresentInChain(Transaction transaction, List<TransactionsInBlock> transactionsInChain){
        for(TransactionsInBlock transactionsInBlock : transactionsInChain){
            Iterator<Transaction> txIter = transactionsInBlock.iterator();
            while(txIter.hasNext()){
                if (txIter.next().equals(transaction))
                    return true;
            }
        }
        return false;        
    }
    public List<Transaction> provideOrderedByFee(ChainVerificationResult 
            chainVerificationResult){
        return null;
    }
}
