package net.devaction.sharedledgersimulator.rsa.exploration;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Víctor Gil
 */
public class KeyGenerationTester{
    private static final Log log = LogFactory.getLog(KeyGenerationTester.class);
    
    public static void main(String[] args){
        KeyPairGenerator kpg = null;
        try{
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch(NoSuchAlgorithmException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }
        kpg.initialize(1024);
        KeyPair keyPair = kpg.generateKeyPair();
        Key privateKey = keyPair.getPrivate();
        Key publicKey = keyPair.getPublic();
        
        Base64.Encoder encoder = Base64.getEncoder();
        String privateKeyBase64Str = encoder.encodeToString(privateKey.getEncoded());
        log.info("Private key in Base64 format:\n" + privateKeyBase64Str);//it creates 1623 chars or 1620 chars
        
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] privateKeyBytes = decoder.decode(privateKeyBase64Str);
        log.info("The private Key is " + privateKeyBytes.length + " bytes long");
        String privateKeyHex = String.format("%040x", new BigInteger(1, privateKeyBytes));
        log.info("The private key in hexadecimal digits:\n" + privateKeyHex);
        
        
        String publicKeyBase64Str = encoder.encodeToString(publicKey.getEncoded());
        log.info("Public key in Base64 format:\n" + publicKeyBase64Str);//it creates 392 chars and again 392 chars for 2048 bits
                                                                        //it creates 162 bytes for 1024 bits, an Etherum address is 20 bytes (40 hexadecimal digits/characters long)
                                                                        //324 hexadecimal characters, and we use the last 40 as the Etherum address
        byte[] publicKeyBytes = decoder.decode(publicKeyBase64Str);
        log.info("The public Key is " + publicKeyBytes.length + " bytes long");
        String publicKeyHex = String.format("%040x", new BigInteger(1, publicKeyBytes));
        log.info("The public key in hexadecimal digits:\n" + publicKeyHex);
    }
}
