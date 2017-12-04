package net.devaction.sharedledgersimulator.rsa;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import net.devaction.sharedledgersimulator.key.KeyPair;
import net.devaction.sharedledgersimulator.key.Verifier;
import net.devaction.util.format.Formatter;

/**
 * @author Víctor Gil
 */
public class RsaKeyPairProviderTest{
    private static final Log log = LogFactory.getLog(RsaKeyPairProviderTest.class);
    
    private RsaKeyPairProvider keyPairProvider = RsaKeyPairProvider.getInstance();
    
    @Test
    public void test01(){
        KeyPair keyPair = keyPairProvider.provideRandomKeyPair();
        log.info("Key pair:\n" + keyPair);
    }
    
    @Test
    public void test02SignAndVerify(){
        KeyPair keyPair = keyPairProvider.provideRandomKeyPair();
        log.info("Key pair:\n" + keyPair);
        byte[] data = generateRandomData();
        byte[] stampedSignature = keyPair.sign(data);
        Verifier verifier = RsaVerifier.getInstance();
        boolean isVerified = verifier.verify(data, stampedSignature, keyPair.getPublicKey());
        Assert.assertTrue(isVerified);
    }

    @Test
    public void test03SignAndVerify(){
        KeyPair keyPair = keyPairProvider.provideRandomKeyPair();
        log.info("Key pair:\n" + keyPair);
        byte[] data = generateRandomData();
        byte[] stampedSignature = keyPair.sign(data);
        Verifier verifier = RsaVerifier.getInstance();
        byte[] copyOfData = new byte[data.length]; 
        System.arraycopy(data, 0, copyOfData, 0, copyOfData.length);
        boolean isVerified = verifier.verify(copyOfData, stampedSignature, keyPair.getPublicKey());
        Assert.assertTrue(isVerified);
    }
    
    @Test
    public void test04SignAndVerify(){
        KeyPair keyPair = keyPairProvider.provideRandomKeyPair();
        log.info("Key pair:\n" + keyPair);
        byte[] data = generateRandomData();
        byte[] stampedSignature = keyPair.sign(data);
        Verifier verifier = RsaVerifier.getInstance();
        byte[] changedData = new byte[data.length]; 
        System.arraycopy(data, 0, changedData, 0, changedData.length);
        
        log.info("Copy of data: " + Arrays.toString(changedData));
        byte changedByte = changedData[changedData.length-1];
        log.info("Byte before: " + changedByte + " --> " + Formatter.toBin(changedByte));
        //we change/invert/toggle the least significant bit of the byte
        changedByte ^= 1 << 0;
        log.info("Byte after: " + changedByte + " --> " + Formatter.toBin(changedByte));
        changedData[changedData.length-1] = changedByte;
        log.info("Changed data: " + Arrays.toString(changedData));
        
        boolean isVerified = verifier.verify(changedData, stampedSignature, keyPair.getPublicKey());
        //it should return false because we have changed one bit
        Assert.assertFalse(isVerified);
    }
    
    private static byte[] generateRandomData(){
        Random random = new Random();
        int bitLength = 512;
        log.info("Going to create a random prime number with bit length: " + bitLength);    
        BigInteger prime = BigInteger.probablePrime(bitLength, random);
        return prime.toByteArray();
    }
}
