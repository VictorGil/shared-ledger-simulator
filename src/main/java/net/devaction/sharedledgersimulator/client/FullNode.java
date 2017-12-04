package net.devaction.sharedledgersimulator.client;

import java.util.Collection;
import java.util.Iterator;

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
public class FullNode{
    private static final Log log = LogFactory.getLog(FullNode.class);
            
    private final BlocksProvider blocksProvider;    
    private final byte[] minerAddress; 
    private boolean wasStopRequested;
    private BlocksInTime blocksInTime; 
    
    public FullNode(byte[] minerAddress){
        blocksProvider = new BlocksProvider();
        this.minerAddress = minerAddress;
        blocksInTime = BlocksInTime.getInstance(); 
    }
    
    public void start(){
        while(!wasStopRequested){
            NodesOrderedByLength<Block> nodes = blocksProvider.provideOrderedbyLength();
            Iterator<Node<Block>> nodesIter = nodes.iterator();
            Block longestVerifiedBlock = null;
            while(nodesIter.hasNext()){
                Block block = nodesIter.next().getData();
                if (BlocksVerifier.verify(block, blocksProvider)){
                    longestVerifiedBlock = block;
                    break;    
                }                
            }
            if (longestVerifiedBlock == null)
                continue;
            Collection<Transaction> transactions = null;
            //get new verified transactions  
            submitNewBlock(transactions, ByteHashcodeProvider.provide(longestVerifiedBlock));
            
            try{
                Thread.sleep(1000);
            } catch(InterruptedException ex){
                log.error(ex);
                throw new RuntimeException(ex);
            }
        }    
    }
    
    void submitNewBlock(Collection<Transaction> transactions, byte[] previousBlockHashcode){
        Block block = new Block(transactions, previousBlockHashcode, minerAddress, 0L);
        block = NounceFinder.find(block);    
        blocksInTime.add(block);
    }
    
    public void stop(){
        wasStopRequested = true;
    }
}
