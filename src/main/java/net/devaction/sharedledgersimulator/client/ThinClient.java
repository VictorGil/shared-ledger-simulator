package net.devaction.sharedledgersimulator.client;

import java.util.Collection;

import net.devaction.sharedledgersimulator.transaction.AddressAndAmount;

/**
 * @author Víctor Gil
 */
public interface ThinClient{
    
    public void submitTransaction(Collection<AddressAndAmount> addressesAndTransactionsCollection);
}
