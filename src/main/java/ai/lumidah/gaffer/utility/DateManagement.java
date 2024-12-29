package ai.lumidah.gaffer.utility;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    public String epochToDateTimeHHMM(long epochTime) {
        ZoneId singaporeZone = ZoneId.of("Asia/Singapore");
        Instant instant = Instant.ofEpochMilli(epochTime * 1000);
        ZonedDateTime singaporeTime = instant.atZone(singaporeZone);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return singaporeTime.format(formatter);
    }
    
}
