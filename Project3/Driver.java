import java.util.Scanner; // Needed for the Scanner class 
import java.io.*;         //needed to read file

public class Driver{

     private static Scanner keyboard = new Scanner(System.in);                      // Create a Scanner object to read input.
     private static int option_number;                                              // holds the user selected value
     private static int initSize = 14;                                              //inital size of heap
     private static MinPQ priceHeap = new MinPQ(initSize, true);                    // creates a price heap with initial capacity of 14 and priceflag set to true
     private static MinPQ mileageHeap = new MinPQ(initSize, false);                 // creates a mileage heap with initial capacity of 14 and priceflag set to false
     private static SeparateChainingHashST st = new SeparateChainingHashST();       // creates a separate chaining hash table
     private static TrieST<Integer> priceTrie = new TrieST<Integer>();              // creates a price R-way trie to store Vin and index in the price heap
     private static TrieST<Integer> mileageTrie = new TrieST<Integer>();            // creates a mileage R-way trie to store Vin and index in the mileage heap
     

    public static void main (String [] args) throws FileNotFoundException {

        Car car;                                       // car object
        String[] attributes;                           // string array of the car's attributes
        String strLine, vin, make, model, color;              
        double price;
        int mileage;
        
        Scanner inputFile = new Scanner( new FileReader( "cars2.txt" ) );        //this opens the file for reading
        String line = inputFile.nextLine();                                     //read first line

        //goes through each line of input file
        while ( inputFile.hasNext() ){
            strLine = inputFile.nextLine();
            attributes = strLine.split(":");                //split each line up by the colon and put them into array
            vin = attributes[0];
            make = attributes[1];
            model = attributes[2];
            price = Double.parseDouble(attributes[3]);      //convert to double
            mileage = Integer.parseInt(attributes[4]);    //convert to double
            color = attributes[5];

            //creates car object
            car = new Car(vin, make, model, price, mileage, color);

            priceHeap.insert(car, priceTrie);             //places car into prices heap, and places its vin number and index into prices Trie
            mileageHeap.insert(car, mileageTrie);         //places car into mileage heap, and places its vin number and index into mileage Trie
            st.put(car, make+model);                                  //places car into separate chaining hash table
        }
                    
        //close the file
        inputFile.close();

        // Create a do while loop to control the program
        do
        {
            // Ask the user to enter number 1-8
            System.out.println("Please choose an option:");
            System.out.println("\t1) Add a car.\n" +
                                "\t2) Update a car.\n" +
                                "\t3) Remove a specific car from consideration.\n" + 
                                "\t4) Retrieve the lowest price car.\n" + 
                                "\t5) Retrieve the lowest mileage car.\n" +
                                "\t6) Retrieve the lowest price by make and model.\n" + 
                                "\t7) Retrieve the lowest mileage car by make and model.\n" + 
                                "\t8) Exit");
            option_number = keyboard.nextInt(); 

            // Determine the number entered
            switch (option_number)
            {
                case 1:
                    addCar();               //if user enters 1, go to addCar method
                    break;
                case 2:
                    updateCar();            //if user enters 2, go to updateCar method
                    break;
                case 3:
                    removeCar();            //if user enters 3, go to removeCar method
                    break;
                case 4:
                    lowestPrice();          //if user enters 4, go to lowestPrice method
                    break;
                case 5:
                    lowestMileage();        //if user enters 5, go to lowestMileage method
                    break;
                case 6:
                case 7:
                    lowestMakeModel();      //if user enters 6 or 7 go to lowestMakeModel method
                    break;
                case 8:                     //if user enters 8, terminate program
                    break;
                default:
                    System.out.println("Invalid Entry!!!");         // To make sure user enters valid input
                    break;
            }
        }
        while (option_number != 8);         //repeat until user enters 8
    }

