package deliveryService.service;

import deliveryService.DeliveryTests;
import deliveryService.utils.TestUtils;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Random;

@Description("Unit tests for Delivery class")
@DisplayName("Delivery should")
class DeliveryTest extends DeliveryTests {

    @BeforeEach
    void setUp() {
        order.setDistance(distance);
        delivery.setOrder(order);
    }

    @Test
    @DisplayName("return order")
    void beAbleToReturnOrder() {
        Assertions.assertNotNull(delivery.getOrder());
    }

    @Test
    @DisplayName("return null if order is not set")
    void returnNullIfOrderIsNotSet() {
        delivery = new Delivery();

        Assertions.assertNull(delivery.getOrder());
    }

    @Test
    @DisplayName("return no price if order is not calculated")
    void returnZeroPriceIfDeliveryIsNotCalculated() {
        Assertions.assertEquals(-1, delivery.getDeliveryPrice());
    }

    @Test
    @DisplayName("not set error if order is not calculated")
    void returnNoErrorMessageIfDeliveryIsNotCalculated() {
        Assertions.assertNull(delivery.getErrorMessage());
    }

    @Test
    @DisplayName("not set info if order is not calculated")
    void returnNoInfoIfDeliveryIsNotCalculated() {
        Assertions.assertNull(delivery.getInfo());
    }

    @Test
    @DisplayName("return no distance commission if order is not calculated")
    void returnNoDistanceCommissionIfDeliveryIsNotCalculated() {
        Assertions.assertEquals(0, delivery.getDistanceCommission());
    }

    @Test
    @DisplayName("return no dimensions commission if order is not calculated")
    void returnNoDimensionsCommissionIfDeliveryIsNotCalculated() {
        Assertions.assertEquals(0, delivery.getDimensionsCommission());
    }

    @Test
    @DisplayName("return no fragility commission if order is not calculated")
    void returnNoFragilityCommissionIfDeliveryIsNotCalculated() {
        Assertions.assertEquals(0, delivery.getFragilityCommission());
    }

    @Test
    @DisplayName("apply and return distance commission")
    void beAbleToHandleDistanceCommission() {
        double distanceCommission = TestUtils.generateRandomDoubleValue();
        delivery.setDistanceCommission(distanceCommission);

        Assertions.assertEquals(distanceCommission, delivery.getDistanceCommission());
    }

    @Test
    @DisplayName("apply and return dimensions commission")
    void beAbleToHandleDimensionsCommission() {
        double dimensionsCommission = TestUtils.generateRandomDoubleValue();
        delivery.setDimensionsCommission(dimensionsCommission);

        Assertions.assertEquals(dimensionsCommission, delivery.getDimensionsCommission());
    }

    @Test
    @DisplayName("apply and return fragility commission")
    void beAbleToHandleFragilityCommission() {
        double fragilityCommission = TestUtils.generateRandomDoubleValue();
        delivery.setFragilityCommission(fragilityCommission);

        Assertions.assertEquals(fragilityCommission, delivery.getFragilityCommission());
    }

    @Test
    @DisplayName("apply and return isMinimum flag")
    void beAbleToHandleIsMinimumPayment() {
        boolean isMinimumPayment = TestUtils.generateRandomBooleanValue();
        delivery.setIsMinimumPayment(isMinimumPayment);

        Assertions.assertEquals(isMinimumPayment, delivery.isMinimumPayment());
    }

    @Test
    @DisplayName("apply and return error flag")
    void beAbleToHandleIsError() {
        boolean isError = TestUtils.generateRandomBooleanValue();
        delivery.setIsError(isError);

        Assertions.assertEquals(isError, delivery.isError());
    }

    @Test
    @DisplayName("apply and return service fee")
    void beAbleToHandleServiceFee() {
        double serviceFee = TestUtils.generateRandomDoubleValue();
        delivery.setServiceFee(serviceFee);

        Assertions.assertEquals(serviceFee, delivery.getServiceFee());
    }

    @Test
    @DisplayName("apply and return delivery price")
    void beAbleToHandleDeliveryPrice() {
        double deliveryPrice = TestUtils.generateRandomDoubleValue();
        delivery.setDeliveryPrice(deliveryPrice);

        Assertions.assertEquals(deliveryPrice, delivery.getDeliveryPrice());
    }

    @Test
    @DisplayName("apply and return error message")
    void beAbleToHandleErrorMessage() {
        byte[] array = new byte[TestUtils.generateRandomIntegerValue()];
        new Random().nextBytes(array);
        String randomString = new String(array, StandardCharsets.UTF_8);
        delivery.setErrorMessage(randomString);

        Assertions.assertEquals(randomString, delivery.getErrorMessage());
    }

    @Test
    @DisplayName("apply and return info")
    void beAbleToHandleInfo() {
        byte[] array = new byte[TestUtils.generateRandomIntegerValue()];
        new Random().nextBytes(array);
        String randomString = new String(array, StandardCharsets.UTF_8);
        delivery.setInfo(randomString);

        Assertions.assertEquals(randomString, delivery.getInfo());
    }
}