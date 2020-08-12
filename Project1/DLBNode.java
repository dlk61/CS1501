class DLBNode {

    private Character symbol;                  // this is the symbol that the node holds
    private DLBNode child, rightSib;           // this is the node's child or sibling node
    private String word;                       // this is the node's completed word if it has one

    // this creates a new DLB node with all 4 variables null
    public DLBNode() {
        this(null, null, null, null);
    }

    // if a symbol is passed to the node, the symbol will be set and the other variables will be set to null
    public DLBNode(Character symbol) {
        this( symbol, null, null, null );
    }

    // when all 4 variables are passed, each one will be set
    public DLBNode(Character symbol, DLBNode child, DLBNode rightSib, String word){
        setSymbol(symbol);
        setChild(child);
        setRightSib(rightSib);
        setWord(word);
    }

    // set the symbol
    public void setSymbol(Character symbol) {
        this.symbol = symbol;
    }

    // set the child
    public void setChild(DLBNode child) {
        this.child = child;
    }

    // set the right sibling node
    public void setRightSib(DLBNode rightSib) {
        this.rightSib = rightSib;
    }

    // set the node's word
    public void setWord(String word) {
		
		this.word = word;
    }

    // return symbol
    public Character getSymbol()
	{
		return symbol;
	}
   
    // return child
    public DLBNode getChild()
	{
		return child;
    }
    
    // return the Right sibling node
	public DLBNode getRightSib()
	{
		return rightSib;
    }	

    // return the node's word
    public String getWord() {
		
		return word;
    }

}