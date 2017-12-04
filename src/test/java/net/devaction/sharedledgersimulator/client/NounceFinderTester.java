package net.devaction.sharedledgersimulator.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.block.Block;

/**
 * @author Víctor Gil
 */
public class NounceFinderTester{
    private static final Log log = LogFactory.getLog(NounceFinderTester.class);
    
    public static void main(String[] args){
        Block genesis = BlockTreeConstructorTester.provideRandomGenesisBlock();    
        Block block1 = BlockTreeConstructorTester.provideRandomBlock(genesis);        
        log.info("Initial nounce: " + block1.getNounce());
        
        block1 = NounceFinder.find(block1);
        log.info("Final nounce: " + block1.getNounce());        
    }    
}
