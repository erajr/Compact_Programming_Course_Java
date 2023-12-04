package HomeAssignment_3_Package;


public class Vehicle {
	private long arrivalTime;
    private int ID;
    public Vehicle(long arrivalTime , int id) {
        this.arrivalTime = arrivalTime;
        this.ID = id;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }
    public long getID() {
        return ID;
    }
}
