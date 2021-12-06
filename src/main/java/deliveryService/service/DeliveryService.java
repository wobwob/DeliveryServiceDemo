package deliveryService.service;

import deliveryService.order.Order;

import java.util.Properties;

public class DeliveryService {
    static final String ZERO_DISTANCE_ERROR = "\nSorry, delivery distance is not specified or equals zero, " +
            "\nPlease try again";
    static final String ZERO_VOLUME_ERROR = "\nSorry, cargo volume is not specified or equals zero, " +
            "\nPlease try again";
    static final String FRAGILE_CARGO_ERROR =
            "\nSorry, delivery of a fragile cargo is not available for the specified distance";

    private final double palletVolume;
    private final double distanceLong;
    private final double minPrice;
    private double serviceCommission;
    private Workload workload;
    private boolean checkMinimum = true;

    public DeliveryService() {
        Properties serviceProperties = Config.load();
        this.palletVolume = Double.parseDouble(serviceProperties.getProperty("PALLET_WIDTH")) *
                Double.parseDouble(serviceProperties.getProperty("PALLET_HEIGHT")) *
                Double.parseDouble(serviceProperties.getProperty("PALLET_DEPTH"));
        this.distanceLong = Integer.parseInt(serviceProperties.getProperty("DISTANCE_LONG"));
        this.minPrice = Double.parseDouble(serviceProperties.getProperty("MIN_PRICE"));

        this.workload = Workload.REGULAR;
    }

    public DeliveryService checkMinimum(boolean checkMinimum) {
        this.checkMinimum = checkMinimum;
        return this;
    }

    public DeliveryService setServiceCommission(double serviceCommission) {
        this.serviceCommission = serviceCommission;

        return this;
    }

    public Workload getWorkload() {
        return this.workload;
    }

    public DeliveryService setWorkload(Workload workload) {
        this.workload = workload;

        return this;
    }

    public Delivery calculateOrderDelivery(Order order) {
        Delivery activeDelivery = new Delivery();
        activeDelivery.setOrder(order);

        if (isLongDistance(activeDelivery) && isFragile(activeDelivery)) {
            activeDelivery.setIsError(true);
            activeDelivery.setDeliveryPrice(-1);
            activeDelivery.setFragilityCommission(-1);
            String errorMessage =
                    FRAGILE_CARGO_ERROR +
                            String.format("\n%-40s%10.2fkm", "Maximum distance for a fragile cargo:", distanceLong) +
                            String.format("\n%-40s%10.2fkm", "Order distance:", activeDelivery.getOrder().getDistance());
            activeDelivery.setErrorMessage(errorMessage);
        } else {
            if (serviceCommission == 0) {
                activeDelivery.setDistanceCommission(calculateDistanceCommission(activeDelivery));
                activeDelivery.setDimensionsCommission(calculateDimensionsCommission(activeDelivery));
                activeDelivery.setFragilityCommission(calculateFragilityCommission(activeDelivery));
            }

            activeDelivery.setServiceFee(calculateServiceFee(activeDelivery));
            activeDelivery.setDeliveryPrice(calculateDeliveryPrice(activeDelivery));
        }

        return activeDelivery;
    }

    private boolean isBigSize(Delivery activeDelivery) {
        return activeDelivery.getOrder().getCargo().getVolume() > palletVolume;
    }

    private boolean isFragile(Delivery activeDelivery) {
        return activeDelivery.getOrder().getCargo().isFragile();
    }

    private boolean isLongDistance(Delivery activeDelivery) {
        return activeDelivery.getOrder().getDistance() > distanceLong;
    }

