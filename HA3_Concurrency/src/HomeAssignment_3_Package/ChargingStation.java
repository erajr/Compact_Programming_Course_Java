package HomeAssignment_3_Package;

public class ChargingStation {
	public final int stationId;
    public boolean isOccupied;

    public ChargingStation(int stationId) {
        this.stationId = stationId;
        this.isOccupied = false;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void occupy() {
        isOccupied = true;
    }

    public void release() {
        isOccupied = false;
    }
}
