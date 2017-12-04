package net.devaction.sharedledgersimulator.transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Víctor Gil
 */
public class RandomTransactionsProvider{

    public static List<Transaction> provide(){
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(RandomTransactionProvider.provide());
        transactions.add(RandomTransactionProvider.provide());
        return transactions;
    }
}
