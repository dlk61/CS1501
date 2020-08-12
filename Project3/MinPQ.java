import java.util.NoSuchElementException;

public class MinPQ {
    private Car[] pq;                    // store items at indices 1 to n
    private int n;                       // number of items on priority queue  
    private boolean priceFlag;           //true if priceHeap, false if mileageHeap

    /**
     * Initializes an empty priority queue with the given initial capacity.
     *
     * @param  initCapacity the initial capacity of this priority queue
     * @param  bool the flag to determine if it is a priceHeap or mileageHeap
     */
    public MinPQ(int initCapacity, boolean bool) {
        pq = new Car[initCapacity + 1];
        n = 0;
        priceFlag = bool;
    }

    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /*
    * Returns a smallest key on this priority queue.
     *
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Car min() {
        if (isEmpty()) {
            return null;
        }
        else return pq[1];
    }

    //returns the car at heap index
    public Car getCar(int index) {
        return pq[index];
    }

    //set an updated car
    public void setCar(Car car, int index, TrieST trie) {
        pq[index] = car;        //place the updated car in its postion
        swim(index, trie);      //call swim method to maintain heap property
        assert isMinHeap();
        sink(index, trie);      //call sink method to maintain heap property
        assert isMinHeap();
    }

    /**
     * Adds a new key to this priority queue.
     *
     * @param  x the key to add to this priority queue
     */
    public void insert(Car x, TrieST trie) {
        // double size of array if necessary
        if (n == pq.length - 1) {
            resize(2 * pq.length);
        }
        // add x, and percolate it up to maintain heap invariant
        pq[++n] = x;
        if (n == 1) {
            trie.put(pq[n].getVin(), n);            //put the car's vin number and index in trie
        }
        else swim(n, trie);     
        assert isMinHeap();
    }

    //only used when using the heap for a linked list in the hash table. 
    //does not update the trie
    public void insert(Car x) {
        // double size of array if necessary
        if (n == pq.length - 1) {
            resize(2 * pq.length);
        }
        // add x, and percolate it up to maintain heap invariant
        pq[++n] = x;
        swim(n);     
        assert isMinHeap();
    }
    
    //only used when using the heap for a linked list in the hash table
    //does not update the trie
    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {                 //checks to see if parent is greater than current
            exch(k, k/2);                                  //if so, then swaps them
            k = k/2;                                       //sets current index as parent index
        }
    }

    // helper function to double the size of the heap array
    private void resize(int capacity) {
        assert capacity > n;
        Car[] temp = new Car[capacity];
        for (int i = 1; i <= n; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    public void remove(int index, TrieST trie) {
        trie.delete(pq[n].getVin());               //delete last one from trie
        pq[index] = pq[n];                         //put whatever is in the last spot into the car that is being removed's postion

        //if the car to delete is the last in the heap...
        if (index == n) {
            pq[index] = null;                       //set last car to null
            n--;                                    //decrement n
            return;
        }
        else {
            trie.put(pq[index].getVin(), index);    //update the car's index in the trie  
            pq[n] = null;                           //set to null
            n--;                                    //decrement n
            sink(index, trie);                      //maintain min heap order
        }
    }


       /***************************************************************************
    * Helper functions to restore the heap invariant.
    ***************************************************************************/

    private void swim(int k, TrieST trie) {
        if (k > 1 && (!greater(k/2, k))){
            trie.put(pq[k].getVin(), k);                   //add the car's vin number and its index to its trie
        }
        while (k > 1 && greater(k/2, k)) {                 //checks to see if parent is greater than current
            exch(k, k/2);                                  //if so, then swaps them
            trie.put(pq[k].getVin(), k);                   //adds current car's index in trie
            trie.put(pq[k/2].getVin(), k/2);               //updates the car's parent index in trie
            k = k/2;                                       //sets current index as parent index
        }
    }

    private void sink(int k, TrieST trie) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            trie.put(pq[k].getVin(), k);                   //updates current car's index in trie
            trie.put(pq[j].getVin(), j);                   //updates parent's index in trie
            k = j;
            
        }
    }

   /***************************************************************************
    * Helper functions for compares and swaps.
    ***************************************************************************/
    private boolean greater(int i, int j) {
        if (priceFlag == true) {
            return (pq[i].getPrice() - pq[j].getPrice() > 0);
        }
        else {
            return (pq[i].getMileage() - (pq[j].getMileage()) > 0);
        }
    }
    

    private void exch(int i, int j) {           //swaps the parent and current 
        Car swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    // is pq[1..n] a min heap?
    private boolean isMinHeap() {
        for (int i = 1; i <= n; i++) {
            if (pq[i] == null) return false;
        }
        for (int i = n+1; i < pq.length; i++) {
            if (pq[i] != null) return false;
        }
        if (pq[0] != null) return false;
        return isMinHeapOrdered(1);
    }

    // is subtree of pq[1..n] rooted at k a min heap?
    private boolean isMinHeapOrdered(int k) {
        if (k > n) return true;
        int left = 2*k;
        int right = 2*k + 1;
        if (left  <= n && greater(k, left))  return false;
        if (right <= n && greater(k, right)) return false;
        return isMinHeapOrdered(left) && isMinHeapOrdered(right);
    }
    
}