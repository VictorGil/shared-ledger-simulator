package net.devaction.sharedledgersimulator.rsa;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.key.KeyPair;
import net.devaction.util.format.Formatter;

/**
 * @author Víctor Gil
 */
public class RsaKeyPair implements KeyPair{
    private static final Log log = LogFactory.getLog(RsaKeyPair.class);
    
    private static final KeyFactory KEY_FACTORY = getKeyFactory();
    private final Signature signature;
    
    private final byte[] privateKey;
    private final byte[] publicKey;
    private final byte[] address;
    
    public RsaKeyPair(byte[] privateKey, byte[] publicKey){
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        
        //we copy the last 20 bytes of the public key into
        //the address
        address = new byte[20];
        int j = 0;
        for (int i=publicKey.length - 20; i<publicKey.length; i++){
           address[j] = publicKey[i];
           j++;
        }
        
        signature = getSignature();        
    }
    
    static KeyFactory getKeyFactory(){
        KeyFactory keyFactory = null;
        try{
            keyFactory = KeyFactory.getInstance("RSA");
        } catch(NoSuchAlgorithmException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }
        return keyFactory;
    }
    
    private Signature getSignature(){    
        Signature signature = null;
        try{
             signature = Signature.getInstance("SHA256withRSA");
        } catch(NoSuchAlgorithmException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }
        
        PrivateKey privateKeyObject;
        try{
            privateKeyObject = KEY_FACTORY.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
            signature.initSign(privateKeyObject);
        } catch(InvalidKeySpecException | InvalidKeyException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }        
        return signature;
    }
    
    @Override
    public byte[] sign(byte[] data){
        byte[] stampedSignature = null;
        try{
            signature.update(data);
            stampedSignature = signature.sign();;
        } catch(SignatureException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }
        return stampedSignature;
    }
    
    @Override
    public byte[] getPublicKey(){
        return publicKey;
    }

    @Override
    public byte[] getAddress(){        
        return address;
    }
    
    @Override
    public String toString(){
        String string = "{Private Key: " + Formatter.toHex(privateKey) + "\n" + 
                "private key number of bytes: " + privateKey.length + "\n" +
                "Public key: " + Formatter.toHex(publicKey) + "\n" +
                "public key number of bytes: " + publicKey.length + "\n" +
                "Address: " + Formatter.toHex(address) + "\n" +
                "address number of bytes: " + address.length + "}";        
        return string;
    }
}
