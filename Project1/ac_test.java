import java.io.*;
import java.util.*;

public class ac_test
{

    //dictionary and history are stored in a DLBTrie
    private static DLBTrie dictionary, history;
    
    public static void main( String[] args ) throws Exception
    {
        Scanner keyboard = new Scanner(System.in);            //Scanner is used for getting input
        String input;                                         // the input string inputted by user
        StringBuilder prefix = new StringBuilder();           //prefix is started out blank and we will append to it using stringbuilder
        String historyWord;                                   //the word created that will be added to the history trie
        final int ARRAY_SIZE = 5;                             //array size is always 5
        String[] predictionsArray = new String[ARRAY_SIZE];   // this creates the array of size 5
        int num_predictions = ARRAY_SIZE;                     // the number of predictions starts out as 5
        int count = 0;                                        // set count to 0; this helps calculate averageTime
        double totalTime = 0;                                 // set totalTime to 0
        double averageTime = 0;                               // set averageTime to 0

        dictionary = new DLBTrie();                          // this creates an empty DLB trie

        try
		{
            //this opens the file for reading
            Scanner infile = new Scanner( new FileReader( "dictionary.txt" ) );

            //adds each line of the dictionary to the dictionary trie
            while ( infile.hasNext() ){
                dictionary.addtoTrie(infile.next()); 
            }
            
            //close the file
            infile.close();
        }
        catch ( Exception e )
        {
            System.out.println( "FATAL ERROR CAUGHT IN C'TOR: " + e );
			System.exit(0);
        }


        File file = new File("user_history.txt");
        file.createNewFile();
        Scanner infile = new Scanner(file);
        FileWriter userHistory = new FileWriter(file, true);
        
        //creates the empty history trie
        history = new DLBTrie();
        
        //every line in user_history.txt is added to the history trie
        while (infile.hasNextLine()) {
            historyWord = infile.nextLine();
            history.addtoTrie(historyWord);
        }

        //Base case
        System.out.print("Enter your first character: ");
        input = keyboard.nextLine();

        // go through this while loop until user input equals ! which exits the program
        while (!input.equals("!")) {

            switch (input) {
                case "1":
                    
                case "2":
                    
                case "3":
                    
                case "4":
                
                case "5":
                  
                    // if input equals 1, 2 3, 4, or 5, a word is chosen and that word is displayed
                    System.out.println("\nWord Completed: " + predictionsArray[Integer.parseInt(input)-1]);

                    // add the chosen word to the history trie
                    history.addtoTrie(predictionsArray[Integer.parseInt(input)-1]);

                    // write the chosen word to the user_history.txt
                    userHistory.write(predictionsArray[Integer.parseInt(input)-1] + "\n");

                    // Ask for next character
                    System.out.print("\nEnter first character of the next word: ");

                    // clear the prefix
                    prefix.setLength(0);

                    // clear the array 
                    for (int i = 0; i < ARRAY_SIZE; i++){
                        predictionsArray[i] = null;
                    }

                    break;
                
                case "$":

                    // this symbol means that the word the user was creating is stopped whether it is a word or not
                    System.out.println("\nWord Completed: " + prefix);

                    // add the word to history
                    history.addtoTrie(prefix.toString());

                    //write the word to user_history.txt
                    userHistory.write(prefix + "\n");

                    //ask for next word's first character
                    System.out.print("\nEnter first character of the next word: ");

                    //clear the word
                    prefix.setLength(0);

                    // clear the array
                    for (int i = 0; i < ARRAY_SIZE; i++){
                        predictionsArray[i] = null;
                    }


                    break;

                default:

                    // add the input onto the prefix
                    prefix.append(input);

                    //the number of predictions starts off with the array size which is 5
                    num_predictions = ARRAY_SIZE;

                    //get the history trie's root node
                    DLBNode historyRoot = history.getRoot();

                    //get the dictionary trie's root node
                    DLBNode dictionaryRoot = dictionary.getRoot();
                    
                    // start the timer
                    long startTime = System.nanoTime();

                    //find the number of predictions from the history trie
                    num_predictions = history.traverse_trie(historyRoot, prefix.toString(), num_predictions, predictionsArray);
        
                    // find the number of predictions from the dictionary 
                    num_predictions = dictionary.traverse_trie(dictionaryRoot, prefix.toString(), num_predictions, predictionsArray);
                    
                    //stop the timer
                    long estimatedTime = ((System.nanoTime() - startTime));

                    //conver the runtime to seconds
                    double runtime = estimatedTime/1000000000.0; 

                    // add each runtime of finding predictions to the total time
                    totalTime += runtime;

                    //increment the count in order to calculate average runtime later
                    count++;


                    System.out.print("\n(");

                    //Display runtime to 6 decimal places
                    System.out.format("%.6f", runtime);
                    System.out.println(" s)");
                    
                    
                    System.out.println("Predictions:");

                    // If the number of predictions is less than 5, the rest are set to null
                    for (int i = 0; i < num_predictions; i++){
                        predictionsArray[5 - num_predictions + i] = null;
                    }

                    
                    // print out the predictions
                    for (int index = 0; index < (5 - num_predictions); index++) {

                        System.out.print("(" + (index + 1) + ") " + predictionsArray[index] + " ");
                    }

                    // if no predictions are found, tell the user
                    if (num_predictions == 5) {
                        System.out.println("No predictions were found."); 
                    }
                    
                    System.out.println("\n");
                    System.out.print("Enter the next character: ");
                    break;
            }

           

            //get the next input
            input = keyboard.nextLine();
            
            
        }
        //close the history file
        infile.close();
        userHistory.close();

        // Find the average time by dividing the total time to find predictions by the number of times predictions were found
        averageTime = totalTime / count;
        System.out.print("\nAverage time: ");
        
        //Display average time to 6 decimals
        System.out.format("%.6f", averageTime);

        System.out.println("\nBye!");
    }

   
}