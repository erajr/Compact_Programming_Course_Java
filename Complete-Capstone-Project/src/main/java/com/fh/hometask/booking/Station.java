package com.fh.hometask.booking;

public class Station {
    
    public final int stationId;
    public enum EnergySource {
        HYDROELECTRIC, SOLAR, WIND
    }
    public EnergySource energySource;
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

    public int getStationId() {
        return stationId;
    }

    public EnergySource getEnergySource() {
        return energySource;
    }

    public void setEnergySource(EnergySource energySource) {
        this.energySource = energySource;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

}
