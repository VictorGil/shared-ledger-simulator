package net.devaction.datasource.adt.tester1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.devaction.adt.TimeLine;

/**
 * @author Víctor Gil
 */
public class Inserter implements Runnable{
    private static final Log log = LogFactory.getLog(Inserter.class);
    
    private final TimeLine timeLine;
    private final int sleepInterval; 
    private final String id;
    private int initialValue;
    private int numOfInsertions;
    
    public Inserter(TimeLine timeLine, int sleepInterval, String id, 
            int initialValue, int numOfInsertions){
        this.timeLine = timeLine;
        this.sleepInterval = sleepInterval;
        this.id = id;
        this.initialValue = initialValue;
        this.numOfInsertions = numOfInsertions;
    }
    
    @Override
    public void run() {
        for (int i = 0; i<numOfInsertions; i++){
            int currentValue = initialValue + i;
            timeLine.add(initialValue + i);
            log.info(id + " just inserted " + currentValue);
            try{
                Thread.sleep(sleepInterval);
             }catch(InterruptedException ex){
                log.error(ex);
                throw new RuntimeException(ex);
             }
        }        
    }

}
