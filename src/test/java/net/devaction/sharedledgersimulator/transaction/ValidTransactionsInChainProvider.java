package net.devaction.sharedledgersimulator.transaction;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.key.KeyPair;
import net.devaction.sharedledgersimulator.rsa.RsaKeyPairProvider;
import net.devaction.util.PropertiesProvider;
import net.devaction.util.serialization.Serializer;

/**
 * @author Víctor Gil
 */
public class ValidTransactionsInChainProvider{
private static final Log log = LogFactory.getLog(ValidTransactionsInBlockProvider.class);
    
    private static final long REWARD = Long.parseLong(PropertiesProvider.get("reward.per.block"));
    private static final RsaKeyPairProvider keyPairProvider = RsaKeyPairProvider.getInstance();
    
    public static List<TransactionsInBlock> provide(){
        List<TransactionsInBlock> transactionsInBlockList = new ArrayList<TransactionsInBlock>();
        
        //GENESIS BLOCK
        KeyPair genesisKeyPair = keyPairProvider.provideRandomKeyPair();
        TransactionsInBlock transactionsInGenesisBlock = constructTransactionsInGenesisBlock(genesisKeyPair);
        transactionsInBlockList.add(transactionsInGenesisBlock);
        
        //BLOCK 2
        KeyPair keyPair1 = keyPairProvider.provideRandomKeyPair();
        KeyPair keyPair2 = keyPairProvider.provideRandomKeyPair();
        TransactionsInBlock transactionsInBlock2 = constructTransactionsInBlock2(genesisKeyPair, 
                keyPair1, keyPair2);
        transactionsInBlockList.add(transactionsInBlock2);
        
        //BLOCK 3
        TransactionsInBlock transactionsInBlock3 = constructTransactionsInBlock3(keyPair1, keyPair2);
        transactionsInBlockList.add(transactionsInBlock3); 
        
        return transactionsInBlockList;
    }
    
    static TransactionsInBlock constructTransactionsInGenesisBlock(KeyPair genesisKeyPair){

        AddressAndAmount outputReward1 = new AddressAndAmount(genesisKeyPair.getAddress(), REWARD);
        List<AddressAndAmount> addressesAndAmountsGenesis = new ArrayList<AddressAndAmount>(); 
        addressesAndAmountsGenesis.add(outputReward1);
        
        UnsignedTransaction unsignedGenesisTx = new UnsignedTransaction(
                null,
                addressesAndAmountsGenesis,
                null);
        Transaction txGenesis = new Transaction(unsignedGenesisTx, null);
        List<Transaction> txGenesisList = new ArrayList<Transaction>();
        txGenesisList.add(txGenesis);
        TransactionsInBlock  transactionsInGenesisBlock = new TransactionsInBlock(txGenesisList, 
                genesisKeyPair.getAddress());
        return transactionsInGenesisBlock;
    }
    
    static TransactionsInBlock constructTransactionsInBlock2(KeyPair genesisKeyPair, 
            KeyPair keyPair1, KeyPair keyPair2){
        //The sum of these amounts should not be higher than
        //the reward per block because we assume this is the first
        //transaction right after the genesis block
        long amount1 = 19999;
        long amount2 = 30000;
        if (amount1 + amount2 > REWARD){
            String errMessage = "Total oputput amount is higher than the REWARD: " + amount1 + " + "
                    + amount2 + " > " + REWARD; 
            log.error(errMessage);
            throw new AssertionError(errMessage);
        }
               
        AddressAndAmount output1 = new AddressAndAmount(keyPair1.getAddress(), amount1);        
        AddressAndAmount output2 = new AddressAndAmount(keyPair2.getAddress(), amount2);
                
        List<AddressAndAmount> addressesAndAmounts1 = new ArrayList<AddressAndAmount>(); 
        addressesAndAmounts1.add(output1);
        addressesAndAmounts1.add(output2);
        
        UnsignedTransaction unsignedTx1 = new UnsignedTransaction(
                genesisKeyPair.getAddress(),
                addressesAndAmounts1,
                genesisKeyPair.getPublicKey());
        byte[] stampedSignature1 = genesisKeyPair.sign(Serializer.serialize(unsignedTx1));
        Transaction tx1 = new Transaction(unsignedTx1, stampedSignature1);        
        List<Transaction> txList2 = new ArrayList<Transaction>();
        txList2.add(tx1);

        KeyPair keyPairMiner2 = keyPairProvider.provideRandomKeyPair();
        AddressAndAmount outputReward2 = new AddressAndAmount(keyPairMiner2.getAddress(), REWARD);
        List<AddressAndAmount> addressesAndAmountsMiner2 = new ArrayList<AddressAndAmount>(); 
        addressesAndAmountsMiner2.add(outputReward2);
        AddressAndAmount outputFee2 = new AddressAndAmount(keyPairMiner2.getAddress(), 1L);
        addressesAndAmountsMiner2.add(outputFee2);
        UnsignedTransaction unsignedTxMiner2 = new UnsignedTransaction(
                null,
                addressesAndAmountsMiner2,
                null);
        Transaction txMiner2 = new Transaction(unsignedTxMiner2, null);        
        txList2.add(txMiner2);
        
        TransactionsInBlock  transactionsInBlock2 = new TransactionsInBlock(txList2, 
                keyPairMiner2.getAddress());
        return transactionsInBlock2;
    }
    
