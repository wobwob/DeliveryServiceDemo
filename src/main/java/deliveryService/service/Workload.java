package deliveryService.service;

import java.util.Properties;

public enum Workload {
    VERY_HIGH,
    HIGH,
    INCREASED,
    REGULAR;

    private double workloadMultiplier;

    private void init() {
        Properties serviceProperties = Config.load();
        workloadMultiplier =
                Double.parseDouble(((String) serviceProperties.get(this.toString())).split(",")[0].trim());
    }

    public double getWorkloadMultiplier() {
        if (workloadMultiplier == 0) {
            init();
        }
        return workloadMultiplier;
    }
}
