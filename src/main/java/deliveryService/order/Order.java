package deliveryService.order;

import deliveryService.cargo.Cargo;

public class Order {
    private double distance;
    private final Cargo cargo;

    public Order(Cargo cargo) {
        this.cargo = cargo;
    }

    public Order setDistance(double distance) {
        this.distance = distance;

        return this;
    }

    public double getDistance() {
        return distance;
    }

    public Cargo getCargo() {
        return cargo;
    }
}
