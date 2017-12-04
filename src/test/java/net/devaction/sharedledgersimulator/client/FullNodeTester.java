package net.devaction.sharedledgersimulator.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.key.KeyPair;
import net.devaction.sharedledgersimulator.network.BlocksInTime;
import net.devaction.sharedledgersimulator.rsa.RsaKeyPairProvider;

/**
 * @author Víctor Gil
 */
public class FullNodeTester{
    private static final Log log = LogFactory.getLog(FullNodeTester.class);
    private static final RsaKeyPairProvider keyPairProvider = RsaKeyPairProvider.getInstance();
    private static final BlocksInTime blocksInTime = BlocksInTime.getInstance();
    
    public static void main(String[] args){
        KeyPair genesisKeyPair = keyPairProvider.provideRandomKeyPair();
        //TO DO
        blocksInTime.add(null);
        
        KeyPair keyPairFullNode1 = keyPairProvider.provideRandomKeyPair();
        byte[] addressFullNode1 = keyPairFullNode1.getAddress();
        FullNode fullNode1 = new FullNode(addressFullNode1);
        

        
    }
}
