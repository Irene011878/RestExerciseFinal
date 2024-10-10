package Utils;



import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private static final Properties properties = new Properties();

    static {
        try{
            InputStream input = PropertiesReader.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(input);

        }
        catch (Exception e){
            throw new RuntimeException("Properties file not Found");

        }
    }

    public static String getBaseUri(){
        return properties.getProperty("BaseUri");
    }
}
