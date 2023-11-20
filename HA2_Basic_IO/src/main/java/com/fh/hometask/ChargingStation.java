package com.fh.hometask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.logging.Level;

import com.fh.hometask.booking.Timeslot;
import com.fh.hometask.users.Administrator;
import com.fh.hometask.users.Customer;


public class ChargingStation 
{

    @SuppressWarnings("unused")
    public static void main( String[] args )
    {

        try {
            // Generate some External users
            Customer user1 = new Customer(1, "John", 25);
            Customer user2 = new Customer(2, "Micheal", 49);

            // Generate some Administrators
            Administrator admin1 = new Administrator(1, "Bob", 47, "Manager Charging Station");
            Administrator admin2 = new Administrator(2, "Kevin", 52, "Head of Security");

            // Generate a Priority Queue based slot system and populate empty slots for next 10 days.
            PriorityQueue<Timeslot> slots = new PriorityQueue<Timeslot>();
            int id = 1;
            int currentYear = LocalDateTime.now().getYear();
            int currentMonth = LocalDateTime.now().getMonthValue();
            int currentDayOfMonth = LocalDateTime.now().plusDays(1L).getDayOfMonth();
            for(int i = 0; i <= 9; i++) { // Day1 to Day10, Day 2
                for(int j = 9; j <= 22; j++) { // Day2 ---> Day2 - 9:00 AM - 9:00PM
                    slots.add(new Timeslot(id, LocalDateTime.of(currentYear, currentMonth, currentDayOfMonth, j, 0), null, false));
                    id++;
                }
                if(currentDayOfMonth >= 28) {
                    if(currentMonth == 2 && currentDayOfMonth == 29 && (currentYear == 2024 || currentYear == 2028 || currentYear == 2032)) {
                        currentDayOfMonth = 0;
                        currentMonth++;
                    }
                    if(currentMonth == 2 && currentDayOfMonth == 28 && (currentYear != 2024 || currentYear != 2028 || currentYear != 2032)) {
                        currentDayOfMonth = 0;
                        currentMonth++;
                    }
                    if((currentMonth == 4 || currentMonth == 6 || currentMonth == 9 || currentMonth == 11) && currentDayOfMonth == 30) {
                        currentDayOfMonth = 0;
                        currentMonth++;
                    }
                    if((currentMonth == 1 || currentMonth == 3 || currentMonth == 5 || currentMonth == 7 || currentMonth == 8 || currentMonth == 10) && currentDayOfMonth == 31) {
                        currentDayOfMonth = 0;
                        currentMonth++;
                    }
                    if(currentMonth == 12 && currentDayOfMonth == 31) {
                        currentYear++;
                        currentMonth = 1;
                        currentDayOfMonth = 0;
                    }
                }
                currentDayOfMonth++;
            }

            // Check available slots before booking:
            checkSlotAvailability(194, slots); // not exist
            checkSlotAvailability(29, slots); // available
            checkSlotAvailability(39, slots); // available

            // Book some random slots
            bookSlotById(29, user1, slots); // slot booked successfully
            bookSlotById(29, user2, slots); // unable to book slot - slot not available
            bookSlotById(194, user2, slots); // unable to book slot - id not exist

            // Check available slots after booking:
            checkSlotAvailability(194, slots); // not exist
            checkSlotAvailability(29, slots); // booked
            checkSlotAvailability(39, slots); // available
        } catch(Exception e) {
            e.printStackTrace();
        }

        // --- Create, move, delete, archive logs functions below:
        // LoggingSystem.readFromLogFile("bufferedlog.txt");
        LoggingSystem.readFromLogFileWithLine("bufferedlog.txt", 139);
        LoggingSystem.deleteLogFile("bufferedlog2.txt");
        // LoggingSystem.moveLogFile("bufferedlog.txt", "newFolder");
        LoggingSystem.archiveLogFile("bufferedlog.txt");
    }

    public static void bookSlotById(int id, Customer user, PriorityQueue<Timeslot> slots) {
        if(id > slots.stream().mapToInt(slot -> slot.getId()).max().getAsInt() || id < slots.stream().mapToInt(slot -> slot.getId()).min().getAsInt()) {
            LoggingSystem.log(Level.INFO, ChargingStation.class.getName(), "Error booking slot - Invalid ID " + id);
        } else {
            slots.forEach(slot -> {
                if(slot.getId() == id) {
                    if(slot.getName() == null && !slot.getIsBooked()) {
                        slot.setIsBooked(true);
                        LoggingSystem.log(Level.INFO, ChargingStation.class.getName(), "Charging Slot of ID " + id + " booked by " + user.getName() + " successfully.");
                    } else {
                        LoggingSystem.log(Level.INFO, ChargingStation.class.getName(), "Error booking slot of ID " + id + " by " + user.getName() + " - Slot already booked. Find other slot.");
                    }
                }
            });
        }
    }

    public static void checkSlotAvailability(int id, PriorityQueue<Timeslot> slots) {
        DateTimeFormatter formatWholeDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter formatOnlyTime = DateTimeFormatter.ofPattern("HH:mm");
        Timeslot slot = slots.stream().filter(match -> match.getId() == id).findFirst().orElse(null);
        if(slot == null) {
            LoggingSystem.log(Level.INFO, ChargingStation.class.getName(), "Slot with given ID not exist");
        } else {
            if(slot.getIsBooked()) {
                LoggingSystem.log(Level.INFO, ChargingStation.class.getName(), "Timeslot: " + slot.getId() + " --- " + formatWholeDate.format(slot.getTimeSlot()) + " to " + formatOnlyTime.format(slot.getTimeSlot().plusHours(1L)) + ": booked");
            } else {
                LoggingSystem.log(Level.INFO, ChargingStation.class.getName(), "Timeslot: " + slot.getId() + " --- " + formatWholeDate.format(slot.getTimeSlot()) + " to " + formatOnlyTime.format(slot.getTimeSlot().plusHours(1L)) + ": available");
            }
        }
    }

}
