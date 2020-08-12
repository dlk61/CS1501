public class LinkedList<T>
{
	private Node<T> head;  // pointer to the front (first) element of the list

	public LinkedList()
	{
		head = null; // compiler does this anyway. just for emphasis
    }
    
    public LinkedList(T obj) {
        head = new Node<T>(obj);
    }
    
    public Node<T> getHead() {
        return head;
    }
    
    //Inserts the car into the symbol table as the head of the linked list
    public void put(T c) {

        head = new Node<T>(c, head);
        
    }
    public Car remove(String vin) {
        Car listCar;
        String listVin;
        Node<Car> headNode = (Node<Car>) head;

        listCar = headNode.getData();
        listVin = listCar.getVin();

        if(vin.equals(listVin)) {
            headNode = headNode.getNext();
            head = (Node<T>) headNode;
            return listCar;
        }
        Node<Car> curNode = headNode;
        while (curNode.getNext() != null) {

            listCar = curNode.getNext().getData();
            listVin = listCar.getVin();

            if (vin.equals(listVin)) {
                curNode.setNext(curNode.getNext().getNext());
                return listCar;
            }
            curNode = curNode.getNext();
        }
        return null;
        
    }
    
    public Car updatePrice(String vin, double price) {
        Node<T> currentNode = head;
        
        while (currentNode.getData() != null) {
            T curCar = currentNode.getData();
            Car listCar = (Car) curCar;
            String curVin = listCar.getVin();

            if (vin.compareTo(curVin) == 0) {
                listCar.setPrice(price);
                return listCar;
            }
            else {
                currentNode = currentNode.getNext();
            }
        }
        return null;

    }
    

    public Car updateMileage(String vin, int mileage) {
        Node<T> currentNode = head;
        
        while (currentNode.getData() != null) {
            T curCar = currentNode.getData();
            Car listCar = (Car) curCar;
            String curVin = listCar.getVin();

            if (vin.compareTo(curVin) == 0) {
                listCar.setMileage(mileage);
                return listCar;
            }
            else {
                currentNode = currentNode.getNext();
            }
        }
        return null;

    }

    public Car updateColor(String vin, String color) {
        Node<T> currentNode = head;
        
        while (currentNode.getData() != null) {
            T curCar = currentNode.getData();
            Car listCar = (Car) curCar;
            String curVin = listCar.getVin();

            if (vin.compareTo(curVin) == 0) {
                listCar.setColor(color);
                return listCar;
            }
            else {
                currentNode = currentNode.getNext();
            }
        }
        return null;

    }

}