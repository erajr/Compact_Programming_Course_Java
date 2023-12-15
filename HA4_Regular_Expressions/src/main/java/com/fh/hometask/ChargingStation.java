package com.fh.hometask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fh.hometask.booking.Timeslot;
import com.fh.hometask.users.Administrator;
import com.fh.hometask.users.Customer;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ChargingStation {

    private static final Logger logger = LogManager.getLogger(ChargingStation.class);
    private static final Scanner scanner = new Scanner(System.in);

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        //
        showOptions();
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (choice) {
            case 1:
                System.out.println("Starting the Charging Station process...");
                handleChargingStation();
                break;
            case 2:
                System.out.println("Opening log file based on equipment name or date...");
                handleLogFileOperations();
                break;
            case 3:
                System.out.println("Exiting the Charging Station application...");
                break;
            default:
                System.out.println("Invalid choice! Please enter a valid option.");
                break;
        }

    }

    public static void showOptions() {
        System.out.println("Welcome to the Charging Station!");
        System.out.println("Choose an option:");
        System.out.println("1. Start the Charging Station process");
        System.out.println("2. Open log file based on equipment name or date");
        System.out.println("3. Quit");
        System.out.print("Enter your choice (1-3): ");
    }

    public static void handleChargingStation() {
        // Your existing logic for Charging Station process
        // ...

        boolean exit = false;

        while (!exit) {
            // Your existing logic...

            // Check for user input to exit
            System.out.println("Press 'Q' to quit the Charging Station process and return to main menu.");
            // if (scanner.hasNextLine()) {
            //     String userInput = scanner.nextLine();
            //     if (userInput.equalsIgnoreCase("Q")) {
            //         System.out.println("Exiting the Charging Station process...");
            //         exit = true;
            //     }
            // }

            // Rest of your charging station logic...
            // logic
            logger.debug("Debug message");
            logger.info("Info message");
            logger.warn("Warning message");
            logger.error("Error message");
            logger.fatal("Fatal message");
            logger.info("This is an example of an INFO level log message");

            try {
                // Generate some External users
                Customer user1 = new Customer(1, "John", 25);
                Customer user2 = new Customer(2, "Micheal", 49);

                // Generate some Administrators
                Administrator admin1 = new Administrator(1, "Bob", 47, "Manager Charging Station");
                Administrator admin2 = new Administrator(2, "Kevin", 52, "Head of Security");

                // Generate a Priority Queue based slot system and populate empty slots for next
                // 10 days.
                PriorityQueue<Timeslot> slots = new PriorityQueue<Timeslot>();
                int id = 1;
                int currentYear = LocalDateTime.now().getYear();
                int currentMonth = LocalDateTime.now().getMonthValue();
                int currentDayOfMonth = LocalDateTime.now().plusDays(1L).getDayOfMonth();
                for (int i = 0; i <= 9; i++) { // Day1 to Day10, Day 2
                    for (int j = 9; j <= 22; j++) { // Day2 ---> Day2 - 9:00 AM - 9:00PM
                        slots.add(new Timeslot(id, LocalDateTime.of(currentYear, currentMonth, currentDayOfMonth, j, 0),
                                null, false));
                        id++;
                    }
                    if (currentDayOfMonth >= 28) {
                        if (currentMonth == 2 && currentDayOfMonth == 29
                                && (currentYear == 2024 || currentYear == 2028 || currentYear == 2032)) {
                            currentDayOfMonth = 0;
                            currentMonth++;
                        }
                        if (currentMonth == 2 && currentDayOfMonth == 28
                                && (currentYear != 2024 || currentYear != 2028 || currentYear != 2032)) {
                            currentDayOfMonth = 0;
                            currentMonth++;
                        }
                        if ((currentMonth == 4 || currentMonth == 6 || currentMonth == 9 || currentMonth == 11)
                                && currentDayOfMonth == 30) {
                            currentDayOfMonth = 0;
                            currentMonth++;
                        }
                        if ((currentMonth == 1 || currentMonth == 3 || currentMonth == 5 || currentMonth == 7
                                || currentMonth == 8 || currentMonth == 10) && currentDayOfMonth == 31) {
                            currentDayOfMonth = 0;
                            currentMonth++;
                        }
                        if (currentMonth == 12 && currentDayOfMonth == 31) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            // --- Create, move, delete, archive logs functions below:
            // LoggingSystem.readFromLogFile("bufferedlog.txt");
            LoggingSystem.readFromLogFileWithLine("bufferedlog.txt", 139);
            LoggingSystem.deleteLogFile("bufferedlog2.txt");
            // LoggingSystem.moveLogFile("bufferedlog.txt", "newFolder");
            LoggingSystem.archiveLogFile("bufferedlog.txt");
            // ---------------------------------------------------------------------------
            // Hometask 3 test simulation below:
            // ---------------------------------------------------------------------------
            ConcurrencySimulation simulation = new ConcurrencySimulation();
            simulation.simulateVehicleArrival();
        }

        // After exiting charging station, show the main menu options
        showOptions();
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (choice) {
            // Handle main menu options...
            case 1:
                System.out.println("Starting the Charging Station process...");
                handleChargingStation();
                break;
            case 2:
                System.out.println("Opening log file based on equipment name or date...");
                handleLogFileOperations();
                break;
            case 3:
                System.out.println("Exiting the Charging Station application...");
                break;
            default:
                System.out.println("Invalid choice! Please enter a valid option.");
                break;
        }
    }

    public static void handleLogFileOperations() {
        System.out.println("Choose an option for log file:");
        System.out.println("1. Open log file based on equipment name");
        System.out.println("2. Open log file based on date");
        System.out.print("Enter your choice (1-2): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (choice) {
            case 1:
                System.out.print("Enter equipment name: ");
                String equipmentName = scanner.nextLine();
                System.out.print(equipmentName);
                openLogFileByEquipmentName(equipmentName);
                break;
            case 2:
                System.out.print("Enter date (format: dd/MM/yyyy): ");
                String dateString = scanner.nextLine();
                System.out.print(dateString);
                openLogFileByDate(dateString);
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    public static void openLogFileByEquipmentName(String equipmentName) {
        String logFolder = "logs/2023-12"; // Path to your logs folder

        // Get the file path based on the equipment name
        String filePath = getLogFilePathByEquipmentName(logFolder, equipmentName);

        if (filePath != null) {
            System.out.println("Opening log file: " + filePath);
            try {
                // Open the file using the default OS program
                ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", filePath);
                processBuilder.start();
            } catch (IOException e) {
                System.out.println("Error opening the log file: " + e.getMessage());
            }
        } else {
            System.out.println("No log file found for equipment: " + equipmentName);
        }
    }

    public static String getLogFilePathByEquipmentName(String logFolder, String equipmentName) {
        File folder = new File(logFolder);

        // Check if the logs folder exists
        if (folder.exists() && folder.isDirectory()) {
            // List all files in the logs folder
            File[] files = folder.listFiles();

            if (files != null) {
                // Iterate through the files to find a match based on equipmentName
                for (File file : files) {
                    if (file.isFile() && file.getName().contains(equipmentName)) {
                        return file.getAbsolutePath();
                    }
                }
            }
        }
        // Return null if no log file is found for the provided equipment name
        return null;
    }


    public static void openLogFileByDate(String dateString) {
        // Assuming a file path pattern based on the date, modify this according to your
        // file naming conventions
        String filePath = "logs/" + dateString + "_log.txt";

        File file = new File(filePath);
        if (file.exists()) {
            try {
                // Open the log file using appropriate mechanisms (e.g., FileReader,
                // BufferedReader, etc.)
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line;
                System.out.println("Log file content for date " + dateString + ":");

                // Read and display the content of the log file
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }

                // Close resources
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error reading the log file: " + e.getMessage());
            }
        } else {
            System.out.println("Log file for date '" + dateString + "' does not exist.");
        }
    }

    public static void bookSlotById(int id, Customer user, PriorityQueue<Timeslot> slots) {
        if (id > slots.stream().mapToInt(slot -> slot.getId()).max().getAsInt()
                || id < slots.stream().mapToInt(slot -> slot.getId()).min().getAsInt()) {
            LoggingSystem.log(Level.INFO, ChargingStation.class.getName(), "Error booking slot - Invalid ID " + id);
        } else {
            slots.forEach(slot -> {
                if (slot.getId() == id) {
                    if (slot.getName() == null && !slot.getIsBooked()) {
                        slot.setIsBooked(true);
                        LoggingSystem.log(Level.INFO, ChargingStation.class.getName(),
                                "Charging Slot of ID " + id + " booked by " + user.getName() + " successfully.");
                    } else {
                        LoggingSystem.log(Level.INFO, ChargingStation.class.getName(), "Error booking slot of ID " + id
                                + " by " + user.getName() + " - Slot already booked. Find other slot.");
                    }
                }
            });
        }
    }

    public static void checkSlotAvailability(int id, PriorityQueue<Timeslot> slots) {
        DateTimeFormatter formatWholeDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter formatOnlyTime = DateTimeFormatter.ofPattern("HH:mm");
        Timeslot slot = slots.stream().filter(match -> match.getId() == id).findFirst().orElse(null);
        if (slot == null) {
            LoggingSystem.log(Level.INFO, ChargingStation.class.getName(), "Slot with given ID not exist");
        } else {
            if (slot.getIsBooked()) {
                LoggingSystem.log(Level.INFO, ChargingStation.class.getName(),
                        "Timeslot: " + slot.getId() + " --- " + formatWholeDate.format(slot.getTimeSlot()) + " to "
                                + formatOnlyTime.format(slot.getTimeSlot().plusHours(1L)) + ": booked");
            } else {
                LoggingSystem.log(Level.INFO, ChargingStation.class.getName(),
                        "Timeslot: " + slot.getId() + " --- " + formatWholeDate.format(slot.getTimeSlot()) + " to "
                                + formatOnlyTime.format(slot.getTimeSlot().plusHours(1L)) + ": available");
            }
        }
    }

}
