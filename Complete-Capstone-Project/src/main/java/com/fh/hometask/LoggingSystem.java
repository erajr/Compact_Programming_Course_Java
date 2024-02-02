package com.fh.hometask;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.ConsoleAppender.Target;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class LoggingSystem {
    static DateTimeFormatter formatWholeDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");
    
    // Using Character Stream to Create log file and append logs in that file
    public static void log(Level level, String className, String msg){
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("bufferedlog.txt", true)))) {
            writer.println(formatWholeDate.format(LocalDateTime.now()) + " --- " + className + " --- " + level + ": " + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(msg);
    }

    // Using Byte Stream to read logs from the given file
    public static void readFromLogFile(String logFileName) {
        try(FileInputStream inputStream = new FileInputStream(logFileName)) {
            StringBuilder line = new StringBuilder();
            int byteInfo;
            while((byteInfo = inputStream.read()) != -1) {
                char character = (char) byteInfo;
                if (character == '\n') {
                    System.out.println(line.toString());
                    line = new StringBuilder();
                } else {
                    line.append(character);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Using Byte Stream to read logs of a file (with a given line number) 
    public static void readFromLogFileWithLine(String logFileName, int lineNumber) {
        try(FileInputStream inputStream = new FileInputStream(logFileName)) {
            StringBuilder line = new StringBuilder();
            int byteInfo;
            int fileLine = 1;
            while(fileLine < lineNumber && (byteInfo = inputStream.read()) != -1) {
                char character = (char) byteInfo;
                if (character == '\n') {
                    fileLine++;
                }
            }

            if(fileLine == lineNumber) {
                while((byteInfo = inputStream.read()) != -1) {
                    char character = (char) byteInfo;
                    if (character == '\n') {
                        System.out.println("Log at line " + lineNumber + ": " + line.toString());
                        break;
                    } else {
                        line.append(character);
                    }
                }
            } else {
                System.out.println("Line not found in the given file.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete the log file
    public static void deleteLogFile(String fileName) {
        Path path = Paths.get(System.getProperty("user.dir"), fileName);
        try {
            if(path.toFile().exists()) {
                Files.deleteIfExists(path);
                System.out.println("File " + fileName + " deleted successfully");
            } else {
                System.out.println("File " + fileName + " does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Move the log file(s)
    public static void moveLogFile(String fileName, String destinationFolder) {
        Path source = Paths.get(System.getProperty("user.dir"), fileName);
        Path destination = Paths.get(System.getProperty("user.dir"), destinationFolder);
        try {
            Files.createDirectories(destination);
            Path destinationFilePath = destination.resolve(source.getFileName());
            if(destinationFilePath.toFile().exists()) {
                System.out.println("File already existed. Overwriting...");
                destinationFilePath.toFile().delete();
            }
            Files.move(source, destinationFilePath);
            System.out.println("File moved from " + source + " to " + destinationFilePath);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Archive the log file(s)
    public static void archiveLogFile(String fileName) {
        Path filePath = Paths.get(System.getProperty("user.dir"), fileName);
        Path zipFilePath = Paths.get(System.getProperty("user.dir"), fileName + ".zip");
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()));
            FileInputStream fos = new FileInputStream(filePath.toFile())) {
            ZipEntry entry = new ZipEntry(fileName);
            zos.putNextEntry(entry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fos.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            System.out.println("Log file archived successfully to: " + zipFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------
    // LOG4J2.XML Code Dynamic
    // ----------------------------------------------------------

    public void initiateLoggers(int MAX_SIZE) {
        String consoleAppenderPattern = "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n";
        String fileAppenderPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n";
        String rollingFileAppenderPattern = "%d{yyyyMMdd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n";

        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();

        PatternLayout consolePatternLayout = PatternLayout.newBuilder().withConfiguration(config).withPattern(consoleAppenderPattern).build();
        PatternLayout fileAppenderPatternLayout = PatternLayout.newBuilder().withConfiguration(config).withPattern(fileAppenderPattern).build();
        PatternLayout rollingFileAppenderPatternLayout = PatternLayout.newBuilder().withConfiguration(config).withPattern(rollingFileAppenderPattern).build();

        FileAppender fileAppender = FileAppender.newBuilder()
            .setConfiguration(config)
            .setName("ChargingSystem")
            .setLayout(fileAppenderPatternLayout)
            .withFileName("logs/System.log")
            .build();

        fileAppender.start();
        config.addAppender(fileAppender);

        ConsoleAppender console = ConsoleAppender.newBuilder()
            .setConfiguration(config)
            .setLayout(consolePatternLayout)
            .setName("Console")
            .setTarget(Target.SYSTEM_OUT)
            .build();

        console.start();
        config.addAppender(console);

        AppenderRef consoleAppenderRef = AppenderRef.createAppenderRef("Console", null, null);
        AppenderRef fileAppenderRef = AppenderRef.createAppenderRef("ChargingSystem", null, null);
        AppenderRef[] refs = new AppenderRef[] { consoleAppenderRef, fileAppenderRef };
        LoggerConfig loggerConfig = LoggerConfig.createLogger(false, org.apache.logging.log4j.Level.INFO, "ChargingSystem", "true", refs, null, config, null);
        loggerConfig.addAppender(fileAppender, null, null);
        loggerConfig.addAppender(console, null, null);
    
        Map<String, RollingFileAppender> rollingFileAppenders = new HashMap<>();

        for(int i = 1; i <= MAX_SIZE; i++) {
            String appenderName = "chargingStation" + i;
            String fileName = "logs/ChargingStation-" + i + ".log";
            String filePattern = "logs/%d{yyyy-MM}/ChargingStation-" + i + "-%d{-dd-MMMM-yyyy}-%i.log";

            CompositeTriggeringPolicy multiplePolicies = 
                CompositeTriggeringPolicy.createPolicy(
                    OnStartupTriggeringPolicy.createPolicy(1L), 
                    SizeBasedTriggeringPolicy.createPolicy("10 MB"), 
                    TimeBasedTriggeringPolicy.newBuilder().build());

            multiplePolicies.start();

            RollingFileAppender appender = RollingFileAppender.newBuilder()
                .setConfiguration(config)
                .setName(appenderName)
                .setLayout(rollingFileAppenderPatternLayout)
                .withCreateOnDemand(false)
                .withPolicy(multiplePolicies)
                .withBufferSize(8192)
                .withBufferedIo(true)
                .setIgnoreExceptions(true)
                .withFilePermissions("-rwxr-xr-x")
                .withFileName(fileName)
                .withFilePattern(filePattern)
                .withAppend(true)
                .build();

                
            rollingFileAppenders.put(appenderName, appender);
            appender.start();
            config.addAppender(appender);

            AppenderRef rollingFileAppenderRef = AppenderRef.createAppenderRef(appenderName, org.apache.logging.log4j.Level.INFO, null);
            AppenderRef[] rollingFileAppenderRefs = new AppenderRef[] { rollingFileAppenderRef };
            LoggerConfig rollingFileLoggerConfig = LoggerConfig.createLogger(false, org.apache.logging.log4j.Level.INFO, appenderName, "true", rollingFileAppenderRefs, null, config, null);
            rollingFileLoggerConfig.addAppender(appender, null, null);
            config.addLogger(appenderName, rollingFileLoggerConfig);
        }

        config.addLogger("ChargingSystem", loggerConfig);
        context.updateLoggers();

    }

}
