package deliveryService.service;

import deliveryService.order.Order;

public class Delivery {
    private Order order;
    private double distanceCommission;
    private double dimensionsCommission;
    private double fragilityCommission;
    private double serviceFee;
    private double deliveryPrice = -1;
    private boolean isMinimumPayment;
    private boolean isError;
    private String errorMessage;
    private String info;

    public boolean isMinimumPayment() {
        return isMinimumPayment;
    }

    public Order getOrder() {
        return order;
    }

    void setOrder(Order order) {
        this.order = order;
    }

    public double getDistanceCommission() {
        return distanceCommission;
    }

    void setDistanceCommission(double distanceCommission) {
        this.distanceCommission = distanceCommission;
    }

    public double getDimensionsCommission() {
        return dimensionsCommission;
    }

    void setDimensionsCommission(double dimensionsCommission) {
        this.dimensionsCommission = dimensionsCommission;
    }

    public double getFragilityCommission() {
        return fragilityCommission;
    }

    void setFragilityCommission(double fragilityCommission) {
        this.fragilityCommission = fragilityCommission;
    }

    void setIsMinimumPayment(boolean isMinimumPayment) {
        this.isMinimumPayment = isMinimumPayment;
    }

    public boolean isError() {
        return isError;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    void setIsError(boolean error) {
        this.isError = error;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getInfo() {
        return info;
    }

    void setInfo(String info) {
        this.info = info;
    }

    public void printResults() {
        if (isError) {
            System.out.println(errorMessage);
        } else {
            printOrderDetails();
            if (info != null && !info.trim().isEmpty()) {
                System.out.println(info);
            }
        }
    }

    private void printOrderDetails() {
        String details =
                String.format("%-15s%28.2fkm\n", "Distance:", order.getDistance()) +
                        String.format("%-15s%30b\n", "Fragile:", order.getCargo().isFragile()) +
                        String.format("%-15s%30s\n\n\n", "Cargo size:", String.format("%.2fcm x %.2fcm x %.2fcm",
                                order.getCargo().getWidth(),
                                order.getCargo().getHeight(),
                                order.getCargo().getDepth())) +
                        String.format("%-30s%12.2fRUB\n", "Delivery distance commission:", distanceCommission) +
                        String.format("%-30s%12.2fRUB\n", "Cargo dimensions commission:", dimensionsCommission) +
                        String.format("%-30s%12.2fRUB\n", "Fragile cargo commission:", fragilityCommission);

        String fee = "";
        String total = String.format("%-30s%12.2fRUB", "TOTAL:", deliveryPrice);
        if (serviceFee != 0) {
            fee = String.format("\n%-30s%12.2fRUB\n", "Service fee:*", serviceFee);
            if (isMinimumPayment) {
                total = String.format("%-30s%12.2fRUB", "TOTAL:**", deliveryPrice);
            }
        } else {
            if (isMinimumPayment) {
                total = String.format("%-30s%12.2fRUB", "TOTAL:*", deliveryPrice);
            }
        }

        System.out.println(
                "\nORDER DETAILS\n" +
                        "----------------------------------------------\n" +
                        details +
                        fee +
                        "----------------------------------------------\n" +
                        total);
    }
}