    private static void addCar() {

        Car newCar;
        String vin, make, model, color;
        double price;
        int mileage;
        boolean is17, noConditions, correct;

        do{
        
            System.out.print("Enter VIN: ");            //asks user to enter vin number
            vin = keyboard.next();

            is17 = vin.length() == 17;                                                         //false is vin length is not 17
            noConditions = !(vin.contains("I") || vin.contains("O") || vin.contains("Q"));     //false if vin contains an I, O, or Q
            correct = vin.matches("^[A-Z0-9]*$");                                           //true if contains a lowercase letter

            //if vin number is not length 17, contains a lowercase letter, or contains an I, O, or Q...
            if (!is17 || !correct || !noConditions ) {                                       
            System.out.println("VIN number must be 17 characters of only numbers and captial letters but" + 
                                "cannot contain the letters I, O, or Q.");
            }

        }while (!is17 || !correct || !noConditions );

        System.out.print("Enter make: ");               //asks user to enter make of car
        make = keyboard.next();

        System.out.print("Enter model: ");              //asks user to enter model of car
        model = keyboard.next();

        System.out.print("Enter price: $");              //asks user to enter price of car
        price = keyboard.nextDouble();

        System.out.print("Enter mileage: ");            //asks user to enter mileage of car
        mileage = keyboard.nextInt();

        System.out.print("Enter color: ");              //asks user to enter color of car
        color = keyboard.next();

        newCar = new Car(vin, make, model, price, mileage, color);      //creates car from attributes

        priceHeap.insert(newCar, priceTrie);            //places the new car into prices heap, and places its vin number and index into prices Trie
        mileageHeap.insert(newCar, mileageTrie);        //places the new car into mileage heap, and places its vin number and index into mileage Trie
        st.put(newCar, make+model);                                 //places the new car into separate chaining hash table
        
        System.out.println("The car was added.");
    }

    private static void updateCar() {
        Car car;
        String vin, updatedColor;
        double updatedPrice;
        int updatedMileage, updateNumber;
        
        System.out.print("Enter the VIN number of the car you would like to update: ");            //asks user to enter vin number
        vin = keyboard.next();

        int priceHeapIndex = priceTrie.get(vin);                    //get the car's index in the price heap by looking up the vin number in the price trie
        int mileageHeapIndex = mileageTrie.get(vin);                //get the car's index in the mileage heap by looking up the vin number in the mileage trie

        //check to make sure the car exists in the database
        if (priceHeapIndex < 1 || mileageHeapIndex < 1) {
            System.out.println("That car does not exist in the database.");
            return;
        }

        do {

            System.out.println("Which would you like to update?");
            System.out.print("\t1) The price of the car.\n" +
                                "\t2) The mileage of the car.\n" +
                                "\t3) The color of the car.\n"); 
            updateNumber = keyboard.nextInt();

            switch (updateNumber) {
                case 1: 
                    System.out.print("Enter the car's updated price: $");
                    updatedPrice = keyboard.nextDouble();
                    car = priceHeap.getCar(priceHeapIndex);                                         //grabs the car by using its heap index found from the price trie
                    car.setPrice(updatedPrice);                                                     //sets the car's price to the user inputted updated price
                    priceHeap.setCar(car, priceHeapIndex, priceTrie);                               //stores the updated car in price heap and update trie
                    st.updatePrice(car, updatedPrice);                                              //updates car in hash table
                    break;
                case 2: 
                    System.out.print("Enter the car's updated mileage: ");
                    updatedMileage = keyboard.nextInt();
                    car = mileageHeap.getCar(mileageHeapIndex);                                     //grabs the car by using its heap index found from the mileage trie
                    car.setMileage(updatedMileage);                                                 //sets the car's mileage to the user inputted updated mileage
                    mileageHeap.setCar(car, mileageHeapIndex, mileageTrie);                         //stores the updated car in mileage heap and update trie
                    st.updateMileage(car, updatedMileage);                                          //updates the car in the hash table
                    break;
                case 3: 
                    System.out.print("Enter the car's updated color: ");
                    updatedColor = keyboard.next();
                    car = mileageHeap.getCar(mileageHeapIndex);                                     //grabs car by using its heap index found from the mileage trie
                    car.setColor(updatedColor);                                                     //sets the car's color to the user inputted updated color
                    st.updateColor(car, updatedColor);                                              //updates the car in the hash table
                    break;
                default:
                    System.out.println("Invalid Entry!!!");
                    break;
            }

        }while (updateNumber < 1 || updateNumber > 3);

        System.out.println("The car has been updated.");
    }

