package net.devaction.sharedledgersimulator.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.sharedledgersimulator.block.ByteHashcodeProvider;
import net.devaction.sharedledgersimulator.block.LeadingZerosCounter;
import net.devaction.util.PropertiesProvider;

/**
 * @author Víctor Gil
 */
public class NounceFinder{    
    private static final Log log = LogFactory.getLog(NounceFinder.class);
    
    private static final int ZEROS = Integer.parseInt(PropertiesProvider.get("leading.zeros"));
    
    public static Block find(Block block){
        long numberOfAttempts = 0;
        while(!enoughNumberOfLeadingZeros(block)){
            long nounce = block.getNounce();
            block.setNounce(++nounce);
            numberOfAttempts++;
        }
        log.debug(ZEROS + " leading zeros were achieved after " + numberOfAttempts + " attempts");
        return block;
    }
    
    public static boolean enoughNumberOfLeadingZeros(Block block){
        int zeros = LeadingZerosCounter.count(ByteHashcodeProvider.provide(block));
        log.trace("Number of leading zeros: " + zeros);
        return zeros >= ZEROS;
    }
}
