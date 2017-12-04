package net.devaction.sharedledgersimulator.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.block.Block;
import net.devaction.sharedledgersimulator.key.KeyPair;
import net.devaction.sharedledgersimulator.network.BlocksInTime;
import net.devaction.sharedledgersimulator.rsa.RsaKeyPairProvider;
import net.devaction.sharedledgersimulator.threading.FullNodeController;
import net.devaction.sharedledgersimulator.transaction.TransactionsInBlock;
import net.devaction.sharedledgersimulator.transaction.ValidTransactionsInChainProvider;

/**
 * @author Víctor Gil
 */
public class FullNodeTester{
    private static final Log log = LogFactory.getLog(FullNodeTester.class);
    private static final RsaKeyPairProvider keyPairProvider = RsaKeyPairProvider.getInstance();
    private static final BlocksInTime blocksInTime = BlocksInTime.getInstance();
    
    public static void main(String[] args){
        KeyPair genesisKeyPair = keyPairProvider.provideRandomKeyPair();
        TransactionsInBlock genesisTransactions = 
                ValidTransactionsInChainProvider.constructTransactionsInGenesisBlock(genesisKeyPair);
        
        Block genesisBlock = new Block(genesisTransactions.getTransactions(), null, genesisKeyPair.getAddress());
        genesisBlock = NounceFinder.find(genesisBlock);        
        blocksInTime.add(genesisBlock);
        
        KeyPair keyPairFullNode1 = keyPairProvider.provideRandomKeyPair();
        byte[] addressFullNode1 = keyPairFullNode1.getAddress();
        FullNode fullNode1 = new FullNode(addressFullNode1);
        
        FullNodeController controller1 = new FullNodeController(fullNode1, "fullNodeController1", 10);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(controller1);
        executor.shutdown();
        try{
            future.get();
        } catch (InterruptedException | ExecutionException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }
        log.info("blocksInTime: " + blocksInTime);        
    }
}
