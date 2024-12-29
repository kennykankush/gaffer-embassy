package ai.lumidah.gaffer.utility;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateManagement {

    public static String epochToDateTime(long epoch){ 

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd MMM h:mma")
        .withZone(ZoneId.systemDefault());

        String formattedDate = formatter.format(Instant.ofEpochSecond(epoch));

        return formattedDate.replace("AM", "am").replace("PM", "pm");

    }

    public static String epochToDateTimeHHMM(long epoch) { 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault());
                
        return formatter.format(Instant.ofEpochSecond(epoch));
    }
    
}
