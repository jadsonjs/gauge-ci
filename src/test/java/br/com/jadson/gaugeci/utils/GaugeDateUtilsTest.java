package br.com.jadson.gaugeci.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

class GaugeDateUtilsTest {

    @Test
    void toLocalDateTime() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String dateInString = "16/09/2022 3:10:45";
        Date date = sdf.parse(dateInString);

        Assertions.assertEquals(
                LocalDateTime.of(2022, 9, 16, 3, 10, 45), new GaugeDateUtils().toLocalDateTime( date ) );
    }

    @Test
    void isSameDay() {
        Assertions.assertTrue(new GaugeDateUtils().isSameDay(
                LocalDateTime.of(2022, 9, 16, 3, 10, 45),
                LocalDateTime.of(2022, 9, 16, 23, 45, 29)));
    }

    @Test
    void isNotSameDay() {
        Assertions.assertFalse(new GaugeDateUtils().isSameDay(
                LocalDateTime.of(2022, 9, 16, 3, 10, 45),
                LocalDateTime.of(2022, 9, 15, 23, 45, 29)) );
    }
}