package deliveryService.examples;

import deliveryService.cargo.Cargo;
import deliveryService.order.Order;
import deliveryService.service.DeliveryService;
import deliveryService.service.Workload;

import java.util.Random;
import java.util.Scanner;

public class DeliveryDemoEntry {

    public static void main(String[] args) {
        boolean retry = true;
        Scanner scanner = new Scanner(System.in);
        double width;
        double height;
        double depth;
        double distance;

        System.out.println("\n===DELIVERY CALCULATOR===\n");
        System.out.println(
                "Welcome to the delivery service!\n" +
                        "Please enter order details to get the price of your delivery\n" +
                        "\033[3m(Press Enter to submit)\033[0m");

        while (retry) {
            System.out.print("\nCargo size in cm: \n");
            while (true) {
                System.out.print("Width: ");
                String widthStr = scanner.nextLine();
                if (isNumeric(widthStr)) {
                    width = Double.parseDouble(widthStr);
                    break;
                } else {
                    System.out.println("Cargo width must be a number, please try again");
                }
            }
            while (true) {
                System.out.print("Height: ");
                String heightStr = scanner.nextLine();
                if (isNumeric(heightStr)) {
                    height = Double.parseDouble(heightStr);
                    break;
                } else {
                    System.out.println("Cargo height must be a number, please try again");
                }
            }
            while (true) {
                System.out.print("Depth: ");
                String depthStr = scanner.nextLine();
                if (isNumeric(depthStr)) {
                    depth = Double.parseDouble(depthStr);
                    break;
                } else {
                    System.out.println("Cargo depth must be a number, please try again");
                }
            }

            System.out.print("Is your cargo fragile (affects the cost of the delivery)? Yes/No: ");
            String fragile = scanner.nextLine();
            Cargo cargo = new Cargo(width, height, depth, fragile.equalsIgnoreCase("yes"));

            while (true) {
                System.out.print("Please specify your delivery distance in km: ");
                String distanceStr = scanner.nextLine();
                if (isNumeric(distanceStr)) {
                    distance = Double.parseDouble(distanceStr);
                    break;
                } else {
                    System.out.println("Order distance must be a number, please try again");
                }
            }

            Order order = new Order(cargo).setDistance(distance);

            DeliveryService deliveryService = new DeliveryService()
                    .setWorkload(Workload.values()[new Random().nextInt(Workload.values().length)]);
//            DeliveryService deliveryService = new DeliveryService()
//                    .setWorkload(Workload.values()[new Random().nextInt(Workload.values().length)])
//                    .checkMinimum(false)
//                    .setServiceCommission(1);

            deliveryService.calculateOrderDelivery(order).printResults();

            System.out.print("\n\nWant to try another order?: (Yes/No): ");
            retry = scanner.nextLine().equalsIgnoreCase("yes");
        }

        scanner.close();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
