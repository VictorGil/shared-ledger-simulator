package net.devaction.sharedledgersimulator.key;

/**
 * @author Víctor Gil
 */
public interface KeyPairProvider{
    
    public KeyPair provideRandomKeyPair();
}
