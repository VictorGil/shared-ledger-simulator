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
        long numberOfAttempts = 1;
        long nounce = block.getNounce();
        while(!enoughNumberOfLeadingZeros(block)){
            nounce = block.getNounce();
            block.setNounce(++nounce);
            log.debug("Nounce " + nounce + " did not work");
            numberOfAttempts++;
            try{
                Thread.sleep(100);
            } catch (InterruptedException ex){
                log.error(ex);
                throw new RuntimeException(ex);
            }
        }
        log.debug(ZEROS + " leading zeros were achieved after " + numberOfAttempts + " attempts."
                + " Successful nounce: " + nounce);
        return block;
    }
    
    public static boolean enoughNumberOfLeadingZeros(Block block){
        int zeros = LeadingZerosCounter.count(ByteHashcodeProvider.provide(block));
        log.trace("Number of leading zeros: " + zeros);
        return zeros >= ZEROS;
    }
}
