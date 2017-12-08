package net.devaction.sharedledgersimulator.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.sharedledgersimulator.block.ByteHashcodeProvider;
import net.devaction.sharedledgersimulator.network.BlocksInTime;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.sharedledgersimulator.tree.Node;
import net.devaction.sharedledgersimulator.tree.NodesOrderedByLength;

/**
 * @author Víctor Gil
 */
public class FullNode implements Runnable{
    private static final Log log = LogFactory.getLog(FullNode.class);
            
    private final BlocksProvider blocksProvider;    
    private final byte[] minerAddress; 
    private boolean wasStopRequested;
    private final BlocksInTime blocksInTime;
    private final NewTxProvider newTxProvider;
    
    public FullNode(byte[] minerAddress){
        blocksProvider = new BlocksProvider();
        this.minerAddress = minerAddress;
        blocksInTime = BlocksInTime.getInstance();
        newTxProvider = new NewTxProvider();
    }
    
    public void run(){
        log.info("Starting full node");
        while(!wasStopRequested){
            NodesOrderedByLength<Block> nodes = blocksProvider.provideOrderedbyLength();
            log.info("Number of nodes/blocks in tree (all chains): " + nodes.size());
            Iterator<Node<Block>> nodesIter = nodes.iterator();
            Block longestVerifiedBlock = null;
            log.info("Nodes ordered by decreasing length (distance to genesis block): " + nodes);
            ChainVerificationResult chainVerificationResult = null;
            while(nodesIter.hasNext()){
                Block block = nodesIter.next().getData();
                chainVerificationResult = BlocksVerifier.verify(block, blocksProvider);
                if (chainVerificationResult != null){
                    longestVerifiedBlock = block;
                    break;    
                }                
            }
            log.info("Tail verified block from longest chain: " + longestVerifiedBlock);
            log.info("Number of blocks in the chain: " + chainVerificationResult.getTransactionsInChain().size());
            
            try{
                Thread.sleep(1000);
            } catch(InterruptedException ex){
                log.error(ex);
                throw new RuntimeException(ex);
            }
            if (longestVerifiedBlock == null)
                continue;
            NewTransactionsAndTotalFee newTransactionsAndTotalFee = newTxProvider.provideThoseWithHighestFee(
                    chainVerificationResult);
            submitNewBlock(newTransactionsAndTotalFee.getTransactions(), 
                    ByteHashcodeProvider.provide(longestVerifiedBlock), 
                    newTransactionsAndTotalFee.getTotalFee());
        }
        log.info("Stopping full node");
    }
    
    void submitNewBlock(Collection<Transaction> transactions, byte[] previousBlockHashcode, long totalFee){
        Block block = new Block(transactions, previousBlockHashcode, minerAddress, totalFee);
        log.info("New block to be submitted: " + block);
        block = NounceFinder.find(block);    
        blocksInTime.add(block);
    }
    
    public void stop(){
        wasStopRequested = true;
    }
}
