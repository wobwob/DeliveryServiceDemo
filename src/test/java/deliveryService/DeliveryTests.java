package deliveryService;

import deliveryService.cargo.Cargo;
import deliveryService.order.Order;
import deliveryService.service.Delivery;
import deliveryService.service.DeliveryService;
import deliveryService.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class DeliveryTests {
    static private final String GENERIC_ERROR = "\nOops! Something went wrong";
    static private final String CONFIG_ERROR = "\nPlease check service configuration file";
    static private final String PROPERTY_FILE = "src/main/java/deliveryService/service/resources/config.properties";

    protected double width;
    protected double height;
    protected double depth;
    protected double distance;
    protected boolean isFragile;
    protected Cargo cargo;
    protected Order order;
    protected Delivery delivery;
    protected DeliveryService deliveryService;

    protected static double palletVolume;
    protected static double distanceLong;
    protected static double minPrice;

    @BeforeAll
    static void beforeAllTests(){
        FileInputStream fis;
        try {
            fis = new FileInputStream(PROPERTY_FILE);

            Properties serviceProperties = new Properties();
            serviceProperties.load(fis);

            palletVolume = Double.parseDouble(serviceProperties.getProperty("PALLET_WIDTH")) *
                    Double.parseDouble(serviceProperties.getProperty("PALLET_HEIGHT")) *
                    Double.parseDouble(serviceProperties.getProperty("PALLET_DEPTH"));
            distanceLong = Integer.parseInt(serviceProperties.getProperty("DISTANCE_LONG"));
            minPrice = Double.parseDouble(serviceProperties.getProperty("MIN_PRICE"));
        } catch (FileNotFoundException e) {
            System.out.println(GENERIC_ERROR + CONFIG_ERROR);
            System.exit(-1);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(GENERIC_ERROR);
            System.exit(-1);
            e.printStackTrace();
        }
    }

    @BeforeEach
    void beforeEachTest() {
        width = TestUtils.generateRandomDoubleValue();
        height = TestUtils.generateRandomDoubleValue();
        depth = TestUtils.generateRandomDoubleValue();
        isFragile = false;
        distance = TestUtils.generateRandomDoubleValue();

        cargo = new Cargo(width, height, depth, isFragile);
        order = new Order(cargo);
        delivery = new Delivery();
        deliveryService = new DeliveryService();
    }
}
