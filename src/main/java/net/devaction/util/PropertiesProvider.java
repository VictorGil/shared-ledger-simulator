package net.devaction.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Víctor Gil
 */
public class PropertiesProvider{
    private static final Log log = LogFactory.getLog(PropertiesProvider.class);
    
    private static final String FILE_NAME = "application.properties";
    private static final Properties APP_PROPERTIES = loadProperties();
    
    private static Properties loadProperties(){
        InputStream inputStream = PropertiesProvider.class.getClassLoader().getResourceAsStream(FILE_NAME);
        Properties appProperties = new Properties();
        try{
            appProperties.load(inputStream);
        }catch(IOException ex){
            log.fatal(ex, ex);
            throw new RuntimeException(ex);
        }finally{
            if (inputStream != null){
                try{
                    inputStream.close();
                } catch(IOException ex){
                    log.error(ex, ex);
                }
            }
        }
        return appProperties;
    }
    
    public static String get(String propertyName){
        return APP_PROPERTIES.getProperty(propertyName);
    }
}
