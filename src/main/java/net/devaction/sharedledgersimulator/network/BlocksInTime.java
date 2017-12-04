package net.devaction.sharedledgersimulator.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.sharedledgersimulator.block.ByteHashcodeProvider;

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
        StringBuilder blocksSB = new StringBuilder();;
        for (int i = 0; i<blocks.size(); i++){
            blocksSB.append("BLOCK " + i + "\n");
            blocksSB.append(toStringBlock(blocks.get(i))).append("\n");
        }
        return "Number of blocks: " + blocks.size() +
                "\nBlocksInTime [\n" + 
                blocksSB + "\n]";
    }
    
    String toStringBlock(Block block){
        byte[] byteHashcode = ByteHashcodeProvider.provide(block);
        return "byteHascode: " + ByteHashcodeProvider.byteHashcodeToString(byteHashcode) + 
                "  " + Arrays.toString(byteHashcode) +
                "\n hascode bits: " + Arrays.toString(toBits(byteHashcode)) +
                "\nBlock data:\n" + block;       
    }
    
    String[] toBits(byte[] bytes){
        String[] bits = new String[bytes.length];
        for (int i = 0; i<bytes.length; i++) {
            bits[i] = toBits(bytes[i]);
        }        
        return bits;
    }
    
    String toBits(byte b1){
        return String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
    }
}
