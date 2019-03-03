package main;

import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigPropertyValues {

    InputStream inputStream;

    @Getter @Setter
    public String cache_path = "";
    @Getter @Setter
    public int map_archiveId;
    @Getter @Setter
    public int object_archiveId;

    public  void setPropValues() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream =  new FileInputStream("config.properties");

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            String path = prop.getProperty("cache_path");
            cache_path = path;
            map_archiveId = Integer.parseInt(prop.getProperty("base_map_archive"));
            object_archiveId = Integer.parseInt(prop.getProperty("base_land_archive"));

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }

}
