package com.fh.hometask.booking;

public class Station {
    
    public final int stationId;
    public boolean isOccupied;

    public Station(int stationId) {
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
