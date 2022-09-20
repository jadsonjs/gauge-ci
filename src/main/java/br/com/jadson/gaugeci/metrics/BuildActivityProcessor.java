package br.com.jadson.gaugeci.metrics;

import br.com.jadson.gaugeci.model.BuildOfAnalysis;
import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import br.com.jadson.gaugeci.utils.GaugeDateUtils;
import br.com.jadson.gaugeci.utils.GaugeMathUtils;
import br.com.jadson.gaugeci.utils.GaugePeriodOfAnalysisUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Calc the CI sub-practice Build Activity
 *
 * ***Build Activity***: It is a unit interval (i.e., a closed interval [0,1]) representing the rate of builds across days, i.e,
 * if builds were made every day in the period of analysis, the value would be 1.
 * If builds were made in half of the days, the value would be 0.5. If there were no builds, the value would be 0.
 *
 * @author  Jadson Santos - jadsonjs@gmail.com
 */
@Component
public class BuildActivityProcessor {

    //@Autowired
    GaugeDateUtils dateUtils;

    GaugePeriodOfAnalysisUtils periodUtils;

    public BuildActivityProcessor(){
        this.dateUtils = new GaugeDateUtils();
        this.periodUtils = new GaugePeriodOfAnalysisUtils();
    }

    /**
     * Calc history of build activity CI sub-practice during several period of analysis
     *
     * @param builds
     * @param start
     * @param end
     * @param period
     * @return
     */
    public List<PeriodOfAnalysis> calcBuildActivityHistory(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period) {

        List<PeriodOfAnalysis> periodsOfAnalysis = PeriodOfAnalysis.generatePeriodsOfAnalysis(start, end, period);

        List<PeriodOfAnalysis> returnList = new ArrayList<>();
        for (PeriodOfAnalysis periodOfAnalysis : periodsOfAnalysis){
            returnList.add(calcBuildActivity(builds, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd(), periodOfAnalysis.getPeriod()));
        }

        return returnList;
    }


    /**
     * Calc build activity CI sub-practice for a specific period of time
     * @param builds
     * @param start
     * @param end
     * @return
     */
    public PeriodOfAnalysis calcBuildActivity(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period) {

        List<BuildOfAnalysis> buildsOfPeriod = periodUtils.getBuildOfPeriod(builds, start, end);

        LocalDateTime currentReleaseDate = start;

        long qtdDaysWithBuilds = 0;

        long qtdTotalDays = start.until(end, ChronoUnit.DAYS) + ( dateUtils.isSameDay(start, end) ? 1 : ( start.getHour() < end.getHour() ? 1 :  2) );  // include the start and end.

        for(int index = 0 ; index < qtdTotalDays ; index ++){

            buildsFor:
            for(BuildOfAnalysis build : builds){

                if(build.startedAt != null) {

                    // there is a build in this day
                    if (currentReleaseDate.getDayOfMonth() == build.startedAt.getDayOfMonth()
                            && currentReleaseDate.getMonthValue() == build.startedAt.getMonthValue()
                            && currentReleaseDate.getYear() == build.startedAt.getYear()) {

                        qtdDaysWithBuilds++;
                        break buildsFor;
                    }
                }

            }

            currentReleaseDate = currentReleaseDate.plusDays(1);
        }

        return new PeriodOfAnalysis("Build Activity", start, end, period, new BigDecimal(qtdDaysWithBuilds).divide(new BigDecimal(qtdTotalDays), GaugeMathUtils.SCALE, RoundingMode.HALF_UP ));

    }


}
