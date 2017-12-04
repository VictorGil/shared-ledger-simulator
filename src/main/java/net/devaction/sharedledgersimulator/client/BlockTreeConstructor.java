package net.devaction.sharedledgersimulator.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.sharedledgersimulator.block.ByteHashcodeProvider;
import net.devaction.sharedledgersimulator.network.BlocksInTime;
import net.devaction.sharedledgersimulator.tree.BlockNode;
import net.devaction.sharedledgersimulator.tree.Node;

/**
 * @author Víctor Gil
 */
public class BlockTreeConstructor{
    private static final Log log = LogFactory.getLog(BlockTreeConstructor.class);
    
    private BlocksInTime blocksInTime; 
    //private Map<List<Byte>, BlockNode> hashcodeMap;
    private Map<List<Byte>, BlockNode> hashcodeMap = new HashMap<List<Byte>, BlockNode>();;
    
    public BlockTreeConstructor(){
        blocksInTime = BlocksInTime.getInstance();    
    }
    
    public BlockNode constructTree(){

        BlockNode node = null;
        Iterator<Block> blocksIterator = blocksInTime.iterator();
        while (blocksIterator.hasNext()){
            Block block = blocksIterator.next();            
            List<Byte> key = generateKey(block);
            node = hashcodeMap.get(key);
            if (node == null){
                node = new BlockNode(block);
                hashcodeMap.put(key, node);
            }
            if (block.getPreviousBlockHashcode() != null) {//otherwise it is the Genesis block
                List<Byte> previousBlockKey = generateKey(block.getPreviousBlockHashcode());
                Node<Block> parentNode = hashcodeMap.get(previousBlockKey);
                if (parentNode != null){ 
                    parentNode.addChild(node);
                }
            }    
        }
        //we return the root node of the tree which contains the Genesis block
        //we assume that the first block in time is the Genesis block
        return node;
    }
    
    //I think that this is not needed
    Map<List<Byte>, BlockNode> constructMap(){
        Iterator<Block> iterToConstructMap = blocksInTime.iterator();
        Map<List<Byte>, BlockNode> hashcodeMap = new HashMap<List<Byte>, BlockNode>();
        while (iterToConstructMap.hasNext()) {
            Block block = iterToConstructMap.next();
            hashcodeMap.put(generateKey(block), new BlockNode(block));
        }        
        return hashcodeMap;
    }
    
    static List<Byte> generateKey(Block block){
        byte[] bytes = ByteHashcodeProvider.provide(block);
        return generateKey(bytes);
    }
    
    static List<Byte> generateKey(byte[] bytes){
        List<Byte> key = new ArrayList<Byte>();
        for (int i=0; i < bytes.length; i++){
            key.add(bytes[i]);
        }
        return key;
    }

    public Block getBlock(List<Byte> byteHashcode){
        return hashcodeMap.get(byteHashcode).getData();
    }    
    
    public Block getBlock(byte[] byteHashcode){
        return hashcodeMap.get(generateKey(byteHashcode)).getData();
    }  
}
