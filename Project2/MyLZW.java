/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;                   // number of input chars
    private static int maxW = 16;                       //max codeword width
    private static int W = 9;                           //W to be incremented
    private static int L = (int) Math.pow(2, 9);              // min number of codewords = 2^9 = 512
    private static int maxL = (int) Math.pow(2, 16);          // max number of codewords = 2^16
    private static String mode;                         // n, r, or m
    private static int compSize = 0;
    private static int uncompSize = 0;
    private static double newRatio = 0.0;
    private static double oldRatio = 0.0;
    private static double ratioRatios = 0.0;

    public static void compress() { 

        BinaryStdOut.write(mode, 8);                //write the type of mode at beginning of compressed file
        String input = BinaryStdIn.readString();    // read the file
                   
        TST<Integer> st = new TST<Integer>();       // create empty ternary trie
        for (int i = 0; i < R; i++)                 // initialize symbol table with 256 ascii characters
            st.put("" + (char) i, i);
        int code = R+1;                             // R is codeword for EOF

        while (input.length() > 0) {

            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            uncompSize+= t * 8;                    // keep track of the uncompressed data that has been processed
            compSize+= W;                          // keep track of compressed data that has been generated

            if (t < input.length() && code < L)    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);   // increment code

            // increase codeword size when all of the codewords of a previous size have been used
            if ((code == L) && (W < maxW)) {
                //st.put(input.substring(0, t + 1), code++);
                W++;
                L = (int) Math.pow(2, W);
            }

            //if reset mode is chosen, and code is equal to 2^16
            if ((mode.equals("r")) && (code == maxL)) {
                st = new TST<Integer>();                    //reset the symbol table/trie
                for (int i = 0; i < R; i++)                 // reset the 256 ascii characters
                    st.put("" + (char) i, i);
                code = R+1;                                // R is codeword for EOF    
                W = 9;                                     // reset W to 9
                L = (int) Math.pow(2, 9);                   // reset L to 2^9
            }

            // when the monitor mode is chosen and code is equal to 2^16
            if ((mode.equals("m")) && (code == maxL)) {

                newRatio = uncompSize / compSize;               // define new ratio

                if (oldRatio == 0.0) {                          // set initial ratio if first time through
                    oldRatio = newRatio;
                }

                ratioRatios = oldRatio / newRatio;             // take ratio of compression ratios

                //reset dictionary if greater than 1.1
                if (ratioRatios > 1.1) {
                    st = new TST<Integer>();
                    for (int i = 0; i < R; i++)
                        st.put("" + (char) i, i);
                    code = R+1;                             // R is codeword for EOF    
                    W = 9;
                    L = (int) Math.pow(2, 9);

                    newRatio = 0.0;
                    oldRatio = 0.0;
                }
                
            }
            input = input.substring(t);            // Scan past s in input.
        }

        BinaryStdOut.write(R, W);                   //write 256 in W bits which is the codeword for EOF
        BinaryStdOut.close();
    } 


    public static void expand() {

        char modeIndicator = BinaryStdIn.readChar(8);      //read first 8 bits of compressed file which is the mode
        String[] st = new String[maxL];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                            // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);   // read first 9 bits and put it into integer value
        
        if (codeword == R) return;               // expanded message is empty string
        String val = st[codeword];               // grab value (char) of key

        while (true) {

            BinaryStdOut.write(val);                      //write the value to output file
            uncompSize+= val.length() * 8;                // keep track of uncompressed data that has been generated

            codeword = BinaryStdIn.readInt(W);            //read the next 9 bits
            compSize+= W;                                //keep track of compressed data that has been processes

            if (codeword == R) break;                     //if next 9 bits is equal to 256, break the while loop

            String s = st[codeword];                      // grab the value (char) of the key

            if (i == codeword) s = val + val.charAt(0);   // special case hack

           
            if (i < (L-1)) st[i++] = val + s.charAt(0);       // append the first letter of s to val

            // increase codeword width
            if ((i == (L-1)) && (W < maxW)) {
                //st[i++] = val + s.charAt(0);
                W++;
                L = (int) Math.pow(2, W);
            }

            // reset the dictionary
            if ((modeIndicator == 'r') && (i == (maxL - 1))) {
                st = new String[maxL];
                for (i = 0; i < R; i++)
                    st[i] = "" + (char) i;
                st[i++] = "";                            // (unused) lookahead for EOF
                W = 9;
                L = (int) Math.pow(2, 9);
                codeword = BinaryStdIn.readInt(W);       // read first 9 bits and put it into integer value
                if (codeword == R) return;               // expanded message is empty string
                val = st[codeword];                      // grab value (char) of key

            }

            //monitor the dictionary
            if ((modeIndicator == 'm') && (i == (maxL - 1))) {

                newRatio = uncompSize / compSize;               // define new ratio

                if (oldRatio == 0.0) {                          // set initial ratio if first time through
                    oldRatio = newRatio;
                }

                ratioRatios = oldRatio / newRatio;             // take ratio of compression ratios

                //reset the dictionary if greater than 1.1
                if (ratioRatios > 1.1) { 
                
                    st = new String[maxL];
                    for (i = 0; i < R; i++)
                        st[i] = "" + (char) i;
                    st[i++] = "";                            // (unused) lookahead for EOF
                    W = 9;
                    L = (int) Math.pow(2, 9);
                    codeword = BinaryStdIn.readInt(W);       // read first 9 bits and put it into integer value
                    if (codeword == R) return;               // expanded message is empty string
                    val = st[codeword];                     // grab value (char) of key
                    
                    
                    newRatio = 0.0;
                    oldRatio = 0.0;
                    
                }
                
            }
            val = s;                                      //val is now equal to s
        }

        BinaryStdOut.close();
    }



    public static void main(String[] args) {

        if      (args[0].equals("-")){                                      //make sure mode is n, m, or r; if not, throw exception
            mode = args[1];                                                 //set mode equal to args[1]
            if (mode.equals("n") || mode.equals("r") || mode.equals("m") )  //check that mode is valid 
                compress();                                                 //call compress method

        } 
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
