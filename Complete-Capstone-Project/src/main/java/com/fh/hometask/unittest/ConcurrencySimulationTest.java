package com.fh.hometask.unittest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fh.hometask.ConcurrencySimulation;
import com.fh.hometask.booking.Station;
import com.fh.hometask.car.Car;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConcurrencySimulationTest {

    private ConcurrencySimulation simulation;
    private Car testCar;
    private Station[] testStations;
    private Car overdueCar;
    private Car timelyCar;
    private final long MAX_WAITING_TIME = 5;

    @BeforeEach
    void setUp() {
        simulation = new ConcurrencySimulation();
        testCar = new Car(System.currentTimeMillis(), 1); // sample car
        testStations = new Station[5]; // Assuming there are 5 charging stations
        for (int i = 0; i < testStations.length; i++) {
            testStations[i] = new Station(i+1); // Initialize charging station
        }
     // Create two cars, one of which exceeds the maximum waiting time
        long currentTime = System.currentTimeMillis();
        overdueCar = new Car(currentTime - TimeUnit.SECONDS.toMillis(MAX_WAITING_TIME + 1), 1); // Timed out cars
        timelyCar = new Car(currentTime, 2); // Non-timed out cars
        simulation.chargingStations = testStations;
        simulation.waitingQueue = new LinkedBlockingQueue<>();
    }

    @Test
    void testAssignChargingStation() {
        boolean result = simulation.assignChargingStation(testCar);

        assertTrue(result, "Should be able to allocate vehicles to charging stations");
        boolean isAnyStationOccupied = false;
        for (Station station : testStations) {
            if (station.isOccupied()) {
                isAnyStationOccupied = true;
                break;
            }
        }
        assertTrue(isAnyStationOccupied, "At least one charging station should be occupied");
    }

    @Test
    void testChargeVehicle() {
        assumeTrue(simulation.assignChargingStation(testCar), "Allocate the vehicle to the charging station first");

        simulation.chargeVehicle(testCar);

        for (Station station : testStations) {
            assertFalse(station.isOccupied(), "While charging is completed, all charging stations should not be occupied");
        }
    }
    
    @Test
    void testConcurrentCarAssignment() {
        // Simulate enough vehicles arriving to fill all charging stations
        for (int i = 0; i < ConcurrencySimulation.MAX_CHARGING_STATIONS; i++) {
            Car car = new Car(System.currentTimeMillis(), i + 10);
            assertTrue(simulation.assignChargingStation(car), "The vehicle should be assigned to the charging station");
        }

        // Check if all charging stations are occupied
        for (Station station : simulation.chargingStations) {
            assertTrue(station.isOccupied(), "All charging stations should be occupied");
        }

        // Simulate the arrival of new vehicles and join the waiting queue
        Car carInQueue = new Car(System.currentTimeMillis(), 100);
        simulation.waitingQueue.offer(carInQueue);

        // Release a charging station and process vehicles in the waiting queue
        simulation.releaseChargingStation();

        // Verify if the vehicles in the waiting queue have been assigned to the recently released charging station
        assertFalse(simulation.waitingQueue.contains(carInQueue), "The vehicles in the waiting queue should be assigned to the charging station");
    }
    
    @Test
    void testReleaseChargingStationAndAssignToNextCar() {
       
        assertTrue(simulation.assignChargingStation(testCar), "Vehicle successfully assigned to charging station"); // Assign the vehicle to the charging station first
        simulation.waitingQueue.offer(testCar); // Join the same vehicle in the waiting queue 
        simulation.releaseChargingStation(); // Simulate the scenario of a charging station being released

        // Check if the vehicle has been reassigned to the charging station
        boolean isCarReassigned = false;
        for (Station station : simulation.chargingStations) {
            if (station.isOccupied()) {
                isCarReassigned = true;
                break;
            }
        }
        assertTrue(isCarReassigned, "The vehicle should be reassigned to the charging station");
    }
    
    @Test
    void testRemoveOverdueVehicles() {
        // Add two cars to the waiting queue
        simulation.waitingQueue.offer(overdueCar);
        simulation.waitingQueue.offer(timelyCar);       
        simulation.removeOverdueVehicles(System.currentTimeMillis()); // Remove timed out vehicles

        // Check that only vehicles that have not timed out are left in the waiting queue
        assertFalse(simulation.waitingQueue.contains(overdueCar), "Vehicles that have exceeded the time limit should be removed");
        assertTrue(simulation.waitingQueue.contains(timelyCar), "Vehicles that have not timed out should still be in the queue");
    }
    
}

