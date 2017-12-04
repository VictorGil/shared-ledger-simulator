package net.devaction.sharedledgersimulator.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.sharedledgersimulator.block.ByteHashcodeProvider;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.sharedledgersimulator.transaction.TransactionsInBlock;

/**
 * @author Víctor Gil
 */
public class BlocksVerifier{
    
    public static boolean verify(Block block, BlocksProvider blockProvider){
        List<TransactionsInBlock> transactionList = new ArrayList<TransactionsInBlock>();
        while(block != null){
            if (!verifyHashcode(block))
                return false;
            
            transactionList.add(
                    new TransactionsInBlock(block.getTransactions(), block.getMinerAddress()));
            
            if (block.getPreviousBlockHashcode() == null)
                block = null;
            else
                block = blockProvider.getBlock(block.getPreviousBlockHashcode());
        }
        
        return TransactionsVerifier.verify(transactionList);
    }
    
    static boolean verifyHashcode(Block block){
        return NounceFinder.enoughNumberOfLeadingZeros(block);
    }
}
