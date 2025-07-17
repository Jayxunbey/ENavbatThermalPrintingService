package uz.gbway.enavbatthermalprintingservice.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtil {
    public String epochToRegex(String regex, String date) {

        // Format: dd-MM-yyyy  hh:mm (12-soatlik format, ikki nuqta bilan bo'sh joy bor)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(regex).withZone(ZoneId.systemDefault());
        Instant instant = Instant.ofEpochSecond(Long.parseLong(date));
        return formatter.format(instant);
    }
}
