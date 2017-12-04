package net.devaction.sharedledgersimulator.key;

/**
 * @author Víctor Gil
 */
public interface KeyPair{

    public byte[] getPublicKey();
    public byte[] getAddress();
    public byte[] sign(byte[] data);
}
