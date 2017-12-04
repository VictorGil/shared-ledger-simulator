package net.devaction.sharedledgersimulator.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.devaction.sharedledgersimulator.key.KeyPair;
import net.devaction.sharedledgersimulator.rsa.RsaKeyPairProvider;
import net.devaction.util.serialization.Serializer;

/**
 * @author Víctor Gil
 */
public class RandomTransactionProvider{
    
    public static Transaction provide(){        
        RsaKeyPairProvider keyPairProvider = RsaKeyPairProvider.getInstance();
        KeyPair keyPair = keyPairProvider.provideRandomKeyPair();
        
        UnsignedTransaction unsignedTransaction = createUnsignedTransaction(keyPair.getAddress(), 
                keyPair.getPublicKey());
        byte[] unsignedTransactionBytes = Serializer.serialize(unsignedTransaction); 
        byte[] stampedSignature = keyPair.sign(unsignedTransactionBytes);
        
        return new Transaction(unsignedTransaction, stampedSignature);
    }
    
    public static UnsignedTransaction createUnsignedTransaction(byte[] sourceAddress, byte[] publicKey){               
        ThreadLocalRandom.current().nextBytes(sourceAddress);
        return new UnsignedTransaction(sourceAddress, createAddressesAndAmounts(2), publicKey);
    }
    
    public static Collection<AddressAndAmount> createAddressesAndAmounts(int size){
        List<AddressAndAmount> list =  new ArrayList<AddressAndAmount>(size);
        for (int i = 0; i<size; i++){
            list.add(createRandomAddressAndAmount());
        }
        return list;
    }
    
    public static AddressAndAmount createRandomAddressAndAmount(){
        //the amount is a long but for this test and integer will do.
        int randomAmount = ThreadLocalRandom.current().nextInt(0, 1000 + 1);
        byte[] targetAddress = new byte[20];
        ThreadLocalRandom.current().nextBytes(targetAddress);
        return new AddressAndAmount(targetAddress, randomAmount);
    }
}
