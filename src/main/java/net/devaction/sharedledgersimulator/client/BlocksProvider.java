package net.devaction.sharedledgersimulator.client;

import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.sharedledgersimulator.tree.BlockNode;
import net.devaction.sharedledgersimulator.tree.DFSTraversal;
import net.devaction.sharedledgersimulator.tree.NodesOrderedByLength;

/**
 * @author Víctor Gil
 */
public class BlocksProvider{
    private final BlockTreeConstructor blockTreeConstructor;
    
    public BlocksProvider(){
        blockTreeConstructor = new BlockTreeConstructor();        
    }
    
    public NodesOrderedByLength<Block> provideOrderedbyLength(){
        BlockNode root = (BlockNode) blockTreeConstructor.constructTree();
        return DFSTraversal.traverseAddingLength(root);
    }
    
    public Block getBlock(byte[] byteHashcode){
        return blockTreeConstructor.getBlock(byteHashcode);
    }
    
    public int size(){
        return blockTreeConstructor.size();
    }
}
