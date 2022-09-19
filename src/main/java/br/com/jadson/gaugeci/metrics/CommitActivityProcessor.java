package br.com.jadson.gaugeci.metrics;

import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import br.com.jadson.gaugeci.model.CommitOfAnalysis;
import br.com.jadson.gaugeci.utils.DateUtils;
import br.com.jadson.gaugeci.utils.MathUtils;
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
public class CommitActivityProcessor {


    @Autowired
    DateUtils dateUtils;

    public CommitActivityProcessor(){ }

    public CommitActivityProcessor(DateUtils dateUtils){
        this.dateUtils =dateUtils;
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
    public List<PeriodOfAnalysis> calcCommitsActivity(List<CommitOfAnalysis> commits, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period) {

        List<PeriodOfAnalysis> periodsOfAnalysis = PeriodOfAnalysis.generatePeriodsOfAnalysis(start, end, period);

        List<PeriodOfAnalysis> return_ = new ArrayList<>();
        for (PeriodOfAnalysis periodOfAnalysis : periodsOfAnalysis){
            return_.add(calcCommitsActivity(commits, periodOfAnalysis));
        }

        return return_;
    }


    /**
     * Calc commit activity CI sub-practice for a specific period of time
     * @param commits
     * @param periodOfAnalysis
     * @return
     */
    public PeriodOfAnalysis calcCommitsActivity(List<CommitOfAnalysis> commits, PeriodOfAnalysis periodOfAnalysis) {

        LocalDateTime start = periodOfAnalysis.getStart();
        LocalDateTime end = periodOfAnalysis.getEnd();

        List<CommitOfAnalysis> commitsOfPeriod = getCommitsOfPeriod(commits, start, end);

        LocalDateTime currentReleaseDate = start;

        long qtdDaysWithCommits = 0;

        long qtdTotalDays = start.until(end, ChronoUnit.DAYS) + ( dateUtils.isSameDay(start, end) ? 1 : ( start.getHour() < end.getHour() ? 1 :  2) );  // include the start and end.

        for(int index = 0 ; index < qtdTotalDays ; index ++){

            boolean containsBuild = false;

            buildsFor:
            for(CommitOfAnalysis commit : commitsOfPeriod){

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

        return new PeriodOfAnalysis(start, end, periodOfAnalysis.getPeriod(), new BigDecimal(qtdDaysWithCommits).divide(new BigDecimal(qtdTotalDays), MathUtils.SCALE, RoundingMode.HALF_UP ));

    }

    private List<CommitOfAnalysis> getCommitsOfPeriod(List<CommitOfAnalysis> commits, LocalDateTime startRelease, LocalDateTime endRelease) {

        List<CommitOfAnalysis> commitsOfRelease = new ArrayList<>();

        for (CommitOfAnalysis commit : commits) {

            if(commit.date != null) {

                LocalDateTime commitDate = commit.date;

                if (commitDate.isAfter(startRelease) && commitDate.isBefore(endRelease)) { // this commit if of this period
                    commitsOfRelease.add(commit);
                }
            }
        }

        return commitsOfRelease;
    }

}
