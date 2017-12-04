package net.devaction.sharedledgersimulator.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.Assert;

import org.junit.Test;

import net.devaction.sharedledgersimulator.transaction.AddressAndAmount;
import net.devaction.sharedledgersimulator.transaction.Transaction;
import net.devaction.sharedledgersimulator.transaction.UnsignedTransaction;

public class RewardAndFeeOutputsTest{
    private static final Log log = LogFactory.getLog(RewardAndFeeOutputsTest.class);
    
    @Test
    public void testAdd1(){
        byte[] minerAddress = new byte[20];        
        ThreadLocalRandom.current().nextBytes(minerAddress);
        
        AddressAndAmount rewardOutput = new AddressAndAmount(minerAddress, 50);
        AddressAndAmount feeOutput = new AddressAndAmount(minerAddress, 22);        
        
        List<AddressAndAmount> outputList =  new ArrayList<AddressAndAmount>(2);
        outputList.add(rewardOutput);
        outputList.add(feeOutput);
        UnsignedTransaction unsignedTransaction = new UnsignedTransaction(null, outputList, null);
        Transaction transaction = new Transaction(unsignedTransaction, null);
                
        RewardAndFeeOutputs rewardAndFeeOutputs = new RewardAndFeeOutputs();
        boolean correct = rewardAndFeeOutputs.add(transaction, minerAddress, 50);
        Assert.assertTrue(correct);
        Assert.assertEquals(22, rewardAndFeeOutputs.getFee());
    }
    
    @Test
    public void testAdd2(){
        byte[] minerAddress = new byte[20];        
        ThreadLocalRandom.current().nextBytes(minerAddress);
        
        AddressAndAmount rewardOutput = new AddressAndAmount(minerAddress, 49);
        AddressAndAmount feeOutput = new AddressAndAmount(minerAddress, 21);        
        
        List<AddressAndAmount> outputList =  new ArrayList<AddressAndAmount>(2);
        outputList.add(rewardOutput);
        outputList.add(feeOutput);
        UnsignedTransaction unsignedTransaction = new UnsignedTransaction(null, outputList, null);
        Transaction transaction = new Transaction(unsignedTransaction, null);
                
        RewardAndFeeOutputs rewardAndFeeOutputs = new RewardAndFeeOutputs();
        boolean correct = rewardAndFeeOutputs.add(transaction, minerAddress, 50);
        Assert.assertFalse(correct);
    }
    
    @Test
    public void testAdd3(){
        byte[] minerAddress = new byte[20];        
        ThreadLocalRandom.current().nextBytes(minerAddress);
        
        AddressAndAmount rewardOutput = new AddressAndAmount(minerAddress, 50);
        AddressAndAmount feeOutput = new AddressAndAmount(minerAddress, 21);        
        AddressAndAmount spureousOutput = new AddressAndAmount(minerAddress, 8); 
        
        List<AddressAndAmount> outputList =  new ArrayList<AddressAndAmount>(2);
        outputList.add(rewardOutput);
        outputList.add(feeOutput);
        outputList.add(spureousOutput);
        UnsignedTransaction unsignedTransaction = new UnsignedTransaction(null, outputList, null);
        Transaction transaction = new Transaction(unsignedTransaction, null);
                
        RewardAndFeeOutputs rewardAndFeeOutputs = new RewardAndFeeOutputs();
        boolean correct = rewardAndFeeOutputs.add(transaction, minerAddress, 50);
        Assert.assertFalse(correct);
    }
}
