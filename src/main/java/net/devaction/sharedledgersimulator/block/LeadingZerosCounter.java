package net.devaction.sharedledgersimulator.block;

/**
 * @author Víctor Gil
 */
public class LeadingZerosCounter{
    
    public static int count(byte[] bytes){
        int zerosCount = 0;
        for (int i=0; i<bytes.length; i++){
            int zerosCountInByte = count(bytes[i]);
            zerosCount = zerosCount + zerosCountInByte;
            if (zerosCountInByte < 8)
                return zerosCount;                  
        }
        return zerosCount;
    }
    
    //I copied this from:
    //https://github.com/ryancwilliams/JHashCash/blob/master/JHashCash/src/com/github/ryancwilliams/JHashCash/utils/ByteUtils.java
    public static int count(byte valueByte) {
        //Convert the byte to a unsigned int
        int value = valueByte;
        if (value < 0) {
            value += 256;
        }
        //count the zeros
        if (value >= 0x80) {
            return 0;
        } else if (value >= 0x40) {
            return 1;
        } else if (value >= 0x20) {
            return 2;
        } else if (value >= 0x10) {
            return 3;
        } else if (value >= 0x08) {
            return 4;
        } else if (value >= 0x04) {
            return 5;
        } else if (value >= 0x02) {
            return 6;
        } else if (value >= 0x01) {
            return 7;
        } else {
            return 8;
        }
    }
}
