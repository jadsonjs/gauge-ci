package br.com.jadson.gaugeci.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class PeriodOfCommitsAnalysisInputDataHistoryTest {

    @Test
    void generatePeriodsOfAnalysis() {

        List<PeriodOfAnalysis> periods = PeriodOfAnalysis.generatePeriodsOfAnalysis(
                LocalDateTime.of(2017, 5, 25, 14, 4, 39),
                LocalDateTime.of(2021, 1, 16, 15, 10, 32), PeriodOfAnalysis.PERIOD.MONTH);

        Assertions.assertEquals(44, periods.size());
        Assertions.assertEquals(LocalDateTime.of(2017, 5, 25, 0, 0, 0), periods.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2021, 1, 16, 23, 59, 59), periods.get(periods.size()-1).getEnd());
    }

    /**
     * "2017-05-25 14:04:39"
     *
     * "2021-01-16 15:10:32"
     */
    @Test
    void generatePeriodDates2() {

        List<PeriodOfAnalysis> periods = PeriodOfAnalysis.generatePeriodsOfAnalysis(
                LocalDateTime.of(2017, 5, 25, 14, 4, 39),
                LocalDateTime.of(2021, 1, 16, 15, 10, 32), PeriodOfAnalysis.PERIOD.MONTH);

        for (PeriodOfAnalysis p : periods){
            System.out.println(p);
        }

        Assertions.assertEquals(44, periods.size());
        Assertions.assertEquals(LocalDateTime.of(2017, 5, 25, 0, 0, 0), periods.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2017, 6, 25, 23, 59, 59), periods.get(0).getEnd());

        Assertions.assertEquals(LocalDateTime.of(2020, 12, 26, 0, 0, 0), periods.get(periods.size()-1).getStart());
        Assertions.assertEquals(LocalDateTime.of(2021, 1, 16, 23, 59, 59), periods.get(periods.size()-1).getEnd());
    }

    @Test
    void generatePeriodDatesSameDate() {

        List<PeriodOfAnalysis> periods = PeriodOfAnalysis.generatePeriodsOfAnalysis(
                LocalDateTime.of(2017, 5, 25, 14, 4, 39),
                LocalDateTime.of(2017, 5, 25, 14, 4, 39), PeriodOfAnalysis.PERIOD.MONTH );

        Assertions.assertEquals(1, periods.size());

    }

    @Test
    void generatePeriodDatesOneDay() {

        List<PeriodOfAnalysis> periods = PeriodOfAnalysis.generatePeriodsOfAnalysis(
                LocalDateTime.of(2017, 5, 25, 14, 4, 39),
                LocalDateTime.of(2017, 5, 26, 14, 4, 39), PeriodOfAnalysis.PERIOD.MONTH);

        Assertions.assertEquals(1, periods.size());

        Assertions.assertEquals(LocalDateTime.of(2017, 5, 25, 0, 0, 0), periods.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2017, 5, 26, 23, 59, 59), periods.get(periods.size()-1).getEnd());

    }


    /**
     * "2021-01-16 14:04:39"
     *
     * "2021-03-16 15:10:32"
     */
    @Test
    void generatePeriodDatesWeeks() {

        List<PeriodOfAnalysis> periods = PeriodOfAnalysis.generatePeriodsOfAnalysis(
                LocalDateTime.of(2021, 1, 16, 14, 4, 39),
                LocalDateTime.of(2021, 3, 16, 15, 10, 32), PeriodOfAnalysis.PERIOD.WEEK );

        for (PeriodOfAnalysis p : periods){
            System.out.println(p);
        }

        Assertions.assertEquals(9, periods.size());
        Assertions.assertEquals(LocalDateTime.of(2021, 1, 16, 0, 0, 0), periods.get(0).getStart());
        Assertions.assertEquals(LocalDateTime.of(2021, 1, 23, 23, 59, 59), periods.get(0).getEnd());

        Assertions.assertEquals(LocalDateTime.of(2021, 3, 14, 0, 0, 0), periods.get(periods.size()-1).getStart());
        Assertions.assertEquals(LocalDateTime.of(2021, 3, 16, 23, 59, 59), periods.get(periods.size()-1).getEnd());
    }
}