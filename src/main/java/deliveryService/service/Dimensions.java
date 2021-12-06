package deliveryService.service;

import java.util.Properties;

public enum Dimensions {
    DIMENSIONS_BIG,
    DIMENSIONS_SMALL;

    private double dimensionsCommission;

    private void init() {
        Properties serviceProperties = Config.load();
        dimensionsCommission =
                Double.parseDouble(((String) serviceProperties.get(this.toString())).split(",")[0].trim());
    }

    public double getDimensionsCommission() {
        if (dimensionsCommission == 0) {
            init();
        }
        return dimensionsCommission;
    }
}
