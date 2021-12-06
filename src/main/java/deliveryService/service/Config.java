package deliveryService.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class Config {
    private static Properties serviceProperties;
    static private final String GENERIC_ERROR = "\nOops! Something went wrong";
    static private final String CONFIG_ERROR = "\nPlease check service configuration file";
    static private final String PROPERTY_FILE = "src/main/java/deliveryService/service/resources/config.properties";

    static Properties load(){
        if (serviceProperties == null) {
            serviceProperties = new Properties();
            FileInputStream fis;
            try {
                fis = new FileInputStream(PROPERTY_FILE);

                serviceProperties = new Properties();
                serviceProperties.load(fis);
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

        return serviceProperties;
    }
}
