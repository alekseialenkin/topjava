package ru.javawebinar.topjava.util.formatter;

import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

public class CustomDateFormatter implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(@Nullable String text, @Nullable Locale locale) throws ParseException {
        return LocalDate.parse(text);
    }

    @Override
    public String print(LocalDate object, @Nullable Locale locale) {
        return object.toString();
    }
}
