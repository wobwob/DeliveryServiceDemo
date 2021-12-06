package deliveryService.service;

import deliveryService.DeliveryTests;
import deliveryService.cargo.Cargo;
import deliveryService.order.Order;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

@Description("Unit tests for DeliveryService class")
@DisplayName("Delivery service should")
class DeliveryServiceTest extends DeliveryTests {

    @Test
    @DisplayName("have regular workload by default")
    void haveRegularWorkloadByDefault() {
        Assertions.assertEquals(deliveryService.getWorkload(), Workload.REGULAR);
    }

    @Test
    @DisplayName("be able to assign and return workload value")
    void beAbleToHandleWorkload() {
        Workload serviceWorkload = Workload.values()[new Random().nextInt(Workload.values().length)];
        deliveryService.setWorkload(serviceWorkload);

        Assertions.assertEquals(deliveryService.getWorkload(), serviceWorkload);
    }

    // DISTANCE COMMISSION
    @Test
    @DisplayName("calculate distance commission (> far)")
    void distanceMoreThanFar() {
        order.setDistance(Distance.DISTANCE_FAR.getValue() + 1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(Distance.DISTANCE_FAR.getDistanceCommission(), delivery.getDistanceCommission());
    }

    @Test
    @DisplayName("calculate distance commission (== far)")
    void distanceFar() {
        order.setDistance(Distance.DISTANCE_FAR.getValue());
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(Distance.DISTANCE_FAR.getDistanceCommission(), delivery.getDistanceCommission());
    }

    @Test
    @DisplayName("calculate distance commission (< far)")
    void distanceLessThanFar() {
        order.setDistance(Distance.DISTANCE_FAR.getValue() - 1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(Distance.DISTANCE_AVERAGE.getDistanceCommission(), delivery.getDistanceCommission());
    }

    @Test
    @DisplayName("calculate distance commission (> average)")
    void distanceMoreThanAverage() {
        order.setDistance(Distance.DISTANCE_AVERAGE.getValue() + 1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(Distance.DISTANCE_AVERAGE.getDistanceCommission(), delivery.getDistanceCommission());
    }

    @Test
    @DisplayName("calculate distance commission (== average)")
    void distanceAverage() {
        order.setDistance(Distance.DISTANCE_AVERAGE.getValue());
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(Distance.DISTANCE_AVERAGE.getDistanceCommission(), delivery.getDistanceCommission());
    }

    @Test
    @DisplayName("calculate distance commission (< average)")
    void distanceLessThanAverage() {
        order.setDistance(Distance.DISTANCE_AVERAGE.getValue() - 1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(Distance.DISTANCE_SHORT.getDistanceCommission(), delivery.getDistanceCommission());
    }

    @Test
    @DisplayName("calculate distance commission (> short)")
    void distanceMoreThanShort() {
        order.setDistance(Distance.DISTANCE_SHORT.getValue() + 1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(Distance.DISTANCE_SHORT.getDistanceCommission(), delivery.getDistanceCommission());
    }

    @Test
    @DisplayName("calculate distance commission (== short)")
    void distanceShort() {
        order.setDistance(Distance.DISTANCE_SHORT.getValue());
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(Distance.DISTANCE_SHORT.getDistanceCommission(), delivery.getDistanceCommission());
    }

    @Test
    @DisplayName("calculate distance commission (< short)")
    void distanceLessThanShort() {
        order.setDistance(Distance.DISTANCE_SHORT.getValue() - 1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(Distance.DISTANCE_DEFAULT.getDistanceCommission(), delivery.getDistanceCommission());
    }

    @Test
    @DisplayName("calculate distance commission for negative distance")
    void negativeDistance() {
        order.setDistance(-1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(0, delivery.getDistanceCommission()),
                () -> Assertions.assertTrue(delivery.isError())
        );
    }

    @Test
    @DisplayName("calculate distance commission for zero distance")
    void zeroDistance() {
        order.setDistance(0);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(0, delivery.getDistanceCommission()),
                () -> Assertions.assertTrue(delivery.isError())
        );
    }

    // DIMENSIONS COMMISSION
    @Test
    @DisplayName("calculate dimensions commission for big cargo")
    void bigCargo() {
        double cargoSide = Math.cbrt(palletVolume);
        cargo = new Cargo(cargoSide + 1, cargoSide, cargoSide, false);
        order = new Order(cargo).setDistance(distance);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(200, delivery.getDimensionsCommission());
    }

    @Test
    @DisplayName("calculate dimensions commission for small cargo")
    void smallCargo() {
        double cargoSide = Math.cbrt(palletVolume);
        cargo = new Cargo(cargoSide - 1, cargoSide, cargoSide, false);
        order = new Order(cargo).setDistance(distance);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(100, delivery.getDimensionsCommission());
    }

    @Test
    @DisplayName("calculate dimensions commission for invalid cargo (zero volume)")
    void zeroVolume() {
        double cargoSide = Math.cbrt(palletVolume);
        cargo = new Cargo(0, 0, 0, false);
        order = new Order(cargo).setDistance(distance);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(0, delivery.getDimensionsCommission()),
                () -> Assertions.assertTrue(delivery.isError())
        );
    }

    // FRAGILITY COMMISSION
    @Test
    @DisplayName("calculate fragility commission for non-fragile cargo")
    void notApplyCommissionForNonFragileCargos() {
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(0, delivery.getFragilityCommission());
    }

    @Test
    @DisplayName("calculate fragility commission for fragile cargo")
    void applyCommissionForFragileCargos() {
        cargo = new Cargo(width, height, depth, true);
        order = new Order(cargo);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(Fragile.FRAGILE.getFragileCommission(), delivery.getFragilityCommission());
    }

    @Test
    @DisplayName("calculate fragility commission for short-distanced fragile cargo")
    void deliverFragileCargosForShortDistance() {
        cargo = new Cargo(width, height, depth, true);
        order = new Order(cargo).setDistance(distanceLong - 1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(Fragile.FRAGILE.getFragileCommission(), delivery.getFragilityCommission());
    }

    @Test
    @DisplayName("calculate fragility commission for long-distanced fragile cargo")
    void notDeliverFragileCargosForLongDistance() {
        cargo = new Cargo(width, height, depth, true);
        order = new Order(cargo).setDistance(distanceLong + 1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertEquals(-1, delivery.getFragilityCommission());
    }

    // WORKLOAD COMMISSION
    @Test
    @DisplayName("calculate workload commission for default workload (regular)")
    void regularWorkloadByDefault() {
        delivery = deliveryService.checkMinimum(false).setServiceCommission(1).calculateOrderDelivery(order);

        Assertions.assertEquals(Workload.REGULAR.getWorkloadMultiplier(), delivery.getDeliveryPrice());
    }

    @Test
    @DisplayName("calculate workload commission for increased workload")
    void increasedWorkload() {
        deliveryService.setWorkload(Workload.INCREASED);
        delivery = deliveryService.checkMinimum(false).setServiceCommission(1).calculateOrderDelivery(order);

        Assertions.assertEquals(Workload.INCREASED.getWorkloadMultiplier(), delivery.getDeliveryPrice());
    }

    @Test
    @DisplayName("calculate workload commission for high workload")
    void highWorkload() {
        deliveryService.setWorkload(Workload.HIGH);
        delivery = deliveryService.checkMinimum(false).setServiceCommission(1).calculateOrderDelivery(order);

        Assertions.assertEquals(Workload.HIGH.getWorkloadMultiplier(), delivery.getDeliveryPrice());
    }

    @Test
    @DisplayName("calculate workload commission for very high workload")
    void veryHighWorkload() {
        deliveryService.setWorkload(Workload.VERY_HIGH);
        delivery = deliveryService.checkMinimum(false).setServiceCommission(1).calculateOrderDelivery(order);

        Assertions.assertEquals(Workload.VERY_HIGH.getWorkloadMultiplier(), delivery.getDeliveryPrice());
    }

    // MINIMUM PRICE
    @Test
    @DisplayName("set minimum price for delivery if the price is less than minimum")
    void setMinPriceForTheDelivery() {
        delivery = deliveryService.setServiceCommission(minPrice - 1).calculateOrderDelivery(order);

        Assertions.assertEquals(minPrice, delivery.getDeliveryPrice());
    }

    @Test
    @DisplayName("set minimum price and take service workload into account")
    void considerServiceWorkloadToSetMinPrice() {
        deliveryService.setWorkload(Workload.INCREASED);
        delivery = deliveryService.setServiceCommission(1).calculateOrderDelivery(order);

        Assertions.assertEquals(minPrice, delivery.getDeliveryPrice());
    }
}