package deliveryService.order;

import deliveryService.DeliveryTests;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Description("Unit tests for Order class")
@DisplayName("Order should")
class OrderTest extends DeliveryTests {

    @Test
    @DisplayName("return zero distance if it is not set")
    void returnZeroIfDistanceIsNotSet() {
        Assertions.assertEquals(0, order.getDistance());
    }

    @Test
    @DisplayName("assign and return distance")
    void beAbleToHandleDistance() {
        order.setDistance(distance);

        Assertions.assertEquals(distance, order.getDistance());
    }

    @Test
    @DisplayName("return cargo")
    void returnCargo() {
        Assertions.assertEquals(cargo, order.getCargo());
    }
}