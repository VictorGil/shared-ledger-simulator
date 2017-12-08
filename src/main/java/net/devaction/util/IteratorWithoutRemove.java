package net.devaction.util;

import java.util.Iterator;

/**
 * @author Víctor Gil
 */
public class IteratorWithoutRemove<T> implements Iterator<T>{
    private Iterator<T> iterator;
    
    public IteratorWithoutRemove(Iterator<T> iterator){
        this.iterator = iterator;
    }
    
    @Override
    public T next() {
        return iterator.next();
        
    }
    
    
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    } 
}