    private double calculateDistanceCommission(Delivery activeDelivery) {
        double distanceCommission = 0;

        if (activeDelivery.getOrder().getDistance() > 0) {
            if (activeDelivery.getOrder().getDistance() >= Distance.DISTANCE_FAR.getValue()) {
                distanceCommission = Distance.DISTANCE_FAR.getDistanceCommission();
            } else if (activeDelivery.getOrder().getDistance() >= Distance.DISTANCE_AVERAGE.getValue()) {
                distanceCommission = Distance.DISTANCE_AVERAGE.getDistanceCommission();
            } else if (activeDelivery.getOrder().getDistance() >= Distance.DISTANCE_SHORT.getValue()) {
                distanceCommission = Distance.DISTANCE_SHORT.getDistanceCommission();
            } else {
                distanceCommission = Distance.DISTANCE_DEFAULT.getDistanceCommission();
            }
        } else {
            activeDelivery.setIsError(true);
            activeDelivery.setErrorMessage(ZERO_DISTANCE_ERROR);
        }

        return distanceCommission;
    }

    private double calculateDimensionsCommission(Delivery activeDelivery) {
        double dimensionsCommission = 0;

        if (activeDelivery.getOrder().getCargo().getVolume() > 0) {
            dimensionsCommission = isBigSize(activeDelivery) ?
                    Dimensions.DIMENSIONS_BIG.getDimensionsCommission() :
                    Dimensions.DIMENSIONS_SMALL.getDimensionsCommission();
        } else {
            activeDelivery.setIsError(true);
            activeDelivery.setErrorMessage(ZERO_VOLUME_ERROR);
        }


        return dimensionsCommission;
    }

    private double calculateFragilityCommission(Delivery activeDelivery) {
        double fragilityCommission = 0;

        if (activeDelivery.getOrder().getCargo().isFragile()) {
            fragilityCommission = Fragile.FRAGILE.getFragileCommission();
        }

        return fragilityCommission;
    }

    private double calculateServiceFee(Delivery activeDelivery) {
        double interimDeliveryPrice;
        double serviceFee;

        interimDeliveryPrice = calculateInterimPrice(activeDelivery);
        serviceFee = interimDeliveryPrice * workload.getWorkloadMultiplier() - interimDeliveryPrice;

        return serviceFee;
    }

    private double calculateDeliveryPrice(Delivery activeDelivery) {
        double deliveryPrice;

        deliveryPrice = calculateInterimPrice(activeDelivery);
        deliveryPrice += activeDelivery.getServiceFee();

        String info = "";
        if (serviceCommission > 0) {
            info += "\n\033[3mPROMO CODE SUCCESSFULLY ACTIVATED\033[0m\n" +
                    "\033[3m(YOUR DELIVERY COST IS " +
                    serviceCommission + "RUB\033[0m)";
        }

        if (activeDelivery.getServiceFee() != 0) {
            info += "\n\033[3m* Service fee depends on the current workload\033[0m\n" +
                    "\033[3m(Current workload is " +
                    getWorkload().toString().toLowerCase().replace("_", " ") + "\033[0m)";
        }

        if (checkMinimum && (deliveryPrice < minPrice)) {
            deliveryPrice = activeDelivery.isError() ? -1 : minPrice;
            activeDelivery.setIsMinimumPayment(true);

            if (activeDelivery.getServiceFee() == 0) {
                info =
                        String.format("\n\033[3m* %.2fRUB is the minimum delivery price\033[0m", minPrice);
            } else {
                info +=
                        String.format("\n\033[3m** %.2fRUB is the minimum delivery price\033[0m", minPrice);
            }
        }
        activeDelivery.setInfo(info);

        return deliveryPrice;
    }

    private double calculateInterimPrice(Delivery activeDelivery) {
        double interimDeliveryPrice = 0;

        if (serviceCommission == 0) {
            interimDeliveryPrice += activeDelivery.getDistanceCommission();
            interimDeliveryPrice += activeDelivery.getDimensionsCommission();
            interimDeliveryPrice += activeDelivery.getFragilityCommission();
        } else {
            interimDeliveryPrice = serviceCommission;
        }

        return interimDeliveryPrice;
    }
}