    static TransactionsInBlock constructTransactionsInBlock3(KeyPair keyPair1, KeyPair keyPair2){
        //19999 --> 11000, 8998, 1 (fee)
        KeyPair keyPair3 = keyPairProvider.provideRandomKeyPair();
        KeyPair keyPair4 = keyPairProvider.provideRandomKeyPair();
        AddressAndAmount output3 = new AddressAndAmount(keyPair3.getAddress(), 11000);
        AddressAndAmount output4 = new AddressAndAmount(keyPair4.getAddress(), 8998);
        
        List<AddressAndAmount> addressesAndAmounts2 = new ArrayList<AddressAndAmount>();
        addressesAndAmounts2.add(output3);
        addressesAndAmounts2.add(output4);
        
        UnsignedTransaction unsignedTx2 = new UnsignedTransaction(
                keyPair1.getAddress(),
                addressesAndAmounts2,
                keyPair1.getPublicKey());
        byte[] stampedSignature2 = keyPair1.sign(Serializer.serialize(unsignedTx2));
        Transaction tx2 = new Transaction(unsignedTx2, stampedSignature2);
        List<Transaction> txList3 = new ArrayList<Transaction>();
        txList3.add(tx2);

        //third transaction 30000 --> 12000, 17997, 3 (fee)
        KeyPair keyPair5 = keyPairProvider.provideRandomKeyPair();
        KeyPair keyPair6 = keyPairProvider.provideRandomKeyPair();
        AddressAndAmount output5 = new AddressAndAmount(keyPair5.getAddress(), 12000);
        AddressAndAmount output6 = new AddressAndAmount(keyPair6.getAddress(), 17997);
        
        List<AddressAndAmount> addressesAndAmounts3 = new ArrayList<AddressAndAmount>();
        addressesAndAmounts3.add(output5);
        addressesAndAmounts3.add(output6);
        
        UnsignedTransaction unsignedTx3 = new UnsignedTransaction(
                keyPair2.getAddress(),
                addressesAndAmounts3,
                keyPair2.getPublicKey());
        byte[] stampedSignature3 = keyPair2.sign(Serializer.serialize(unsignedTx3));
        Transaction tx3 = new Transaction(unsignedTx3, stampedSignature3);
        txList3.add(tx3);
        
        //Third miner 
        KeyPair keyPairMiner3 = keyPairProvider.provideRandomKeyPair();
        List<AddressAndAmount> addressesAndAmountsMiner3 = new ArrayList<AddressAndAmount>();        
        AddressAndAmount miner3FeeOutput = new AddressAndAmount(keyPairMiner3.getAddress(), 1L + 3L);
        addressesAndAmountsMiner3.add(miner3FeeOutput);
        AddressAndAmount miner3RewardOutput = new AddressAndAmount(keyPairMiner3.getAddress(), REWARD);
        addressesAndAmountsMiner3.add(miner3RewardOutput);
        
        UnsignedTransaction unsignedTxMiner3 = new UnsignedTransaction(
                null,
                addressesAndAmountsMiner3,
                null);
        Transaction txMiner3 = new Transaction(unsignedTxMiner3, null);
        txList3.add(txMiner3);
        
        TransactionsInBlock  transactionsInBlock3 = new TransactionsInBlock(txList3, 
                keyPairMiner3.getAddress());
        return transactionsInBlock3;
    }    
}
