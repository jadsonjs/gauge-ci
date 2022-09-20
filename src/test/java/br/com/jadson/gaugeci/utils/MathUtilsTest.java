package br.com.jadson.gaugeci.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class MathUtilsTest {

    @Test
    void sumOfValues() {
        GaugeMathUtils utils = new GaugeMathUtils();
        Assertions.assertEquals(BigDecimal.ZERO, utils.sumOfValues(new ArrayList<>(){}));
    }

    @Test
    void sumOfValues2() {
        GaugeMathUtils utils = new GaugeMathUtils();
        Assertions.assertEquals(new BigDecimal("66.94"), utils.sumOfValues(List.of(new BigDecimal("10.50"), new BigDecimal("55.40"), new BigDecimal("1.04") )));
    }

    @Test
    void meanOfValues() {
        GaugeMathUtils utils = new GaugeMathUtils();
        Assertions.assertEquals(new BigDecimal("6.7800"), utils.meanOfValues(
                List.of(new BigDecimal("6.5"),
                        new BigDecimal("5.0"),
                        new BigDecimal("9.4"),
                        new BigDecimal("8.2"),
                        new BigDecimal("4.8"))));
    }

    @Test
    void medianOfValues() {
        GaugeMathUtils utils = new GaugeMathUtils();
        Assertions.assertEquals(new BigDecimal("6.5"), utils.medianOfValues(
                List.of(new BigDecimal("6.5"),
                        new BigDecimal("5.0"),
                        new BigDecimal("9.4"),
                        new BigDecimal("8.2"),
                        new BigDecimal("4.8"))));
    }
}