package net.devaction.sharedledgersimulator.rsa.client;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.client.ThinClient;
import net.devaction.sharedledgersimulator.key.KeyPair;
import net.devaction.sharedledgersimulator.network.TransactionsInTime;
import net.devaction.sharedledgersimulator.rsa.RsaKeyPairProvider;
import net.devaction.sharedledgersimulator.transaction.AddressAndAmount;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.sharedledgersimulator.transaction.UnsignedTransaction;
import net.devaction.util.serialization.Serializer;

/**
 * @author Víctor Gil
 */
public class RsaThinClient implements ThinClient{
    private static final Log log = LogFactory.getLog(RsaThinClient.class);
    
    private final KeyPair keyPair;
    private final TransactionsInTime transactionsInTime;
    
    public RsaThinClient(){
        keyPair = RsaKeyPairProvider.getInstance().provideRandomKeyPair();
        transactionsInTime = TransactionsInTime.getInstance();
    }
    
    public RsaThinClient(KeyPair keyPair){
        this.keyPair = keyPair; 
        transactionsInTime = TransactionsInTime.getInstance();
    }
    
    @Override
    public void submitTransaction(Collection<AddressAndAmount> addressesAndTransactionsCollection){
        Transaction transaction = createTransaction(addressesAndTransactionsCollection);
        log.debug("Transaction created: " + transaction);
        transactionsInTime.add(transaction);
    }
    
    Transaction createTransaction(Collection<AddressAndAmount> addressesAndTransactionsCollection){
        UnsignedTransaction unsignedTransaction = new UnsignedTransaction( 
                keyPair.getAddress(), addressesAndTransactionsCollection, 
                keyPair.getPublicKey());
        byte[] stampedSignature = keyPair.sign(Serializer.serialize(unsignedTransaction));
        return new Transaction(unsignedTransaction, stampedSignature);
    }
}
