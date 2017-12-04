package net.devaction.sharedledgersimulator.transaction;

import java.util.Comparator;

/**
 * @author Víctor Gil
 */
public class FeeComparator implements Comparator<Transaction>{

    @Override
    public int compare(Transaction transaction1, Transaction transaction2){
        //descending order, transactions with higher fees first
        //because we are greedy :-)
        //return Long.compare(transaction2.getFee(), transaction1.getFee());
        return 0;
    }
}
