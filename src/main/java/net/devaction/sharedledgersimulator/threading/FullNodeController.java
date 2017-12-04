package net.devaction.sharedledgersimulator.threading;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.sharedledgersimulator.client.FullNode;

/**
 * @author Víctor Gil
 */
public class FullNodeController implements Runnable{
    private static transient final Log log = LogFactory.getLog(FullNodeController.class);
    
    private final FullNode fullNode;
    private final String id;
    private final int seconds;
    
    public FullNodeController(FullNode fullNode, String id, int seconds){
        this.fullNode = fullNode;
        this.id = id;
        this.seconds = seconds;        
    }
    
    @Override
    public void run(){
        log.info("Starting " + id);
        //(new Thread(fullNode)).start();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(fullNode);
        executor.shutdown();
        for (int i=0; i<seconds; i++){
            try{
                Thread.sleep(1000);
            } catch(InterruptedException ex){
                log.error(ex);
                throw new RuntimeException(ex);
            }
        }
        log.info("Stopping the full node of " + id);
        fullNode.stop();        
        try{
            //we wait until the full node stops
            future.get();
        } catch (InterruptedException | ExecutionException ex){
            log.error(ex, ex);
            throw new RuntimeException(ex);
        }
        log.info("Full node of " + id + " is stopped");
    }

}