    private static void removeCar() {
        Car car;
        String vin;

        System.out.print("Enter the vin number of the car you would like to remove: ");
        vin = keyboard.next();

        int priceHeapIndex = priceTrie.get(vin);             //gets index of car in price heap
        int mileageHeapIndex = mileageTrie.get(vin);         //gets index of car in mileage heap

        //check to make sure vin is in database
        if (priceHeapIndex < 1 || mileageHeapIndex < 1) {
            System.out.println("That car does not exist in the database.");
            return;
        }

        car = priceHeap.getCar(priceHeapIndex);               //grabs the car by using its heap index found from the price trie
        priceTrie.delete(vin);                                //delete the car's VIN number and index from price trie
        priceHeap.remove(priceHeapIndex, priceTrie);          //removes the car from the price heap and updates its trie
        mileageTrie.delete(vin);                              //deletes the vin number from the mileage trie
        mileageHeap.remove(mileageHeapIndex, mileageTrie);    //removes the car from the mileage heap and updates its trie
        st.remove(car, car.getMake() + car.getModel());       //updates the hash table

        System.out.println("The car has been removed from the database");
        
    }

    private static void lowestPrice() {

        Car minPriceCar = priceHeap.min();              //grabs the min from the heap
        if (minPriceCar == null) {
            System.out.println("There are currently no cars stored in the database.");
        }
        else{
            System.out.print("The lowest price car is: ");
            System.out.println("\n\tVIN number: " + minPriceCar.getVin() + 
                           "\n\tMake: " + minPriceCar.getMake() + 
                           "\n\tModel: " +  minPriceCar.getModel() + 
                            "\n\tPrice: $" + minPriceCar.getPrice() + 
                            "\n\tMileage: " +  minPriceCar.getMileage() + 
                            "\n\tColor: " + minPriceCar.getColor());
        }
    }

    private static void lowestMileage() {

        Car minMileageCar = mileageHeap.min();              //grabs the min from the heap
        if (minMileageCar == null) {
            System.out.println("There are currently no cars stored in the database.");
        }
        else{
            System.out.print("The lowest mileage car is: ");
            System.out.println("\n\tVIN number: " + minMileageCar.getVin() + 
                            "\n\tMake: " + minMileageCar.getMake() + 
                            "\n\tModel: " +  minMileageCar.getModel() + 
                             "\n\tPrice: $" + minMileageCar.getPrice() + 
                            "\n\tMileage: " +  minMileageCar.getMileage() + 
                             "\n\tColor: " + minMileageCar.getColor());
        }
    }

    private static void lowestMakeModel() {

        System.out.print("Enter the make of the car: ");
        String make = keyboard.next();

        System.out.print("Enter the model of the car: ");
        String model = keyboard.next();

        LinkedList list = st.getList(make + model);   //get the linked list of specific make and model

        Node<Car> node = list.getHead();

        MinPQ specificHeap;
        if (option_number == 6) 
            specificHeap = new MinPQ(initSize, true);
        else specificHeap = new MinPQ(initSize, false);

        while (node != null) {
             //checks to make sure the cars in the list have the same make and model, just in case one hashes to the same index
            if ((option_number == 6) && ((node.getData().getMake() + node.getData().getModel()).equals(make+model))) {     
                specificHeap.insert(node.getData());
            }
            else if ((option_number == 7) && ((node.getData().getMake() + node.getData().getModel()).equals(make+model))){
                specificHeap.insert(node.getData());
            }
            node = node.getNext();
        }

        Car car = specificHeap.min();
        if (car == null) {
            System.out.println("There are currently none of those type of cars stored in the database.");
        }
        else {
            if (option_number == 6) {
                System.out.print("The lowest price car by " + car.getMake() + " " + car.getModel() + " is: ");
                System.out.println("\n\tVIN number: " + car.getVin() + 
                               "\n\tMake: " + car.getMake() + 
                               "\n\tModel: " +  car.getModel() + 
                                "\n\tPrice: $" + car.getPrice() + 
                                "\n\tMileage: " +  car.getMileage() + 
                                "\n\tColor: " + car.getColor());
            }
            else  {
                System.out.print("The lowest mileage car by " + car.getMake() + " " + car.getModel() + " is: ");
                System.out.println("\n\tVIN number: " + car.getVin() + 
                               "\n\tMake: " + car.getMake() + 
                               "\n\tModel: " +  car.getModel() + 
                                "\n\tPrice: $" + car.getPrice() + 
                                "\n\tMileage: " +  car.getMileage() + 
                                "\n\tColor: " + car.getColor());
            }
        }

    }

}