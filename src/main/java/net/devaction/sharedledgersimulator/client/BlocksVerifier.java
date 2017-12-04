package net.devaction.sharedledgersimulator.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.sharedledgersimulator.transaction.TransactionsInBlock;

/**
 * @author Víctor Gil
 */
public class BlocksVerifier{
    
    public static ChainVerificationResult verify(Block block, BlocksProvider blockProvider){
        List<TransactionsInBlock> transactionList = new ArrayList<TransactionsInBlock>();
        while(block != null){
            if (!verifyHashcode(block))
                //verification failed
                return null;
            
            transactionList.add(
                    new TransactionsInBlock(block.getTransactions(), block.getMinerAddress()));
            
            if (block.getPreviousBlockHashcode() == null)
                block = null;
            else
                block = blockProvider.getBlock(block.getPreviousBlockHashcode());
        }
        
        Map<List<Byte>, Long> balances = TransactionsVerifier.verify(transactionList);
        if (balances == null)
            //verification failed
            return null;
        return new ChainVerificationResult(transactionList, balances);
    }
    
    static boolean verifyHashcode(Block block){
        return NounceFinder.enoughNumberOfLeadingZeros(block);
    }
}
