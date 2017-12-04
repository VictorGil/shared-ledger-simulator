package net.devaction.util.format;

import java.math.BigInteger;

/**
 * @author Víctor Gil
 */
public class Formatter{

    public static String toHex(byte[] bytes){
        return String.format("%040x", new BigInteger(1, bytes));
    }
    
    public static String[] toBin(byte[] bytes){
        String[] bits = new String[bytes.length];
        for (int i=0; i<bytes.length; i++) {
            bits[i] = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0'); 
        }
        return bits;
    }
    public static String toBin(byte byteArg){
        return String.format("%8s", Integer.toBinaryString(byteArg & 0xFF)).replace(' ', '0');
    }
}
