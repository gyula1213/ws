package hu.bme.mit.brszta.data;

import java.io.Serializable;

public class Vehicle implements Serializable {

    // privát attribútum, amely csak az osztályon belül érhető el
    private int odometer;

    // publikus attribútum, amely kívülről is elérhető
    private String licencePlate;

    public Vehicle(String licencePlate) {
        this.licencePlate = licencePlate;
        this.odometer = 0;
    }

    public int getOdometer() {
        return odometer;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    // publikus metódus, bárki vezetheti a buszt
    public void drive(int distance) {
        this.odometer += distance;
    }
}
