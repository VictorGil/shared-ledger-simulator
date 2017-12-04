package net.devaction.sharedledgersimulator.block;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.util.PropertiesProvider;
import net.devaction.util.serialization.Serializer;

/**
 * @author Víctor Gil
 */
public class ByteHashcodeProvider{
    private static final Log log = LogFactory.getLog(ByteHashcodeProvider.class);
    
    private static final MessageDigest DIGEST = getMessageDigest();
    
    private static MessageDigest getMessageDigest(){
        String hashingAlgorithm = PropertiesProvider.get("hashing.algorithm");
        MessageDigest messageDigest = null;
        try{
           messageDigest = MessageDigest.getInstance(hashingAlgorithm);
        } catch(NoSuchAlgorithmException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }
        return messageDigest;
    }
    
    public static byte[] provide(Block block){        
        return DIGEST.digest(Serializer.serialize(block));
    }
    
    public static String byteHashcodeToString(byte[] hashcode) {
        return String.format("%02x", hashcode[hashcode.length - 2]) + 
                String.format("%02x", hashcode[hashcode.length - 1]);
    }
    
}
