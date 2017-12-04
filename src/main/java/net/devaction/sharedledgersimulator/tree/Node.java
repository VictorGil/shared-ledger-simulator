package net.devaction.sharedledgersimulator.tree;

import java.util.ArrayList;
import java.util.List;

import net.devaction.util.IteratorWithoutRemove;

/**
 * @author Víctor Gil
 */
public class Node<T extends Comparable<T>> implements Comparable<Node<T>>{
    protected T data;
    
    protected final List<Node<T>> children = new ArrayList<Node<T>>();
    //this is the distance to the 
    //genesis node
    protected long length = -1L;
    
    public Node(T data) {
        this.data = data;
    }
    
    public void addChild(Node<T> child){
        children.add(child);
    }
    public IteratorWithoutRemove<Node<T>> childrenIterator(){
        return new IteratorWithoutRemove<Node<T>>(children.iterator());    
    }
    
    @Override
    public int compareTo(Node<T> otherNode){
        //reverse order, nodes with higher  
        //length first
        //return Long.compare(otherNode.getLength(), length);
        if (length != otherNode.getLength())
            return Long.compare(otherNode.getLength(), length);
        return this.getData().compareTo(otherNode.getData());
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + (int) (length ^ (length >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (children == null){
            if (other.children != null)
                return false;
        } else if (!children.equals(other.children))
            return false;
        if (data == null){
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (length != other.length)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Node [data=" + data + ", length=" + length + "]";
    }

    public T getData() {
        return data;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
