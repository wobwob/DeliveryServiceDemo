package deliveryService.service;

import java.util.Properties;

public enum Fragile {
    FRAGILE;

    private double fragileCommission;

    private void init() {
        Properties serviceProperties = Config.load();
        fragileCommission =
                Double.parseDouble(((String) serviceProperties.get(this.toString())).split(",")[0].trim());
    }

    public double getFragileCommission() {
        if (fragileCommission == 0) {
            init();
        }
        return fragileCommission;
    }
}
