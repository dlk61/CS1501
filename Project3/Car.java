class Car{

    private String vin, make, model, color;
    private double price;       
    private int mileage;     

    //when all 6 variables are passed, each one will be set
    public Car(String vin, String make, String model, double price, int mileage, String color) {
        setVin(vin);
        setMake(make);
        setModel(model);
        setPrice(price);
        setMileage(mileage);
        setColor(color);
    }

    // set the VIN
    public void setVin(String vin) {
        this.vin = vin;
    }

    // set the make
    public void setMake(String make) {
        this.make = make;
    }

    // set the model
    public void setModel(String model) {
        this.model = model;
    }

    // set the price
    public void setPrice(double price) {
        this.price = price;
    }

    // set the mileage
    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    // set the color
    public void setColor(String color) {
        this.color = color;
    }

    // return VIN number
    public String getVin()
	{
		return vin;
	}
   
    // return make
    public String getMake()
	{
		return make;
    }
    
    // return model
	public String getModel()
	{
		return model;
    }	

    // return price
    public double getPrice() {
		
		return price;
    }

    // return mileage
	public int getMileage()
	{
		return mileage;
    }	

    // return color
    public String getColor() {
		
		return color;
    }

}