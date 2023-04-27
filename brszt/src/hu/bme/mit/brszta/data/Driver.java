package hu.bme.mit.brszta.data;

public class Driver {

    private String name;

    private Truck truck;

    public Driver(String name, Truck truck) {
        this.name = name;
        this.truck = truck;
    }

    public void performFuryRoad() {
        if (this.truck.getTirePressure() < 0.5f) {
            this.truck.inflateTire();
        }
        this.truck.drive(100000);
    }
}
