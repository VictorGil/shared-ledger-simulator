package net.devaction.util.format;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Víctor Gil
 */
public class DateUtil{
    public static final String DATE_FORMAT = "EEEE dd-MMM-yyyy HH:mm:ss.SSSZ";
    public static final  SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT,
            new Locale("en"));
    
    //the arg is the number of milliseconds (not seconds) from 1/1/1970 
    //00:00:00 UTC
    //it returns something such as: Sunday 19-Nov-2017 12:13:18.028+0100 (1511089998028)
    public static String getDateString(long javaTime){
        return FORMATTER.format(new Date(javaTime)) + " (" + javaTime + ")";        
    }
}
