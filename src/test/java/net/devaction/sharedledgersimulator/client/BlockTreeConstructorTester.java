package net.devaction.sharedledgersimulator.client;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.sharedledgersimulator.block.ByteHashcodeProvider;
import net.devaction.sharedledgersimulator.network.BlocksInTime;
import net.devaction.sharedledgersimulator.transaction.RandomTransactionsProvider;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.sharedledgersimulator.tree.BlockNode;
import net.devaction.sharedledgersimulator.tree.DFSTraversal;
import net.devaction.sharedledgersimulator.tree.NodesOrderedByLength;

/**
 * @author Víctor Gil
 */
public class BlockTreeConstructorTester{
    private static final Log log = LogFactory.getLog(BlockTreeConstructorTester.class);
    
    public static void main(String[] args){
        log.info("Going to create the Genesis random block:");
        Block genesis = provideRandomGenesisBlock();
        log.info(genesis);
        
        log.info("Going to create a random block:");
        Block block1 = provideRandomBlock(ByteHashcodeProvider.provide(genesis));
        log.info(block1);
        BlocksInTime blocksInTime = BlocksInTime.getInstance();
        blocksInTime.add(genesis);
        blocksInTime.add(block1);
        
        Block block2 = provideRandomBlock(ByteHashcodeProvider.provide(block1));
        blocksInTime.add(block2);
        
        Block block3a = provideRandomBlock(ByteHashcodeProvider.provide(block2));
        blocksInTime.add(block3a);
        Block block3b = provideRandomBlock(ByteHashcodeProvider.provide(block2));
        blocksInTime.add(block3b);
        
        Block block4a = provideRandomBlock(ByteHashcodeProvider.provide(block3a));
        Block block4b = provideRandomBlock(ByteHashcodeProvider.provide(block3a));
        Block block4c = provideRandomBlock(ByteHashcodeProvider.provide(block3a));
        Block block4d = provideRandomBlock(ByteHashcodeProvider.provide(block3b));
        
        Block block5a = provideRandomBlock(ByteHashcodeProvider.provide(block4b));
        Block block5b = provideRandomBlock(ByteHashcodeProvider.provide(block4d));
        
        Block block6a = provideRandomBlock(ByteHashcodeProvider.provide(block5a));
        
        blocksInTime.add(block3b);
        blocksInTime.add(block4a); blocksInTime.add(block4b);
        blocksInTime.add(block4c); blocksInTime.add(block4d);
        blocksInTime.add(block5a); blocksInTime.add(block5b);
        blocksInTime.add(block6a);
        
        BlockTreeConstructor btc = new BlockTreeConstructor();
        
        //Map<List<Byte>, BlockNode> hashcodeMap = btc.constructMap();
        //log.info("hashcode map:\n" + hashcodeMap);
        
        BlockNode root = (BlockNode) btc.constructTree();
        log.info("Root node (genesis block): " + root);
        log.info("Complete block tree: " + root);
        root.print();
        
        NodesOrderedByLength<Block> nodesByLenth = DFSTraversal.traverseAddingLength(root);
        root.print(); 
        log.info("Blocks ordered by length (distance to genesis block):");
        log.info(nodesByLenth);
    }
    
    public static Block provideRandomGenesisBlock(){
        byte[] minerAddress = new byte[20];        
        ThreadLocalRandom.current().nextBytes(minerAddress);
        List<Transaction> transactions = RandomTransactionsProvider.provide();
        Block block = new Block(transactions, null, minerAddress, 0);
        return block;
    } 
    
    public static Block provideRandomBlock(byte[] previousBlockByteHashcode){
        byte[] minerAddress = new byte[20];        
        ThreadLocalRandom.current().nextBytes(minerAddress);
        List<Transaction> transactions = RandomTransactionsProvider.provide();
        Block block = new Block(transactions, previousBlockByteHashcode, minerAddress, 0);
        return block;
    } 

    public static Block provideRandomBlock(Block previousBlock){
        if (previousBlock == null)
            return provideRandomGenesisBlock();
        byte[] previousBlockByteHashcode = ByteHashcodeProvider.provide(previousBlock);
        return provideRandomBlock(previousBlockByteHashcode);
    } 
}
