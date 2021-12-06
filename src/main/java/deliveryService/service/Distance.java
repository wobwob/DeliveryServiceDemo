package deliveryService.service;

import java.util.Properties;

public enum Distance {
    DISTANCE_FAR,
    DISTANCE_AVERAGE,
    DISTANCE_SHORT,
    DISTANCE_DEFAULT;

    private double distance;
    private double distanceCommission;

    private void init() {
        Properties serviceProperties = Config.load();
        distance = Double.parseDouble(((String) serviceProperties.get(this.toString())).split(",")[0].trim());
        distanceCommission =
                Double.parseDouble(((String) serviceProperties.get(this.toString())).split(",")[1].trim());
    }

    public double getValue() {
        if (distance == 0) {
            init();
        }
        return distance;
    }

    public double getDistanceCommission() {
        if (distanceCommission == 0) {
            init();
        }
        return distanceCommission;
    }
}
