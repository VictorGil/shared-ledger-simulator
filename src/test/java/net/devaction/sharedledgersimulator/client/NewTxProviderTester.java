package net.devaction.sharedledgersimulator.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.key.KeyPair;
import net.devaction.sharedledgersimulator.network.TransactionsInTime;
import net.devaction.sharedledgersimulator.rsa.RsaKeyPairProvider;
import net.devaction.sharedledgersimulator.transaction.AddressAndAmount;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.sharedledgersimulator.transaction.TransactionsInBlock;
import net.devaction.sharedledgersimulator.transaction.UnsignedTransaction;
import net.devaction.sharedledgersimulator.transaction.ValidTransactionsInChainProvider;
import net.devaction.util.serialization.Serializer;

/**
 * @author Víctor Gil
 */
public class NewTxProviderTester{
    private static final Log log = LogFactory.getLog(NewTxProviderTester.class);
    
    public static void main(String[] args){
        RsaKeyPairProvider keyPairProvider = RsaKeyPairProvider.getInstance();
        
        //this is to generate the source address of the first transaction
        KeyPair keyPair0 = keyPairProvider.provideRandomKeyPair();
        
        KeyPair keyPair1 = keyPairProvider.provideRandomKeyPair();
        AddressAndAmount output1 = new AddressAndAmount(keyPair1.getAddress(), 1490);
        
        KeyPair keyPair2 = keyPairProvider.provideRandomKeyPair();
        AddressAndAmount output2 = new AddressAndAmount(keyPair2.getAddress(), 1999);
        
        List<AddressAndAmount> addressesAndAmounts1 = new ArrayList<AddressAndAmount>(); 
        addressesAndAmounts1.add(output1);
        addressesAndAmounts1.add(output2);
        
        UnsignedTransaction unsignedTx1 = new UnsignedTransaction(
                keyPair0.getAddress(),
                addressesAndAmounts1,
                keyPair0.getPublicKey());
        byte[] stampedSignature1 = keyPair0.sign(Serializer.serialize(unsignedTx1));
        Transaction tx1 = new Transaction(unsignedTx1, stampedSignature1);
        
        TransactionsInTime transactionsInTime = TransactionsInTime.getInstance();
        transactionsInTime.add(tx1);
        
        Map<List<Byte>, Long> balances = new HashMap<List<Byte>, Long>();
        
        List<Byte> balance1key = BlockTreeConstructor.generateKey(keyPair0.getAddress());
        balances.put(balance1key, 3500L); //so the fee is 11
        
        
        //this is to generate the source address of the second transaction
        KeyPair keyPair3 = keyPairProvider.provideRandomKeyPair();
        
        KeyPair keyPair4 = keyPairProvider.provideRandomKeyPair();
        AddressAndAmount output3 = new AddressAndAmount(keyPair4.getAddress(), 895);
        
        List<AddressAndAmount> addressesAndAmounts2 = new ArrayList<AddressAndAmount>(); 
        addressesAndAmounts2.add(output3);
        
        UnsignedTransaction unsignedTx2 = new UnsignedTransaction(
                keyPair3.getAddress(),
                addressesAndAmounts2,
                keyPair3.getPublicKey());
        byte[] stampedSignature2 = keyPair3.sign(Serializer.serialize(unsignedTx2));
        Transaction tx2 = new Transaction(unsignedTx2, stampedSignature2);
        
        transactionsInTime.add(tx2);
        
        List<Byte> balance2key = BlockTreeConstructor.generateKey(keyPair3.getAddress());
        balances.put(balance2key, 900L); //so the fee is 5
        
        
        List<TransactionsInBlock> transactionsInChain = ValidTransactionsInChainProvider.provide();
        ChainVerificationResult chainVerificationResult = new ChainVerificationResult(transactionsInChain, 
                balances);
        
        NewTxProvider newTxProvider = new NewTxProvider(); 
        
        SortedSet<TransactionWithFee> txWithFeeSet = newTxProvider.provideOrderedByFee(chainVerificationResult);
        log.info("New transactions with fee ordered:\n" + txWithFeeSet);
        
        NewTransactionsAndTotalFee transactions = newTxProvider.provideThoseWithHighestFee(chainVerificationResult);
        log.info("New transactions to be put in a new block:\n" + transactions);
    }
}
