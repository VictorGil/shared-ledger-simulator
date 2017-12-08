package net.devaction.sharedledgersimulator.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.sharedledgersimulator.client.transaction.AddressAndAmountListGenerator;
import net.devaction.sharedledgersimulator.key.KeyPair;
import net.devaction.sharedledgersimulator.network.BlocksInTime;
import net.devaction.sharedledgersimulator.rsa.RsaKeyPairProvider;
import net.devaction.sharedledgersimulator.rsa.client.RsaThinClient;
import net.devaction.sharedledgersimulator.threading.FullNodeController;
import net.devaction.sharedledgersimulator.transaction.AddressAndAmount;
import net.devaction.sharedledgersimulator.transaction.TransactionsInBlock;
import net.devaction.sharedledgersimulator.transaction.ValidTransactionsInChainProvider;

/**
 * @author Víctor Gil
 */
public class FullNodeAndThinClientTester{
    private static final Log log = LogFactory.getLog(FullNodeAndThinClientTester.class);
    
    private static final RsaKeyPairProvider keyPairProvider = RsaKeyPairProvider.getInstance();
    private static final BlocksInTime blocksInTime = BlocksInTime.getInstance();
    
    public static void main(String[] args) throws InterruptedException{
        KeyPair genesisKeyPair = keyPairProvider.provideRandomKeyPair();
        TransactionsInBlock genesisTransactions = 
                ValidTransactionsInChainProvider.constructTransactionsInGenesisBlock(genesisKeyPair);
        
        Block genesisBlock = new Block(genesisTransactions.getTransactions(), null, genesisKeyPair.getAddress());
        genesisBlock = NounceFinder.find(genesisBlock);        
        blocksInTime.add(genesisBlock);
        
        KeyPair keyPairFullNode1 = keyPairProvider.provideRandomKeyPair();
        byte[] addressFullNode1 = keyPairFullNode1.getAddress();
        FullNode fullNode1 = new FullNode(addressFullNode1);
        
        KeyPair keyPairFullNode2 = keyPairProvider.provideRandomKeyPair();
        byte[] addressFullNode2 = keyPairFullNode2.getAddress();
        FullNode fullNode2 = new FullNode(addressFullNode2);
        
        FullNodeController controller1 = new FullNodeController(fullNode1, "fullNodeController1", 18);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(controller1);       
        
        Thread.sleep(3650);

        FullNodeController controller2 = new FullNodeController(fullNode2, "fullNodeController2", 14);
        executor.submit(controller2);
        executor.shutdown();
        
        Thread.sleep(1650);
                
        ThinClient client1 = new RsaThinClient(genesisKeyPair);
        List<AddressAndAmount> aaList1 = AddressAndAmountListGenerator.generate(9, 5000);
        KeyPair keyPair1 = keyPairProvider.provideRandomKeyPair();
        AddressAndAmount aa1 = new AddressAndAmount(keyPair1.getAddress(), 4994);//the fee for the miner is 6
        aaList1.add(aa1);
        client1.submitTransaction(aaList1);
        
        Thread.sleep(4450);
        
        ThinClient client2 = new RsaThinClient(keyPair1);
        KeyPair keyPair2 = keyPairProvider.provideRandomKeyPair();
        KeyPair keyPair3 = keyPairProvider.provideRandomKeyPair();
        AddressAndAmount aa2 = new AddressAndAmount(keyPair2.getAddress(), 2990);
        AddressAndAmount  aa3 = new AddressAndAmount(keyPair3.getAddress(), 2000);//the fee for the miner is 4
        List<AddressAndAmount> aaList2 = new ArrayList<AddressAndAmount>();
        aaList2.add(aa2); aaList2.add(aa3);
        client2.submitTransaction(aaList2);
        
        try{
            executor.awaitTermination(1000, TimeUnit.DAYS);
        } catch (InterruptedException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }
        log.info("blocksInTime: " + blocksInTime);   
    }
}
