package net.devaction.sharedledgersimulator.rsa;

import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.key.KeyPair;
import net.devaction.sharedledgersimulator.key.KeyPairProvider;
import net.devaction.util.PropertiesProvider;

/**
 * @author Víctor Gil
 */
public class RsaKeyPairProvider implements KeyPairProvider{
    private static final Log log = LogFactory.getLog(RsaKeyPairProvider.class);
    
    private static RsaKeyPairProvider INSTANCE;
        
    private static final int MODULUS_SIZE = Integer.parseInt(PropertiesProvider.get("rsa.modulus.size"));
    private static final KeyPairGenerator KEY_PAIR_GENERATOR = getKeyPairGenerator();
    
    public static RsaKeyPairProvider getInstance(){
        if (INSTANCE == null)
            INSTANCE = new RsaKeyPairProvider();
        return INSTANCE;        
    }
    
    private RsaKeyPairProvider(){        
    }
    
    private static KeyPairGenerator getKeyPairGenerator(){
        KeyPairGenerator kpg = null;
        try{
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(MODULUS_SIZE);
        } catch(NoSuchAlgorithmException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }
        return kpg;
    }
    
    //this method cannot be declared as static because it is part 
    //of the KeyPairProvider API
    @Override
    public KeyPair provideRandomKeyPair(){
        java.security.KeyPair keyPair = KEY_PAIR_GENERATOR.generateKeyPair();
        Key privateKey = keyPair.getPrivate();
        Key publicKey = keyPair.getPublic();
        
        return new RsaKeyPair(privateKey.getEncoded(), publicKey.getEncoded());
    }
    
    
}
