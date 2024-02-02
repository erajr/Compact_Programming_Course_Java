package com.fh.hometask.booking;

import java.time.LocalDateTime;
import java.util.logging.Level;

import com.fh.hometask.LoggingSystem;

public class Timeslot implements Comparable<Timeslot> {
    private int id;
    private LocalDateTime timeSlot;
    private String name;
    private Boolean isBooked;

    public Timeslot(int id, LocalDateTime timeSlot, String name, Boolean isBooked) {
        this.id = id;
        this.timeSlot = timeSlot;
        this.name = name;
        this.isBooked = isBooked;
        LoggingSystem.log(Level.INFO, Timeslot.class.getName(), "New timeslot added: ID " + id + ", timeSlot: " + timeSlot + ", name: " + name + ", booked: " + isBooked); 
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public LocalDateTime getTimeSlot() {
        return timeSlot;
    }
    public void setTimeSlot(LocalDateTime timeSlot) {
        this.timeSlot = timeSlot;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Boolean getIsBooked() {
        return isBooked;
    }
    public void setIsBooked(Boolean isBooked) {
        this.isBooked = isBooked;
    }
    @Override
    public int compareTo(Timeslot o) {
        return this.timeSlot.compareTo(o.timeSlot);
    }

}
