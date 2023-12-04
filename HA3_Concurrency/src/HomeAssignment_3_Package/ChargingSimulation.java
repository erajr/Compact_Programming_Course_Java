package HomeAssignment_3_Package;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ChargingSimulation {
	private static final int MAX_CHARGING_STATIONS = 5; //max charging stations are 5
    private static final int MAX_WAITING_TIME = 5; //15mins is too long for simulation, so set 5 secs

    private BlockingQueue<Vehicle> waitingQueue = new LinkedBlockingQueue<>();
    private ChargingStation[] chargingStations = new ChargingStation[MAX_CHARGING_STATIONS];
    private ExecutorService chargingThreadPool = Executors.newFixedThreadPool(MAX_CHARGING_STATIONS);

    public ChargingSimulation() {
        for (int i = 0; i < MAX_CHARGING_STATIONS; i++) {
            chargingStations[i] = new ChargingStation(i + 1);
        }
    }

    // Simulates vehicles arriving at random times and initiates the charging process
    public void simulateVehicleArrival() {
        Random random = new Random();
        while (true) {
            try {
            	long currentTime = System.currentTimeMillis(); //arrival time is currentTime
            	TimeUnit.MILLISECONDS.sleep(random.nextInt(2000) + 1); // Simulate random arrival times (max 20 seconds)
                Vehicle vehicle = new Vehicle(currentTime ,random.nextInt(500) );
                System.out.println("Vehicle "+vehicle.getID()+" is arrived.");
                if (assignChargingStation(vehicle)) {
                    chargingThreadPool.execute(() -> chargeVehicle(vehicle));
                } else {
                	System.out.println("Vehicle "+vehicle.getID()+" is getting into queue becuase all stations are occupied.");
                    waitingQueue.offer(vehicle);
                    removeOverdueVehicles(currentTime);          
                    
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Assigns an available charging station to a vehicle
    private boolean assignChargingStation(Vehicle vehicle) {
        for (ChargingStation station : chargingStations) {
            if (!station.isOccupied()) {
                station.occupy();
                System.out.println("Vehicle "+vehicle.getID()+" is assigned to station with ID:"+station.stationId+".");
                return true;
            }
        }
        return false;
    }

    // Simulates the charging process for a vehicle
    private void chargeVehicle(Vehicle vehicle) {
        // Simulate charging process
        try {
        	System.out.println("Vehicle " +vehicle.getID()+ " charging started.");
        	TimeUnit.SECONDS.sleep(20); // Simulate charging time - set to 20 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        releaseChargingStation();
    }

    // Releases a charging station and assigns it to the next vehicle in the waiting queue
    private void releaseChargingStation() {
        for (ChargingStation station : chargingStations) {
            if (station.isOccupied()) {
                station.release();
                System.out.println("Station ID:"+station.stationId+" is free.");
                if (!waitingQueue.isEmpty()) {
                    Vehicle nextVehicle = waitingQueue.poll();
                    System.out.println("Vehicle id : " +nextVehicle.getID() + " is assigned to to the station " + station.stationId + " for the charging");
                    station.occupy();
                    chargingThreadPool.execute(() -> chargeVehicle(nextVehicle));
                }
            }
        }
    }

    // Removes vehicles from the waiting queue if they have waited for more than MAX_WAITING_TIME
    private void removeOverdueVehicles(long currentTime) {
        Iterator<Vehicle> iterator = waitingQueue.iterator();
        while (iterator.hasNext()) {
            Vehicle vehicle = iterator.next();
            long waitingTime = TimeUnit.MILLISECONDS.toSeconds(currentTime - vehicle.getArrivalTime());
            //System.out.println("Waiting time for vehicle id: " +vehicle.getID() + " is : " + waitingTime + " seconds");
            if (waitingTime > MAX_WAITING_TIME) {
                System.out.println("Vehicle " + vehicle.getID() +" waited for more than 5 seconds and has left the queue.");
                iterator.remove(); // Remove the vehicle from the waiting queue
            }
        }
    }
}
