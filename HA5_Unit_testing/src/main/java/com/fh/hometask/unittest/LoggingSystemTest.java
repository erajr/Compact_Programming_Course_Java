package com.fh.hometask.unittest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fh.hometask.LoggingSystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class LoggingSystemTest {

    private static final String LOG_FILE_NAME = "bufferedlog.txt";
    private static final String DESTINATION_FOLDER = "test_logs";
    private static final String ZIP_EXTENSION = ".zip";

    @BeforeEach
    void setUp() {
        
    	LoggingSystem.deleteLogFile(LOG_FILE_NAME); // Ensure that log files do not exist before each test begins
        Path destinationFolderPath = Paths.get(System.getProperty("user.dir"), DESTINATION_FOLDER);
        deleteDirectory(destinationFolderPath.toFile());
    }

    @AfterEach
    void tearDown() {
        
        LoggingSystem.deleteLogFile(LOG_FILE_NAME);// Delete log files after each test is completed
        Path destinationFolderPath = Paths.get(System.getProperty("user.dir"), DESTINATION_FOLDER);
        deleteDirectory(destinationFolderPath.toFile());
    }
    
    @Test
    void testLogContent() throws IOException {
        String className = "TestClass";
        String message = "Test log message";
        LoggingSystem.log(Level.INFO, className, message); // Write Log

        // Read log file content
        Path logFilePath = Paths.get(System.getProperty("user.dir"), LOG_FILE_NAME);
        String logContent = new String(Files.readAllBytes(logFilePath));

        assertTrue(logContent.contains(className) && logContent.contains(message), "The log file should include the class name and message");
    }

    @Test
    void testLogCreationAndAppend() {
        LoggingSystem.log(Level.INFO, "TestClass", "Test message");
        Path logFilePath = Paths.get(System.getProperty("user.dir"), LOG_FILE_NAME);
        assertTrue(Files.exists(logFilePath), "The log file should be created");

        // Verify log content
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath.toFile()))) {
            String line = reader.readLine();
            assertNotNull(line, "The log file should contain content");
            assertTrue(line.contains("Test message"), "The log content should include test messages");
        } catch (Exception e) {
            fail("An error occurred while reading the log file");
        }
    }

    @Test
    void testDeleteLogFile() {
        // Create some logs
        LoggingSystem.log(Level.INFO, "TestClass", "Test message");

        // Delete log files and verify
        LoggingSystem.deleteLogFile(LOG_FILE_NAME);
        Path logFilePath = Paths.get(System.getProperty("user.dir"), LOG_FILE_NAME);
        assertFalse(Files.exists(logFilePath), "The log file should be deleted");
    }

    @Test
    void testMoveLogFile() {
        LoggingSystem.log(Level.INFO, "TestClass", "Test message");
        LoggingSystem.moveLogFile(LOG_FILE_NAME, DESTINATION_FOLDER);

        Path destinationFilePath = Paths.get(System.getProperty("user.dir"), DESTINATION_FOLDER, LOG_FILE_NAME);
        assertTrue(Files.exists(destinationFilePath), "The log file should be moved to the target folder");
        assertFalse(Files.exists(Paths.get(System.getProperty("user.dir"), LOG_FILE_NAME)), "The original log file should no longer exist");
    }

    @Test
    void testArchiveLogFile() {
        LoggingSystem.log(Level.INFO, "TestClass", "Test message");
        LoggingSystem.archiveLogFile(LOG_FILE_NAME);

        Path zipFilePath = Paths.get(System.getProperty("user.dir"), LOG_FILE_NAME + ZIP_EXTENSION);
        assertTrue(Files.exists(zipFilePath), "The log file should be archived as a ZIP file");

        // Verify the content of the ZIP file
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath.toFile()))) {
            ZipEntry entry = zis.getNextEntry();
            assertNotNull(entry, "The ZIP file should contain entries");
            assertEquals(LOG_FILE_NAME, entry.getName(), "The entry name of the ZIP file should match the original file name");
        } catch (Exception e) {
            fail("Error reading ZIP file");
        }
    }

    // Auxiliary methods for deleting directories and their contents
    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

}
