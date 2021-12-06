package deliveryService.e2e;

import deliveryService.DeliveryTests;
import deliveryService.cargo.Cargo;
import deliveryService.order.Order;
import deliveryService.service.*;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Description("Full circuit tests to check service as a whole")
@DisplayName("User should")
public class DeliveryServiceE2ETests extends DeliveryTests {

    @BeforeEach
    void beforeEachE2ETest() {
//        E2E-specific setup
    }

    @Test
    @DisplayName("get a price for a valid order")
    void serviceShouldBeAbleToCalculateDelivery() {
        cargo = new Cargo(width, height, depth, false);
        order = new Order(cargo).setDistance(distance);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertTrue(delivery.getDeliveryPrice() > 0),
                () -> Assertions.assertFalse(delivery.isError())
        );
    }

    @Test
    @DisplayName("get an error if distance is invalid (not set)")
    void serviceShouldReturnErrorIfDistanceIsNotSet() {
        cargo = new Cargo(width, height, depth, false);
        order = new Order(cargo);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(-1, delivery.getDeliveryPrice()),
                () -> Assertions.assertTrue(delivery.isError()),
                () -> Assertions.assertTrue(delivery.getErrorMessage().length() > 0)
        );
    }

    @Test
    @DisplayName("get an error if distance is invalid (negative)")
    void serviceShouldReturnErrorIfDistanceIsNegative() {
        cargo = new Cargo(width, height, depth, false);
        order = new Order(cargo).setDistance(-1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(-1, delivery.getDeliveryPrice()),
                () -> Assertions.assertTrue(delivery.isError()),
                () -> Assertions.assertTrue(delivery.getErrorMessage().length() > 0)
        );
    }

    @Test
    @DisplayName("get an error if distance is invalid (zero)")
    void serviceShouldReturnErrorIfDistanceIsZero() {
        cargo = new Cargo(width, height, depth, false);
        order = new Order(cargo).setDistance(0);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(-1, delivery.getDeliveryPrice()),
                () -> Assertions.assertTrue(delivery.isError()),
                () -> Assertions.assertTrue(delivery.getErrorMessage().length() > 0)
        );
    }

    @Test
    @DisplayName("get an error if cargo volume is invalid (zero)")
    void serviceShouldReturnErrorIfCargoVolumeIsNegative() {
        cargo = new Cargo(-10, height, depth, false);
        order = new Order(cargo).setDistance(distance);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(-1, delivery.getDeliveryPrice()),
                () -> Assertions.assertTrue(delivery.isError()),
                () -> Assertions.assertTrue(delivery.getErrorMessage().length() > 0)
        );
    }

    @Test
    @DisplayName("get an error for long-distanced fragile order")
    void serviceShouldReturnErrorForFragileOrderAndLongDistance() {
        cargo = new Cargo(width, height, depth, true);
        order = new Order(cargo).setDistance(distanceLong + 1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(-1, delivery.getDeliveryPrice()),
                () -> Assertions.assertTrue(delivery.isError()),
                () -> Assertions.assertTrue(delivery.getErrorMessage().length() > 0)
        );
    }

    @Test
    @DisplayName("get the minimum delivery price for an order")
    void serviceShouldSetMinPrice() {
        cargo = new Cargo(0.1, 0.1, 0.1, false);
        order = new Order(cargo).setDistance(0.1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(minPrice, delivery.getDeliveryPrice()),
                () -> Assertions.assertFalse(delivery.isError())
        );
    }

    @Test
    @DisplayName("get distance commission for an order")
    void serviceShouldSetDistanceCommission() {
        Distance testDistance = Distance.DISTANCE_SHORT;

        cargo = new Cargo(width, height, depth, false);
        order = new Order(cargo).setDistance(testDistance.getValue());
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(testDistance.getDistanceCommission(), delivery.getDistanceCommission()),
                () -> Assertions.assertTrue(calculateCommissionsTotal(delivery) >= testDistance.getDistanceCommission()),
                () -> Assertions.assertTrue(delivery.getDeliveryPrice() >= testDistance.getDistanceCommission()),
                () -> Assertions.assertFalse(delivery.isError())
        );
    }

    @Test
    @DisplayName("get dimensions commission for an order")
    void serviceShouldSetDimensionsCommission() {
        Dimensions testDimensions = Dimensions.DIMENSIONS_BIG;
        double testCargoSize = Math.cbrt(palletVolume);

        cargo = new Cargo(testCargoSize + 1, testCargoSize, testCargoSize, false);
        order = new Order(cargo).setDistance(distance);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(testDimensions.getDimensionsCommission(), delivery.getDimensionsCommission()),
                () -> Assertions.assertTrue(calculateCommissionsTotal(delivery) >= testDimensions.getDimensionsCommission()),
                () -> Assertions.assertTrue(delivery.getDeliveryPrice() >= testDimensions.getDimensionsCommission()),
                () -> Assertions.assertFalse(delivery.isError())
        );
    }

    @Test
    @DisplayName("get fragility commission for an order")
    void serviceShouldSetFragilityCommission() {
        Fragile testFragile = Fragile.FRAGILE;

        cargo = new Cargo(width, height, depth, true);
        order = new Order(cargo).setDistance(distanceLong - 1);
        delivery = deliveryService.calculateOrderDelivery(order);

        Assertions.assertAll(
                () -> Assertions.assertEquals(testFragile.getFragileCommission(), delivery.getFragilityCommission()),
                () -> Assertions.assertTrue(calculateCommissionsTotal(delivery) >= testFragile.getFragileCommission()),
                () -> Assertions.assertTrue(delivery.getDeliveryPrice() >= testFragile.getFragileCommission()),
                () -> Assertions.assertFalse(delivery.isError())
        );
    }

    @Test
    @DisplayName("get workload commission for an order")
    void serviceShouldSetWorkloadCommission() {
        Workload testWorkload = Workload.VERY_HIGH;
        deliveryService.setWorkload(testWorkload);

        cargo = new Cargo(width, height, depth, false);
        order = new Order(cargo).setDistance(distance);
        delivery = deliveryService.calculateOrderDelivery(order);

        double commissionsByWorkload = calculateCommissionsTotal(delivery) * testWorkload.getWorkloadMultiplier();

        Assertions.assertAll(
                () -> Assertions.assertEquals(commissionsByWorkload, delivery.getDeliveryPrice()),
                () -> Assertions.assertFalse(delivery.isError())
        );
    }

    double calculateCommissionsTotal(Delivery activeDelivery) {
        return activeDelivery.getDistanceCommission() +
                activeDelivery.getDimensionsCommission() +
                activeDelivery.getFragilityCommission();
    }
}
