package net.devaction.sharedledgersimulator.tree;

import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.sharedledgersimulator.block.ByteHashcodeProvider;

/**
 * @author Víctor Gil
 */
public class BlockNode extends Node<Block>{

    //This is just for testing
    //it has not real function
    //useful when printing the tree
    private final String id;
    
    public BlockNode(Block block, String id){
        super(block);
        this.id = id;
    }
    
    public BlockNode(Block block){        
        super(block);
        byte[] hashcode = ByteHashcodeProvider.provide(block);
        id = String.format("%02x", hashcode[hashcode.length - 2]) + 
                String.format("%02x", hashcode[hashcode.length - 1]);
    }

    
    public boolean isGenesis(){
        if (data.getPreviousBlockHashcode() == null)
            return true;
        return false;
    }
    
    public void print(){
        print("", true);
    }
    
    private void print(String prefix, boolean isLeaf) { 
        if (isGenesis())
            System.out.println("    " + id + " (" + length + ")");
        else
            System.out.println(prefix + (isLeaf ? "└── " : "├── ") + id + " (" + length + ")");
        for (int i = 0; i < children.size() - 1; i++) {
            ((BlockNode) children.get(i)).print(prefix + (isLeaf ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            ((BlockNode) children.get(children.size() - 1))
                    .print(prefix + (isLeaf ? "    " : "│   "), true);
        }
    }

    public String getId() {
        return id;
    }
}
