package net.devaction.sharedledgersimulator.rsa;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.key.Verifier;

/**
 * @author Víctor Gil
 */
public class RsaVerifier implements Verifier{
    private static final Log log = LogFactory.getLog(RsaVerifier.class);
    private static RsaVerifier INSTANCE;
    
    public static RsaVerifier getInstance(){
        if (INSTANCE == null)
            INSTANCE = new RsaVerifier();
        return INSTANCE;        
    }
    
    private RsaVerifier(){
    }
    
    //this method cannot be declared as static because it is part 
    //of the Verifier API
    @Override
    public boolean verify(byte[] data, byte[] stampedSignature, byte[] publicKey){
        Signature signature;
        try{
           signature = Signature.getInstance("SHA256withRSA");
        } catch(NoSuchAlgorithmException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }
        KeyFactory keyFactory = RsaKeyPair.getKeyFactory();
        PublicKey publicKeyObject;
        boolean isValidSignature;
        try{
            publicKeyObject = keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));
            signature.initVerify(publicKeyObject);
            signature.update(data);
            isValidSignature = signature.verify(stampedSignature);
        } catch(InvalidKeySpecException | InvalidKeyException | SignatureException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }
        return isValidSignature;
    }
}
