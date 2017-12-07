package net.devaction.sharedledgersimulator.client.transaction;

import java.util.ArrayList;
import java.util.List;

import net.devaction.sharedledgersimulator.key.KeyPair;
import net.devaction.sharedledgersimulator.rsa.RsaKeyPairProvider;
import net.devaction.sharedledgersimulator.transaction.AddressAndAmount;

/**
 * @author Víctor Gil
 */
public class AddressAndAmountListGenerator{
    private static final RsaKeyPairProvider keyPairProvider = RsaKeyPairProvider.getInstance();
    
    //all the amount is the same for all the AddressAndAmount objects generated
    public static List<AddressAndAmount> generate(int count, long amount){
        List<AddressAndAmount> list = new ArrayList<AddressAndAmount>(count);
        
        for (int i = 0; i<count; i++){
            KeyPair keyPair = keyPairProvider.provideRandomKeyPair();
            AddressAndAmount aa = new AddressAndAmount(keyPair.getAddress(), amount);
            list.add(aa);
        }        
        return list;
    }
}
