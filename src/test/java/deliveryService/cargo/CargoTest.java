package deliveryService.cargo;

import deliveryService.DeliveryTests;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Description("Unit tests for Cargo class")
@DisplayName("Cargo should")
class CargoTest extends DeliveryTests {

    @Test
    @DisplayName("return it's width")
    void returnCargoWidth() {
        Assertions.assertEquals(width, cargo.getWidth());
    }

    @Test
    @DisplayName("return it's height")
    void returnCargoHeight() {
        Assertions.assertEquals(height, cargo.getHeight());
    }

    @Test
    @DisplayName("return it's depth")
    void returnCargoDepth() {
        Assertions.assertEquals(depth, cargo.getDepth());
    }

    @Test
    @DisplayName("return it's fragility")
    void returnIsFragile() {
        Assertions.assertEquals(isFragile, cargo.isFragile());
    }

    @Test
    @DisplayName("return it's volume")
    void returnCargoVolume() {
        double volume = width * height * depth;

        Assertions.assertEquals(volume, cargo.getVolume(),
                "Actual result is not as expected, passed values: " +
                        width + " x " + height + " x " + depth);
    }
}