package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HabrCareerDateTimeParserTest {

    @Test
    public void whenGetLocalDateTimeCorrect() {
        LocalDateTime expected = LocalDateTime.of(2023, 12, 24, 10, 27, 36);
        String text = "2023-12-24T10:27:36+03:00";
        HabrCareerDateTimeParser timeParser = new HabrCareerDateTimeParser();
        LocalDateTime dateTime = timeParser.parse(text);
        assertThat(dateTime).isEqualTo(expected);
    }

    @Test
    public void whenGetLocalDateTimeIncorrect() {
        DateTimeParseException exception = assertThrows(
                DateTimeParseException.class,
                () -> {
                    String text = "2023-12-2410:27:36+03:00";
                    HabrCareerDateTimeParser timeParser = new HabrCareerDateTimeParser();
                    timeParser.parse(text);
                });
        assertThat(exception.getMessage()).isEqualTo("Text '2023-12-2410:27:36+03:00' could not be parsed at index 10");
    }
}