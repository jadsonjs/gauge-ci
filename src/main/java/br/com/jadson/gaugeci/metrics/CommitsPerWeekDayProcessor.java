package br.com.jadson.gaugeci.metrics;

import br.com.jadson.gaugeci.model.CommitOfAnalysis;
import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import br.com.jadson.gaugeci.utils.GaugeDateUtils;
import br.com.jadson.gaugeci.utils.GaugeMathUtils;
import br.com.jadson.gaugeci.utils.GaugePeriodOfAnalysisUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CI Theater paper
 *
 * W. Felidré, L. Furtado, D. A. d. Costa, B. Cartaxo and G. Pinto,
 * "Continuous Integration Theater," 2019 ACM/IEEE International Symposium on Empirical Software Engineering and
 * Measurement (ESEM), 2019, pp. 1-10, doi: 10.1109/ESEM.2019.8870152.
 */
@Component
public class CommitsPerWeekDayProcessor {

    GaugeDateUtils dateUtils;

    GaugeMathUtils mathUtils;

    GaugePeriodOfAnalysisUtils periodUtils;

    public CommitsPerWeekDayProcessor(){
        this.mathUtils = new GaugeMathUtils();
        this.dateUtils = new GaugeDateUtils();
        this.periodUtils = new GaugePeriodOfAnalysisUtils();
    }


    /**
     * Calc history of commit per weekday CI sub-practice during several period of analysis
     *
     * @param commits
     * @param start
     * @param end
     * @param period
     * @return
     */
    public List<PeriodOfAnalysis> calcCommitsPerWeekDayHistory(List<CommitOfAnalysis> commits, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period) {

        List<PeriodOfAnalysis> periodsOfAnalysis = PeriodOfAnalysis.generatePeriodsOfAnalysis(start, end, period);

        List<PeriodOfAnalysis> returnList = new ArrayList<>();
        for (PeriodOfAnalysis periodOfAnalysis : periodsOfAnalysis){
            List<CommitOfAnalysis> commitsOfPeriod = periodUtils.getCommitsOfPeriod(commits, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd());
            returnList.add(calcCommitsPerWeekDayOfPeriod(commitsOfPeriod, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd(), periodOfAnalysis.getPeriod()));
        }

        return returnList;
    }


    private PeriodOfAnalysis calcCommitsPerWeekDayOfPeriod(List<CommitOfAnalysis> commits, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period) {
        List<BigDecimal> commitsPerWeekDay = calcCommitsPerWeekDayValues(commits, start, end);
        // OBS.: here not work median, because we can have a lot of day with zero commits, that generate commitPerDay = 0 and is not thue
        return new PeriodOfAnalysis("Commits Per Weekday", start, end, period, mathUtils.meanOfValues(commitsPerWeekDay) );

    }

    /**
     * Calc commit per week day CI sub-practice for a specific period of time
     * @param commits
     * @param start
     * @param end
     * @return
     */
    public PeriodOfAnalysis calcCommitsPerWeekDay(List<CommitOfAnalysis> commits, LocalDateTime start, LocalDateTime end) {

        List<BigDecimal> commitsPerWeekDay = calcCommitsPerWeekDayValues(commits, start, end);
        // OBS.: here not work median, because we can have a lot of day with zero commits, that generate commitPerDay = 0 and is not thue
        return new PeriodOfAnalysis("Commits Per Weekday", start, end, PeriodOfAnalysis.PERIOD.UNIQUE, mathUtils.meanOfValues(commitsPerWeekDay));

    }


    /**
     * Calc commit per week day CI sub-practice for a specific period of time
     * @param commits
     * @param start
     * @param end
     * @return
     */
    public List<BigDecimal> calcCommitsPerWeekDayValues(List<CommitOfAnalysis> commits, LocalDateTime start, LocalDateTime end) {



        LocalDateTime currentReleaseDate = start;

        long qtdTotalDays = PeriodOfAnalysis.daysBetweenDate(start, end);

        Long[] commitsPerDayList = new Long[(int) qtdTotalDays];

        // init the array
        for (int i = 0; i < commitsPerDayList.length ; i++) {
            commitsPerDayList[i] = 0l;
        }

        for(int index = 0 ; index < qtdTotalDays ; index ++){

            for(CommitOfAnalysis commit : commits){

                if(commit.date != null) {

                    // there is a commit in this day
                    if (currentReleaseDate.getDayOfMonth() == commit.date.getDayOfMonth()
                            && currentReleaseDate.getMonthValue() == commit.date.getMonthValue()
                            && currentReleaseDate.getYear() == commit.date.getYear()) {

                        commitsPerDayList[index] = commitsPerDayList[index]+1;
                    }
                }

            }

            currentReleaseDate = currentReleaseDate.plusDays(1);
        }

        List<BigDecimal> metricList = new ArrayList<>();
        for (Long time : commitsPerDayList){
            metricList.add(new BigDecimal(time));
        }

        // here not work median, because we can have a lot of day with zero commits, that generate commitPerDay = 0 and is not thue
        return metricList;

    }


}
