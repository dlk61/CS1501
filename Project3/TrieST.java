public class TrieST<Value> {
    private static final int R = 256;        

    private Node root;      // root of trie
    private int n = 0;          // number of keys in trie

    // R-way trie node
    private static class Node {
        private int heapIndex;
        private Node[] next = new Node[R];
    }

   /**
     * Initializes an empty string symbol table.
     */
    public TrieST() {
        root = new Node();
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param heapIndex value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, int heapIndex) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        else root = put(root, key, heapIndex, 0);
    }

    private Node put(Node x, String key, int heapIndex, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.heapIndex == -1) n++;
            x.heapIndex= heapIndex;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, heapIndex, d+1);
        return x;
    }

    /**
     * Returns the heapIndex associated with the given VIN number.
     * @param key the VIN number
     * @return the heapIndex associated with the given VIN number if the VIN number is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return -1;
        return (int) x.heapIndex;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != -1;
    }

    /**
     * Removes the VIN number from the set if it is present.
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.heapIndex != -1) n--;
            x.heapIndex = -1;
        }
        else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d+1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.heapIndex != -1) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Is this symbol table empty?
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }
    
}