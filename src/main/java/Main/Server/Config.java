package Main.Server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String SETTINGS ="src/main/resources/settings.properties";
    public static int PORT;
    public static String HOST;

    public Config(){

        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader(SETTINGS)) {
            properties.load(fileReader);
            PORT = Integer.parseInt(properties.getProperty("PORT"));
            HOST = properties.getProperty("HOST");

        } catch (FileNotFoundException e) {
            System.out.println("Can't find Settings");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Can't find write/read Settings");
            throw new RuntimeException(e);
        }
    }



}
