package br.com.jadson.gaugeci.metrics;

import br.com.jadson.gaugeci.model.BuildOfAnalysis;
import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import br.com.jadson.gaugeci.model.StatisticalMeasure;
import br.com.jadson.gaugeci.model.UnitOfTime;
import br.com.jadson.gaugeci.utils.GaugeDateUtils;
import br.com.jadson.gaugeci.utils.GaugeMathUtils;
import br.com.jadson.gaugeci.utils.GaugePeriodOfAnalysisUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Calc the CI sub-practice Time to Fix Broken Build
 *
 * **Time to Fix a Broken Build**: It consists of the median time in a period that builds remained broken.
 * When a build breaks, we compute the time in seconds until the build returns to the "passed" status.
 * If the analysis period ends and the build did not return to the "passed" status, we consider the time since it
 * was broken until the end of the period. When a period has no broken builds, the value would be 0.
 *
 * @author  Jadson Santos - jadsonjs@gmail.com
 */
@Component
public class TimeToFixBrokenBuildProcessor {

    // @Autowired to be used without spring boot
    GaugeDateUtils dateUtils;

    // @Autowired to be used without spring boot
    GaugeMathUtils mathUtils;

    //@Autowired
    GaugePeriodOfAnalysisUtils periodUtils;

    public TimeToFixBrokenBuildProcessor(){
        this.mathUtils = new GaugeMathUtils();
        this.dateUtils = new GaugeDateUtils();
        this.periodUtils = new GaugePeriodOfAnalysisUtils();
    }

    /**
     * Calc history of time to fix CI sub-practice during several period of analysis
     *
     * @param builds
     * @param start
     * @param end
     * @param period
     * @return
     */
    public List<PeriodOfAnalysis> calcTimeToFixBrokenBuildHistory(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period, StatisticalMeasure measure, UnitOfTime unit) {

        List<PeriodOfAnalysis> periodsOfAnalysis = PeriodOfAnalysis.generatePeriodsOfAnalysis(start, end, period);

        List<PeriodOfAnalysis> returnList = new ArrayList<>();
        for (PeriodOfAnalysis periodOfAnalysis : periodsOfAnalysis){
            returnList.add(calcTimeToFixBrokenBuild(builds, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd(), periodOfAnalysis.getPeriod(), measure, unit));
        }

        return returnList;
    }

    public PeriodOfAnalysis calcTimeToFixBrokenBuild(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period, StatisticalMeasure measure, UnitOfTime unit) {

        validateBuild(builds);

        List<BuildOfAnalysis> buildOfPeriod = periodUtils.getBuildOfPeriod(builds, start, end);

        Collections.sort(buildOfPeriod, (o1, o2) -> {
            if(o1.finishedAt == null && o2.finishedAt == null)
                return 0;
            if(o1.finishedAt == null)
                return -1;
            if(o2.finishedAt == null)
                return 1;
            return o1.finishedAt.compareTo(o2.finishedAt);
        });

        List<BigDecimal> timesToFix = new ArrayList<>();

        boolean failed = false;
        LocalDateTime timeOfFailed = null;

        BuildOfAnalysis lastBuildInfo = null;

        for(BuildOfAnalysis buildInfo : buildOfPeriod){

            if( buildInfo.state.equals("failed") && ! failed ){ // failed for first time
                failed = true;
                timeOfFailed = buildInfo.finishedAt;
            }
            if(buildInfo.state.equals("passed") && failed ){ // came back to work
                LocalDateTime fixTime = buildInfo.finishedAt;
                long secondsToFixBuild = timeOfFailed.until( fixTime, ChronoUnit.SECONDS);

                timesToFix.add(dateUtils.scaleTime(secondsToFixBuild, unit));
                failed = false;
            }

            lastBuildInfo = buildInfo;
        }

        if(lastBuildInfo != null) {
            if (lastBuildInfo.state.equals("failed") && failed) { // if not came back to work in the last build
                LocalDateTime fixTime = lastBuildInfo.finishedAt;
                long secondsToFixBuild = timeOfFailed.until(fixTime, ChronoUnit.SECONDS);
                if(secondsToFixBuild > 0)
                    timesToFix.add(dateUtils.scaleTime(secondsToFixBuild, unit));
            }
        }

        return new PeriodOfAnalysis("Time To Fix Broken Build", start, end, period, measure == StatisticalMeasure.MEAN ?  mathUtils.meanOfValues(timesToFix) : mathUtils.medianOfValues(timesToFix));

    }

    private void validateBuild(List<BuildOfAnalysis> builds) {
        for(BuildOfAnalysis buildInfo : builds){
            if(buildInfo.startedAt == null || buildInfo.finishedAt == null){
                throw new IllegalArgumentException(" build \"startedAt\" and \"finishedAt\" field are required. They can not have null values.");
            }

        }
    }


}
