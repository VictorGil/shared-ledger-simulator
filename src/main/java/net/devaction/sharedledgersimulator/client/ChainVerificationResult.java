package net.devaction.sharedledgersimulator.client;

import java.util.List;
import java.util.Map;

import net.devaction.sharedledgersimulator.transaction.TransactionsInBlock;

/**
 * @author Víctor Gil
 */
public class ChainVerificationResult{
    private final List<TransactionsInBlock> transactionsInChain;
    private final Map<List<Byte>, Long> balances;
    
    public ChainVerificationResult(List<TransactionsInBlock> transactionsInChain, 
            Map<List<Byte>, Long> balances){       
        this.transactionsInChain = transactionsInChain;
        this.balances = balances;
    }

    public List<TransactionsInBlock> getTransactionsInChain(){
        return transactionsInChain;
    }
        
    public Map<List<Byte>, Long> getBalances(){
        return balances;
    }
}
