package br.com.jadson.gaugeci.metrics;

import br.com.jadson.gaugeci.model.CommitOfAnalysis;
import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import br.com.jadson.gaugeci.utils.GaugeDateUtils;
import br.com.jadson.gaugeci.utils.GaugeMathUtils;
import br.com.jadson.gaugeci.utils.GaugePeriodOfAnalysisUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Calc the CI sub-practice commit Activity
 *
 * **Commit Activity**: It is a unit interval representing the rate of commits across days.
 * If commits were made every day in a period, the value would be 1. If commits were made in half
 * of days the value would be 0.5. If there were no commits in a period the value would be 0.
 *
 * @author  Jadson Santos - jadsonjs@gmail.com
 */
@Component
public class CommitActivityProcessor {


    GaugeDateUtils dateUtils;

    GaugePeriodOfAnalysisUtils periodUtils;

    public CommitActivityProcessor(){
        this.dateUtils = new GaugeDateUtils();
        this.periodUtils = new GaugePeriodOfAnalysisUtils();
    }


    /**
     * Calc history of commit activity CI sub-practice during several period of analysis
     *
     * @param commits
     * @param start
     * @param end
     * @param period
     * @return
     */
    public List<PeriodOfAnalysis> calcCommitsActivityHistory(List<CommitOfAnalysis> commits, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period) {

        List<PeriodOfAnalysis> periodsOfAnalysis = PeriodOfAnalysis.generatePeriodsOfAnalysis(start, end, period);

        List<PeriodOfAnalysis> returnList = new ArrayList<>();
        for (PeriodOfAnalysis periodOfAnalysis : periodsOfAnalysis){
            List<CommitOfAnalysis> commitsOfPeriod = periodUtils.getCommitsOfPeriod(commits, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd());
            returnList.add(calcCommitsActivityOfPeriod(commitsOfPeriod, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd(), periodOfAnalysis.getPeriod()));
        }

        return returnList;
    }


    /**
     * Calc commit activity CI sub-practice for a specific period of time
     * @param commits
     * @param start
     * @param end
     * @return
     */
    private PeriodOfAnalysis calcCommitsActivityOfPeriod(List<CommitOfAnalysis> commits, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period) {
        BigDecimal commitsActivity = calcCommitsActivityValues( commits, start, end);
        return new PeriodOfAnalysis("Commit Activity", start, end, period, commitsActivity);

    }


    /**
     * Calc commit activity CI sub-practice for a specific period of time
     * @param commits
     * @param start
     * @param end
     * @return
     */
    public PeriodOfAnalysis calcCommitsActivity(List<CommitOfAnalysis> commits, LocalDateTime start, LocalDateTime end) {
        BigDecimal commitsActivity = calcCommitsActivityValues( commits, start, end);
        return new PeriodOfAnalysis("Commit Activity", start, end, PeriodOfAnalysis.PERIOD.UNIQUE, commitsActivity);

    }


    /**
     * Calc commit activity CI sub-practice for a specific period of time
     * @param commits
     * @param start
     * @param end
     * @return
     */
    public BigDecimal calcCommitsActivityValues(List<CommitOfAnalysis> commits, LocalDateTime start, LocalDateTime end) {


        LocalDateTime currentReleaseDate = start;

        long qtdDaysWithCommits = 0;

        long qtdTotalDays = PeriodOfAnalysis.daysBetweenDate(start, end);

        for(int index = 0 ; index < qtdTotalDays ; index ++){

            boolean containsBuild = false;

            buildsFor:
            for(CommitOfAnalysis commit : commits){

                if(commit.date != null) {

                    // there is a commit in this day
                    if (currentReleaseDate.getDayOfMonth() == commit.date.getDayOfMonth()
                            && currentReleaseDate.getMonthValue() == commit.date.getMonthValue()
                            && currentReleaseDate.getYear() == commit.date.getYear()) {

                        qtdDaysWithCommits++;
                        break buildsFor;
                    }
                }

            }

            currentReleaseDate = currentReleaseDate.plusDays(1);
        }

        return new BigDecimal(qtdDaysWithCommits).divide(new BigDecimal(qtdTotalDays), GaugeMathUtils.SCALE, RoundingMode.HALF_UP );

    }


}
