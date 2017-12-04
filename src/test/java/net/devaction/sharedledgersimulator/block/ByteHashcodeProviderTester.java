package net.devaction.sharedledgersimulator.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.transaction.RandomTransactionProvider;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.util.format.Formatter;
import net.devaction.util.serialization.Serializer;

/**
 * @author Víctor Gil
 */
public class ByteHashcodeProviderTester{
    private static final Log log = LogFactory.getLog(ByteHashcodeProviderTester.class);
    
    public static void main(String[] args){
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(RandomTransactionProvider.provide());
        transactions.add(RandomTransactionProvider.provide());
        byte[] minerAddress = new byte[20];        
        ThreadLocalRandom.current().nextBytes(minerAddress);
        byte[] previousBlockByteHashcode = new byte[100];
        ThreadLocalRandom.current().nextBytes(previousBlockByteHashcode);
        Block block = new Block(transactions, previousBlockByteHashcode, minerAddress, 0);
        log.info("Random block:\n" + block);
        byte[] serializedBlock = Serializer.serialize(block);
        log.info("Length of the serialized block in bytes: " + serializedBlock.length);
        
        byte[] hashcodeBytes = ByteHashcodeProvider.provide(block);
        log.info("Hascode bytes: " + Arrays.toString(hashcodeBytes));
        log.info("Length of the hashcode in bytes: " + hashcodeBytes.length);        
        log.info("Number of leading zeros in the hascodeBytes: "  + LeadingZerosCounter.count(hashcodeBytes));
        //the byte range is from -128 to 127
        byte[] bytes = new byte[]{0, -128, 0, 0, 0};
        log.info("Bits: " + Arrays.toString(Formatter.toBin(bytes)));
        log.info("Number of leading zeros in " + Arrays.toString(bytes) + ": "  + LeadingZerosCounter.count(bytes));
    }
}
