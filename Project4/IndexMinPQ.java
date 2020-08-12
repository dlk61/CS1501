import java.util.NoSuchElementException;

public class IndexMinPQ<Key extends Comparable<Key>>  {
    private final int maxN;        // maximum number of elements on PQ
    private int n;           // number of elements on PQ
    private final int[] pq;        // binary heap using 1-based indexing
    private final int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private final Key[] keys;      // keys[i] = priority of i

  
     // Initializes an empty indexed priority queue with indices between {@code 0}
     //and {@code maxN - 1}.
  
    public IndexMinPQ(final int maxN) {
        if (maxN < 0) throw new IllegalArgumentException();
        this.maxN = maxN;
        n = 0;
        keys = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
        pq   = new int[maxN + 1];
        qp   = new int[maxN + 1];                   // make this of length maxN??
        for (int i = 0; i <= maxN; i++)
            qp[i] = -1;
    }

     // Returns true if this priority queue is empty.
     
    public boolean isEmpty() {
        return n == 0;
    }

    
    // Is {@code i} an index on this priority queue?

    public boolean contains(final int i) {
        return qp[i] != -1;
    }

    
    // Associates key with index {@code i}.
     
    public void insert(final int i, final Key key) {
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        swim(n);
    }

   
    // Removes a minimum key and returns its associated index.
     
    public int delMin() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        final int min = pq[1];
        exch(1, n--);
        sink(1);
        assert min == pq[n+1];
        qp[min] = -1;        // delete
        keys[min] = null;    // to help with garbage collection
        pq[n+1] = -1;        // not needed
        return min;
    }


    
     // Decrease the key associated with index {@code i} to the specified value.

    public void decreaseKey(final int i, final Key key) {
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        if (keys[i].compareTo(key) == 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
        if (keys[i].compareTo(key) < 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");
        keys[i] = key;
        swim(qp[i]);
    }


    
    // Remove the key associated with index {@code i}.
    
    public void delete(final int i) {
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        final int index = qp[i];
        exch(index, n--);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
    }

   /***************************************************************************
    * General helper functions.
    ***************************************************************************/
    private boolean greater(final int i, final int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private void exch(final int i, final int j) {
        final int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }


   /***************************************************************************
    * Heap helper functions.
    ***************************************************************************/
    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }
  

}