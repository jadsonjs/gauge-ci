package br.com.jadson.gaugeci.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/***
 * Keep the calculated value of metric for a specific period of analysis
 */
public class PeriodOfAnalysis {


    public enum PERIOD{UNIQUE, DAY, MONTH, WEEK, YEAR}

    String subPractice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime start;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime end;

    /** Just For debug */
    long daysBetweenDate;

    PERIOD period;

    /**
     * Tee value of CI metric
     */
    BigDecimal value = BigDecimal.ZERO;

    public PeriodOfAnalysis(String subPractice, LocalDateTime start, LocalDateTime end, PERIOD period, BigDecimal value) {

        if(start == null || end == null)
            throw new IllegalArgumentException("Invalid Period Date");

        this.subPractice = subPractice;
        this.start = start;
        this.end = end;
        this.daysBetweenDate = daysBetweenDate(start, end);

        this.period = period;
        this.value = value;
    }


    /**
     * Generate a sequence of periods to calc metrics of CI
     *
     * Generate period in the interval of 1 month
     * generatePeriodsOfAnalysis(init, end, PERIOD.MONTH, 1);
     *
     * @param init
     * @param end
     * @return
     */
    public static List<PeriodOfAnalysis> generatePeriodsOfAnalysis(LocalDateTime init, LocalDateTime end, PERIOD period) {

        List<PeriodOfAnalysis> periods = new ArrayList<>();

        LocalDateTime ptr1 = init.with(LocalTime.of(0, 0, 0));  // MIDNIGHT (00:00)
        LocalDateTime ptr2 = incrementPeriod(period, 1, ptr1);


        while(ptr1.isBefore(end) && ptr2.isBefore(end) ){
            periods.add(new PeriodOfAnalysis("", ptr1, ptr2, period, BigDecimal.ZERO));
            ptr1 = ptr2.plusDays(1).with(LocalTime.of(0, 0, 0));
            ptr2 = incrementPeriod(period, 1, ptr2);
        }

        // add the last period, because the period if not same size.
        if( ! end.equals(ptr1) && ptr1.isBefore(end) ) {
            periods.add(new PeriodOfAnalysis("", ptr1, end.with(LocalTime.of(23, 59, 59)), period, BigDecimal.ZERO));
        }

        return periods;
    }

    private static LocalDateTime incrementPeriod(PERIOD period, int qtd, LocalDateTime ptr) {
        switch (period){
            case YEAR:
                ptr = ptr.plusYears(qtd);
                break;
            case MONTH:
                ptr = ptr.plusMonths(qtd);
                break;
            case WEEK:
                ptr = ptr.plusWeeks(qtd);
                break;
            case DAY:
                ptr = ptr.plusDays(qtd);
                break;
        }
        return ptr.with(LocalTime.of(23, 59, 59));
    }

    public static long daysBetweenDate(LocalDateTime start, LocalDateTime end){
        return start.until(end, ChronoUnit.DAYS) + ( isSameDay(start, end) ? 1 : ( start.getHour() < end.getHour() ? 1 :  2) );  // include the start and end.
    }

    private static boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return formatter.format(date1).equals(formatter.format(date2));
    }

    public String getSubPractice() { return subPractice; }

    public long getDaysBetweenDate() { return daysBetweenDate; }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public PERIOD getPeriod() { return period; }

    public BigDecimal setValue(BigDecimal value) {
        if(value != null) {
            this.value = value;
        }
        return value;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return period+" [ " + "start= " + start + " end= " + end + " ] value: " +value+ " qty days: "+daysBetweenDate;
    }
}
