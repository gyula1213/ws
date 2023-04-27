package hu.bme.mit.brszta.data;

public class Truck extends Vehicle {

    // védett attribútum, leszármazott osztályok is láthatják
    private float tirePressure;

    public Truck(String plate) {
        super(plate);
        this.tirePressure = 0.0f;
    }

    // publikus metódus, bárki felfújhatja a kerekeket
    public void inflateTire() {
        this.tirePressure = 1.0f;
    }

    // publikus metódus, bárki megnézheti, hogy szükséges-e szervíz
    public boolean isMaintenanceRequired() {
        return this.getOdometer() > 400000;
    }

    public float getTirePressure() {
        return tirePressure;
    }
}
