package com.fh.hometask;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fh.hometask.booking.Station;
import com.fh.hometask.car.Car;

public class ConcurrencySimulation {

    private static final Logger logger = LogManager.getLogger("ChargingSystem");

    public static final int MAX_CHARGING_STATIONS = 7; // 'N' number of charging Stations
    private static final int MAX_WAITING_TIME = 10; // (Seconds) Fixed time for waiting
    private static final int WEATHER_CHANGE_SECONDS = 30;

    private static final Logger[] chargingStationLogger = new Logger[MAX_CHARGING_STATIONS];

    public BlockingQueue<Car> waitingQueue = new LinkedBlockingQueue<>();
    public Station[] chargingStations = new Station[MAX_CHARGING_STATIONS];
    private ExecutorService chargingThreadPool = Executors.newFixedThreadPool(MAX_CHARGING_STATIONS);

    public ConcurrencySimulation() {
        LoggingSystem system = new LoggingSystem();
        system.initiateLoggers(MAX_CHARGING_STATIONS);
        for (int i = 0; i < MAX_CHARGING_STATIONS; i++) {
            chargingStations[i] = new Station(i + 1);
            chargingStationLogger[i] = LogManager.getLogger("chargingStation" + (i + 1));
        }

        Random random = new Random();
        int randomChangeWeather = random.nextInt(WEATHER_CHANGE_SECONDS - 5 + 1) + 5;
        ScheduledExecutorService weatherChange = Executors.newScheduledThreadPool(1);
        weatherChange.scheduleAtFixedRate(this::simulateWeatherConditions, 0, randomChangeWeather, TimeUnit.SECONDS);
    }

    public void assignStationLoggers() {

    }

    // Simulates vehicles arriving at random times and initiates the charging process
    public void simulateVehicleArrival() {
        Random random = new Random();
        while (true) {
            try {
            	long currentTime = System.currentTimeMillis(); // arrival time is currentTime
            	TimeUnit.MILLISECONDS.sleep(random.nextInt(2000) + 1); // Simulate random arrival times (max 20 seconds)
                Car car = new Car(currentTime ,random.nextInt(500) );
                logger.info("Vehicle "+car.getId()+" is arrived.");
                if (assignChargingStation(car)) {
                    chargingThreadPool.execute(() -> chargeVehicle(car));
                } else {
                	logger.info("Vehicle "+car.getId()+" is getting into queue becuase all stations are occupied.");
                    waitingQueue.offer(car);
                    removeOverdueVehicles(currentTime);          
                    
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Assigns an available charging station to a vehicle
    public boolean assignChargingStation(Car car) {
        for (Station station : chargingStations) {
            if (!station.isOccupied()) {
                station.occupy();
                chargingStationLogger[station.stationId - 1].info("Vehicle "+car.getId()+" is assigned to station with ID:"+station.stationId+".");
                logger.info("Vehicle "+car.getId()+" is assigned to station with ID:"+station.stationId+".");
                return true;
            }
        }
        return false;
    }

    // Simulates the charging process for a vehicle
    public void chargeVehicle(Car car) {
        // Simulate charging process
        try {
        	logger.info("Vehicle " +car.getId()+ " charging started.");
        	TimeUnit.SECONDS.sleep(20); // Simulate charging time - set to 20 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        releaseChargingStation();
    }

    // Releases a charging station and assigns it to the next vehicle in the waiting queue
    public void releaseChargingStation() {
        for (Station station : chargingStations) {
            if (station.isOccupied()) {
                station.release();
                chargingStationLogger[station.stationId - 1].info("Station ID: " + station.stationId + " is free.");
                logger.info("Station ID: " + station.stationId + " is free.");
                if (!waitingQueue.isEmpty()) {
                    Car nextCar = waitingQueue.poll();
                    chargingStationLogger[station.stationId - 1].info("Vehicle id : " + nextCar.getId() + " is assigned to to the station " + station.stationId + " for the charging");
                    logger.info("Vehicle id : " + nextCar.getId() + " is assigned to to the station " + station.stationId + " for the charging");
                    station.occupy();
                    chargingThreadPool.execute(() -> chargeVehicle(nextCar));
                }
            }
        }
    }

    // Removes vehicles from the waiting queue if they have waited for more than MAX_WAITING_TIME
    public void removeOverdueVehicles(long currentTime) {
        Iterator<Car> iterator = waitingQueue.iterator();
        while (iterator.hasNext()) {
            Car car = iterator.next();
            long waitingTime = TimeUnit.MILLISECONDS.toSeconds(currentTime - car.getArrivalTime());
            // System.out.println("Waiting time for vehicle id: " +vehicle.getID() + " is : " + waitingTime + " seconds");
            if (waitingTime > MAX_WAITING_TIME) {
                logger.warn("Vehicle " + car.getId() +" waited for more than 5 seconds and has left the queue.");
                iterator.remove(); // Remove the vehicle from the waiting queue
            }
        }
    }

    private void simulateWeatherConditions() {
        for (Station station : chargingStations) {
            station.setEnergySource(getRandomEnergySource());
            if(station.getEnergySource().equals(Station.EnergySource.SOLAR)) {
                chargingStationLogger[station.stationId - 1].info("Due to Sunny weather, Energy Source set at Station ID: " + station.getStationId() + " is " + station.getEnergySource());
            } else if(station.getEnergySource().equals(Station.EnergySource.HYDROELECTRIC)) {
                chargingStationLogger[station.stationId - 1].info("Due to Rainy weather, Energy Source set at Station ID: " + station.getStationId() + " is " + station.getEnergySource());
            } else {
                chargingStationLogger[station.stationId - 1].info("Due to Windy weather, Energy Source set at Station ID: " + station.getStationId() + " is " + station.getEnergySource());
            }
        }
    }

    private Station.EnergySource getRandomEnergySource() {
        Station.EnergySource[] energySources = Station.EnergySource.values();
        int randomIndex = (int) (Math.random() * energySources.length);
        return energySources[randomIndex];
    }

}
