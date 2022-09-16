package br.com.jadson.gaugeci.metrics;

import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import br.com.jadson.gaugeci.model.CommitMetric;
import br.com.jadson.gaugeci.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
public class CommitActivity {

    private final int scale = 10;

    @Autowired
    DateUtils dateUtils;


    /**
     * Calc history of commit activity CI sub-practice during several period of analysis
     *
     * @param commits
     * @param start
     * @param end
     * @param period
     * @return
     */
    public List<PeriodOfAnalysis> calcCommitsActivity(List<CommitMetric> commits, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period) {

        List<PeriodOfAnalysis> periodsOfAnalysis = PeriodOfAnalysis.generatePeriodsOfAnalysis(start, end, period);

        for (PeriodOfAnalysis periodOfAnalysis : periodsOfAnalysis){
            periodOfAnalysis.setValue(calcCommitsActivity(commits, periodOfAnalysis.getInit(), periodOfAnalysis.getEnd()));
        }

        return periodsOfAnalysis;
    }


    /**
     * Calc commit activity CI sub-practice for a specific period of time
     * @param commits
     * @param start
     * @param end
     * @return
     */
    public BigDecimal calcCommitsActivity(List<CommitMetric> commits, LocalDateTime start, LocalDateTime end) {

        List<CommitMetric> commitsOfPeriod = getCommitsOfPeriod(commits, start, end);

        LocalDateTime currentReleaseDate = start;

        long qtdDaysWithCommits = 0;

        long qtdTotalDays = start.until(end, ChronoUnit.DAYS) + ( dateUtils.isSameDay(start, end) ? 1 : 2 ); // include the start and end.

        for(int index = 0 ; index < qtdTotalDays ; index ++){

            boolean containsBuild = false;

            buildsFor:
            for(CommitMetric commit : commitsOfPeriod){

                if(commit.date != null) {

                    // there is a commit in this day
                    if (currentReleaseDate.getDayOfMonth() == dateUtils.toLocalDateTime(commit.date).getDayOfMonth()
                            && currentReleaseDate.getMonthValue() == dateUtils.toLocalDateTime(commit.date).getMonthValue()
                            && currentReleaseDate.getYear() == dateUtils.toLocalDateTime(commit.date).getYear()) {

                        qtdDaysWithCommits++;
                        break buildsFor;
                    }
                }

            }

            currentReleaseDate = currentReleaseDate.plusDays(1);
        }

        return new BigDecimal(qtdDaysWithCommits).divide(new BigDecimal(qtdTotalDays), scale, RoundingMode.HALF_UP );

    }

    private List<CommitMetric> getCommitsOfPeriod(List<CommitMetric> commits, LocalDateTime startRelease, LocalDateTime endRelease) {

        List<CommitMetric> commitsOfRelease = new ArrayList<>();

        for (CommitMetric commit : commits) {

            if(commit.date != null) {

                LocalDateTime commitDate = dateUtils.toLocalDateTime(commit.date);

                if (commitDate.isAfter(startRelease) && commitDate.isBefore(endRelease)) { // this commit if of this period
                    commitsOfRelease.add(commit);
                }
            }
        }

        return commitsOfRelease;
    }

}
