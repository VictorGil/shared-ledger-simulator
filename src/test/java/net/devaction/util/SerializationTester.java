package net.devaction.util;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.transaction.RandomTransactionProvider;
import net.devaction.sharedledgersimulator.transaction.UnsignedTransaction;
import net.devaction.util.serialization.Deserializer;
import net.devaction.util.serialization.Serializer;

/**
 * @author Víctor Gil
 */
public class SerializationTester{
    private static final Log log = LogFactory.getLog(SerializationTester.class);
    
    public static void main(String[] args){
        byte[] sourceAddress = new byte[20];        
        //Random random = new Random(); 
        //random.nextBytes(sourceAddress);
        ThreadLocalRandom.current().nextBytes(sourceAddress);
        
        byte[] publicKey = new byte[120];
        ThreadLocalRandom.current().nextBytes(publicKey);
        
        UnsignedTransaction unsignedTransaction = new UnsignedTransaction(sourceAddress, 
                RandomTransactionProvider.createAddressesAndAmounts(2), publicKey);
        log.info("Unsigned transaction: " + unsignedTransaction);
        byte[] bytes = Serializer.serialize(unsignedTransaction);
        log.info("Serialization bytes: " + Arrays.toString(bytes));
        log.info("Serialization bytes length: " + bytes.length);
        
        UnsignedTransaction deserializedUT = Deserializer.createObject(bytes, UnsignedTransaction.class);
        log.info("\nDeserialized unsigned transaction: " + deserializedUT);
        log.info("Are the two unsigned transactions equal?: " + unsignedTransaction.equals(deserializedUT));        
    }
    

}
