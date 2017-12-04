package net.devaction.sharedledgersimulator.tree;

import java.util.Iterator;
import java.util.Stack;

/**
 * @author Víctor Gil
 */
public class DFSTraversal{
    
    public static <T extends Comparable<T>> NodesOrderedByLength<T> traverseAddingLength(Node<T> root){
        NodesOrderedByLength<T> nodesOrdered = new NodesOrderedByLength<T>(); 
        Stack<Node<T>> stack = new Stack<Node<T>>();
        root.setLength(0);
        stack.add(root);
        while(!stack.isEmpty()){            
            Node<T> node = stack.pop();
            long length = node.getLength();
            Iterator<Node<T>> childrenIterator = node.childrenIterator();
            while(childrenIterator.hasNext()){
                Node<T> child = childrenIterator.next();
                child.setLength(length+1);
                stack.add(child);
            }
            nodesOrdered.add(node);
        }        
        return nodesOrdered;
    }
}
