package net.devaction.sharedledgersimulator.tree;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Víctor Gil
 */
public class DFSTraversalTester{
    private static final Log log = LogFactory.getLog(DFSTraversalTester.class);
    
    public static void main(String[] args){
       Node<String> root = constructTree();
       NodesOrderedByLength<String> nodes = DFSTraversal.traverseAddingLength(root);
       log.info("Nodes:\n" + nodes);
       Iterator<Node<String>> iter = nodes.iterator();
       while(iter.hasNext())
           log.info(iter.next());
    }
    
    static Node<String> constructTree(){
        Node<String> nodeF = new Node<String>("F"); 
        Node<String> nodeG = new Node<String>("G");
        Node<String> nodeE = new Node<String>("E");
        nodeE.addChild(nodeF);
        nodeE.addChild(nodeG);
        
        Node<String> nodeB = new Node<String>("B");
        nodeB.addChild(nodeE);
        
        Node<String> nodeA = new Node<String>("A");
        nodeA.addChild(nodeB);
        
        Node<String> root = new Node<String>("root");
        root.addChild(nodeA);
        
        Node<String> nodeD = new Node<String>("D");
        Node<String> nodeC = new Node<String>("C");
        nodeC.addChild(nodeD);        
        root.addChild(nodeC);
        
        Node<String> nodeK = new Node<String>("K");
        Node<String> nodeJ = new Node<String>("J");
        nodeJ.addChild(nodeK);
        
        Node<String> nodeI = new Node<String>("I");
        nodeI.addChild(nodeJ);
        
        Node<String> nodeH = new Node<String>("H");
        nodeH.addChild(nodeI);
        
        nodeC.addChild(nodeH);        
        
        return root;        
    }
}
