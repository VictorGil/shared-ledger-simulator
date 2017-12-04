package net.devaction.sharedledgersimulator.tree;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import net.devaction.util.IteratorWithoutRemove;

/**
 * @author Víctor Gil
 */
public class NodesOrderedByLength<T extends Comparable<T>>{
    private final SortedSet<Node<T>> synchSortedSet = Collections.synchronizedSortedSet(new TreeSet<Node<T>>());
    
    public void add(Node<T> node){
        synchSortedSet.add(node);
    }
    
    public IteratorWithoutRemove<Node<T>> iterator(){        
        return new IteratorWithoutRemove<Node<T>>(synchSortedSet.iterator()); 
    }
    
    public int size(){
        return synchSortedSet.size();
    }
    @Override
    public String toString(){
        return "NodesOrderedByLength[\n    " + 
                synchSortedSet + "]";
    }    
}
