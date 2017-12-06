package net.devaction.sharedledgersimulator.client;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.devaction.sharedledgersimulator.network.TransactionsInTime;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.sharedledgersimulator.transaction.TransactionsInBlock;
import net.devaction.util.PropertiesProvider;

/**
 * @author Víctor Gil
 */
public class NewTxProvider{
    private final TransactionsInTime transactionsInTime;
    private static final int TX_PER_BLOCK = Integer.parseInt(PropertiesProvider.get("transactions.per.block"));
    
    public NewTxProvider(){
        transactionsInTime = TransactionsInTime.getInstance();
    }
    
    public NewTransactionsAndTotalFee provideThoseWithHighestFee(ChainVerificationResult 
            chainVerificationResult){
        
        SortedSet<TransactionWithFee> txWithFeeSet = provideOrderedByFee(chainVerificationResult);
        Iterator<TransactionWithFee> iter = txWithFeeSet.iterator();
        NewTransactionsAndTotalFee txsAndFee = new NewTransactionsAndTotalFee();
        while(iter.hasNext() && txsAndFee.size() < TX_PER_BLOCK){
            txsAndFee.add(iter.next());
        }
        return txsAndFee;
    }
    
    SortedSet<TransactionWithFee> provideOrderedByFee(ChainVerificationResult 
            chainVerificationResult){
        final Iterator<Transaction> txIterator = transactionsInTime.iterator();
        List<TransactionsInBlock> txInChain = chainVerificationResult.getTransactionsInChain();
        SortedSet<TransactionWithFee> txWithFeeSet = new TreeSet<TransactionWithFee>();
        while (txIterator.hasNext()){
            Transaction transaction = txIterator.next();
            if (!TransactionsVerifier.verifySignature(transaction))
                continue;
            if (isAlreadyPresentInChain(transaction, txInChain))
                continue;
            Map<List<Byte>, Long> balances = chainVerificationResult.getBalances();
            TransactionWithFee txWithFee = TransactionWithFeeConstructor.contruct(transaction, balances);
            txWithFeeSet.add(txWithFee);
        }        
        return txWithFeeSet;
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
}
