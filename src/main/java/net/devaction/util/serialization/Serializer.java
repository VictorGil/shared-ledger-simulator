package net.devaction.util.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Víctor Gil
 */
public class Serializer{
    private static final Log log = LogFactory.getLog(Serializer.class);
    
    public static byte[] serialize(Object object){
        byte[] bytes = new byte[1];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
          out = new ObjectOutputStream(bos);   
          out.writeObject(object);
          out.flush();
          bytes = bos.toByteArray();
        } catch(IOException ex){
          log.error(ex, ex);
          throw new RuntimeException(ex);
        } finally {
          try{
            if (bos != null)  
                bos.close();
          } catch (IOException ex) {
            log.error(ex, ex);
          }
        }
        return bytes;
    }
}
