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

    private String buildSuccessStatus = "passed";
    private String buildsFailedStatus = "failed";


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
     * Constructor to define labels of success and failed.
     *
     * @param successStatusLabel
     * @param failedStatusLabel
     */
    public TimeToFixBrokenBuildProcessor(String successStatusLabel, String failedStatusLabel){
        this();
        // pre condition
        if(successStatusLabel == null || failedStatusLabel == null)
            throw new IllegalArgumentException("build labels can not be null");

        this.buildSuccessStatus = successStatusLabel;
        this.buildsFailedStatus = failedStatusLabel;
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
            List<BuildOfAnalysis> buildOfPeriod = periodUtils.getBuildOfPeriod(builds, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd());
            returnList.add(calcTimeToFixBrokenBuildOfPeriod(buildOfPeriod, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd(), periodOfAnalysis.getPeriod(), measure, unit));
        }

        return returnList;
    }

    /**
     *
     * @param builds
     * @param start
     * @param end
     * @param period
     * @param measure
     * @param unit
     * @return
     */
    private PeriodOfAnalysis calcTimeToFixBrokenBuildOfPeriod(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period, StatisticalMeasure measure, UnitOfTime unit) {
        List<BigDecimal> timesToFix = calcTimeToFixBrokenBuildValues(builds, unit);
        return new PeriodOfAnalysis("Time To Fix Broken Build", start, end, period, measure == StatisticalMeasure.MEAN ?  mathUtils.meanOfValues(timesToFix) : mathUtils.medianOfValues(timesToFix));
    }

    //////////////////////////////////////////////////////////////////////////

    /**
     * Calcule the value for all periods
     * @param builds
     * @param measure
     * @param unit
     * @return
     */
    public PeriodOfAnalysis calcTimeToFixBrokenBuild(List<BuildOfAnalysis> builds, StatisticalMeasure measure, UnitOfTime unit) {

        orderBuilds(builds);

        List<BigDecimal> timesToFix = calcTimeToFixBrokenBuildValues(builds, unit);
        return new PeriodOfAnalysis("Time To Fix Broken Build", builds.get(0).startedAt, builds.get(builds.size()-1).finishedAt, PeriodOfAnalysis.PERIOD.UNIQUE, measure == StatisticalMeasure.MEAN ?  mathUtils.meanOfValues(timesToFix) : mathUtils.medianOfValues(timesToFix));

    }


    /**
     * Return a list of sub-pratice values.
     * @param builds
     * @param unit
     * @return
     */
    public List<BigDecimal> calcTimeToFixBrokenBuildValues(List<BuildOfAnalysis> builds, UnitOfTime unit) {

        validateBuild(builds);

        orderBuilds(builds);

        List<BigDecimal> timesToFix = new ArrayList<>();

        boolean failed = false;
        LocalDateTime timeOfFailed = null;

        BuildOfAnalysis lastBuildInfo = null;

        for(BuildOfAnalysis buildInfo : builds){

            if( buildInfo.state.equals(this.buildsFailedStatus) && ! failed ){ // failed for first time
                failed = true;
                timeOfFailed = buildInfo.finishedAt;
            }
            if(buildInfo.state.equals(buildSuccessStatus) && failed ){ // came back to work
                LocalDateTime fixTime = buildInfo.finishedAt;
                long secondsToFixBuild = timeOfFailed.until( fixTime, ChronoUnit.SECONDS);

                timesToFix.add(dateUtils.scaleTime(secondsToFixBuild, unit));
                failed = false;
            }

            lastBuildInfo = buildInfo;
        }

        if(lastBuildInfo != null) {
            if (lastBuildInfo.state.equals(this.buildsFailedStatus) && failed) { // if not came back to work in the last build
                LocalDateTime fixTime = lastBuildInfo.finishedAt;
                long secondsToFixBuild = timeOfFailed.until(fixTime, ChronoUnit.SECONDS);
                if(secondsToFixBuild > 0)
                    timesToFix.add(dateUtils.scaleTime(secondsToFixBuild, unit));
            }
        }

        return timesToFix;

    }

    private void orderBuilds(List<BuildOfAnalysis> builds) {
        Collections.sort(builds, (o1, o2) -> {
            if (o1.finishedAt == null && o2.finishedAt == null)
                return 0;
            if (o1.finishedAt == null)
                return -1;
            if (o2.finishedAt == null)
                return 1;
            return o1.finishedAt.compareTo(o2.finishedAt);
        });
    }

    private void validateBuild(List<BuildOfAnalysis> builds) {
        for(BuildOfAnalysis buildInfo : builds){
            if(buildInfo.startedAt == null || buildInfo.finishedAt == null){
                throw new IllegalArgumentException(" build \"startedAt\" and \"finishedAt\" field are required. They can not have null values.");
            }

        }
    }


}
