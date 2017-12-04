package net.devaction.sharedledgersimulator.network;

import java.util.Iterator;

import net.devaction.sharedledgersimulator.block.Block;

/**
 * @author Víctor Gil
 */
public class BlocksInTimeIterator implements Iterator<Block>{
    private final BlocksInTime blocksInTime;
    private int current;
    
    public BlocksInTimeIterator(BlocksInTime blocksInTime){
        this.blocksInTime = blocksInTime;
    }
    
    @Override
    public boolean hasNext(){
        if (current <= blocksInTime.size() - 1)
            return true;
        return false;
    }

    @Override
    public Block next(){
        if (hasNext()){
            Block next = blocksInTime.get(current);
            current++;
            return next;
        }
        return null;
    }
}
