package com.fh.hometask.car;

public class Car {

    private long arrivalTime;
    private int id;

    public Car(long arrivalTime, int id) {
        this.arrivalTime = arrivalTime;
        this.id = id;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
