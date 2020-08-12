public class SeparateChainingHashST {
    private static final int INIT_CAPACITY = 101;

    private int n;                                // number of cars
    private int m;                                // hash table size
    private LinkedList<Car>[] st;                 // array of linked-list symbol tables

    /**
     * Initializes an empty symbol table.
     */
    public SeparateChainingHashST() {
        this(INIT_CAPACITY);
    } 

    /**
     * Initializes an empty symbol table with {@code m} chains.
     * @param m the initial number of chains
     */
    public SeparateChainingHashST(int m) {
        this.m = m;
        st = (LinkedList<Car>[]) new LinkedList[m];
        for (int i = 0; i < m; i++)
            st[i] = new LinkedList<Car>();
    } 

    //inserts the car into the hash table
    public void put(Car c, String type) {
        int i = hash(type);                 //hashes the make and model
        st[i].put(c);                       //puts the car at the hashed index in the array
        n++;
    } 

    // hash value between 0 and m-1
    public int hash(String makeModel) {
        return (makeModel.hashCode() & 0x7fffffff) % m;
    } 

    //removes car from hash table
    public void remove(Car c, String type) {
        int i = hash(type);
        LinkedList<Car> list = st[i];
        String vin = c.getVin();
        list.remove(vin);
        st[i] = list;
        n--;
    }

    //grabs the specific linked list
    public LinkedList<Car> getList(String s) {
        return st[hash(s)];     
    }

//updates the lists when a car is removed or updated

    public Car updatePrice(Car car, double price) {
        String make = car.getMake();
        String model = car.getModel();
        int hashIndex = hash(make+model);                 //hashes the make and model of car
        LinkedList list = st[hashIndex];                  //gets the linked list based on the hash index
        String vin = car.getVin();                        //gets the VIN number of the car
        return list.updatePrice(vin, price);              //returns the updated car
    }
    
    
    public Car updateMileage(Car car, int mileage) {
        String make = car.getMake();
        String model = car.getModel();      
        int hashIndex = hash(make+model);
        LinkedList list = st[hashIndex];
        String vin = car.getVin();
        return list.updateMileage(vin, mileage);
    }

    public Car updateColor(Car car, String color) {
        String make = car.getMake();
        String model = car.getModel();
        int hashIndex = hash(make+model);
        String vin = car.getVin();
        LinkedList list = st[hashIndex];
        return list.updateColor(vin, color);
    }

    
}