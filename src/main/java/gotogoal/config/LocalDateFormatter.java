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

//    public static final String PL_PATTERN = "yyyy-MM-dd";
//    public static final String NORMAL_PATTERN = "yyyy-MM-dd";
//    @Override public LocalDate parse(String text, Locale locale) throws ParseException {
//        return LocalDate.parse(text, DateTimeFormatter.ofPattern(getPattern(locale)));
//    }
//
//    @Override public String print(LocalDate object, Locale locale) {
//        return DateTimeFormatter.ofPattern(getPattern(locale)).format(object);
//    }
//
//    public static String getPattern(Locale locale) {
//        return isUnitedStates(locale) ? PL_PATTERN : NORMAL_PATTERN;
//    }
//
//    private static boolean isUnitedStates(Locale locale) {
//        return Locale.US.getCountry().equals(locale.getCountry());
//    }