package net.devaction.util.serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Víctor Gil
 */
public class Deserializer{
    private static final Log log = LogFactory.getLog(Deserializer.class);
    
    @SuppressWarnings("unchecked")
    public static <T> T createObject(byte[] bytes, Class<T> clazz){
        T type = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
          in = new ObjectInputStream(bis);  
          type = (T) in.readObject();
        } catch(IOException | ClassNotFoundException ex){
          log.error(ex, ex);
          throw new RuntimeException(ex);
        } finally {
          try{
            if (in != null)
                in.close();
          } catch (IOException ex) {
            log.error(ex, ex);
          }
        }
        return type;
    }
}
