package gotogoal.config;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {   
    @Override
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ISO_DATE.format(object);
    }

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
       return LocalDate.parse(text,DateTimeFormatter.ISO_DATE);
    }
}
