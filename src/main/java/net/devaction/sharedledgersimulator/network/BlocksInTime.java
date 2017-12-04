package net.devaction.sharedledgersimulator.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import net.devaction.adt.TimeLineIterator;
import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.util.IteratorWithoutRemove;

/**
 * @author Víctor Gil
 */
public class BlocksInTime{
    private static BlocksInTime INSTANCE;
    
    //It is only possible to add Block objects to this set
    //It is not allowed to remove any Block object from it
    //private final ConcurrentSkipListSet<Block> blocks = new ConcurrentSkipListSet<Block>();
    //It is only possible to add Block objects to this list
    //It is not allowed to remove any Block object from it
    private final List<Block> blocks = Collections.synchronizedList(new ArrayList<Block>());
    
    public static BlocksInTime getInstance(){
        if (INSTANCE == null)
            INSTANCE = new BlocksInTime();
        return INSTANCE;        
    }
    
    private BlocksInTime(){        
    }
    
    public void add(Block block){
        blocks.add(block);
    }
    
    public Iterator<Block> iterator(){
        return new BlocksInTimeIterator(this);
    }
    
    Block get(int i){
        return blocks.get(i);
    }
    
    int size(){
        return blocks.size();
    }
    
    @Override
    public String toString() {
        return "Number of blocks: " + blocks.size() +
                "\nBlocksInTime [" + blocks + "]";
    }
}
