package net.devaction.sharedledgersimulator.key;

/**
 * @author Víctor Gil
 */
public interface Verifier{
    
    public boolean verify(byte[] data, byte[] stampedSignature, byte[] publicKey);
}
