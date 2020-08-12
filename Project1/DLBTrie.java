import java.io.*;
import java.util.*;

public class DLBTrie {

    private DLBNode root;           //head of trie
    
    //sets root to blank node
    public DLBTrie() {
        root = new DLBNode();       
    }

    // returns the head of the trie
    public DLBNode getRoot() {
        return root;
    }
    
    // adds each symbol in the word to the trie
	public void addtoTrie( String word ) {
       
        // if root is null, create a blank node
        if (root == null) { 
            root = new DLBNode();
        }
        
        // set curNode to root
        DLBNode current = root;
        
        // goes through each character in the word, and calls the addCharacter method
        for (int i = 0; i < word.length(); i++) {
            current = addCharacter( word.charAt(i), current);
        }
        
        // symbol representing the end of the word
        Character carrot = '^';			
		
		current.setSymbol(carrot);			        // Sets the symbol of the node to ^
		current.setWord( word);				       // Sets the word to the node's Word parameter
       
    }
   
    // method that adds each character to each node
	private static DLBNode addCharacter( Character symbol, DLBNode node ) {
        
        // if there is no Symbol in the node, set the character to its Symbol and set the new child to a blank node
        if (node.getSymbol() == null) {
            node.setSymbol(symbol);
            node.setChild(new DLBNode());
            node = node.getChild();
        }
        
        // if the character equals the node's Symbol, grab the child
        else if (symbol.equals(node.getSymbol())) {
            node = node.getChild();
        }   
        
        // if there is a Right sibling, grab it and pass it back 
        else if (node.getRightSib() != null) {
            node = node.getRightSib();
            node = addCharacter(symbol, node);
        }
        
        // else set the Right sibling to a blank node and grab it and pass it
        else {
            
            node.setRightSib(new DLBNode());
            node = node.getRightSib();
            node = addCharacter(symbol, node);
        }
        
        return node;
        
    }

    // checks to see if it is the terminal node or not
    public static boolean isCarrot(DLBNode node) {
		
		return (node.getSymbol().equals('^'));
            
    }

    // traverses the history trie or dictionary trie, whichever one is being passed
    public int traverse_trie(DLBNode node, String inputSoFar, int num_strings, String[] array) {

        int count = 0;
        
        // goes through each character in the prefix
        for (int i = 0; i < inputSoFar.length(); i++) {

            // gets Right sibling if root is not null, Symbols are not the same, and there is a Right sibling
            while (node.getSymbol() != null && inputSoFar.charAt(i) != node.getSymbol() && node.getRightSib() != null) {
                node = node.getRightSib();
            }

            // gets child if root is same as input and there is a child
            if (node.getSymbol() != null && node.getSymbol() == inputSoFar.charAt(i) && node.getChild() != null) {
                node = node.getChild();

                // if traversed through entire word so far, increment the count 
                if (i == inputSoFar.length() - 1) {
                    count++;
                }
            } 
            // else stop the for loop
            else {
                i = inputSoFar.length();
            }
            
        }

        // find the predictions and return the number of predictions in num_strings
        if (node.getSymbol() != null && count > 0) {
           num_strings = findPredictions(node, num_strings, array);
        }

       count = 0;

       return num_strings;
    }
    
    
    public static int findPredictions(DLBNode currentNode, int numPredictions, String[] arrayPredictions) {

        if (currentNode.getSymbol() != null && numPredictions > 0) {
            if (currentNode.getChild() != null) {
                numPredictions = findPredictions(currentNode.getChild(), numPredictions, arrayPredictions);
                
                // if 5 predictions found
                if (numPredictions == 0) {
                    return 0;
                }
            }
        
            // if its the end node, decrement the number of predictions
            if ((isCarrot(currentNode))) {

                boolean contains = false;

                // if word is already in array, set boolean to true so you don't add it
                for (int i = 0; i < arrayPredictions.length; i++) {

                    if (arrayPredictions[i] != null && arrayPredictions[i].equals(currentNode.getWord())) {
                        contains = true;
                    }
                }
               
                // only add word if it is not already in array
                if (!contains) {
                    arrayPredictions[arrayPredictions.length - numPredictions] = currentNode.getWord();
                    numPredictions--;
                }
                
            }

            if (currentNode.getRightSib() != null) {
                numPredictions = findPredictions(currentNode.getRightSib(), numPredictions, arrayPredictions);

                // if 5 predictions found
                if (numPredictions == 0) {
                    return 0;
                }
            }

            return numPredictions;
        }

        return numPredictions;
    }
    
}
