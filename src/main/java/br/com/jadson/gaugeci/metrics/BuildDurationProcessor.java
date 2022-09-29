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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Calc the CI sub-practice Build Duration
 *
 * ***Build Duration***: It measures the duration of the build (build finished at timestamp - build started at timestamp).
 *
 * @author  Jadson Santos - jadsonjs@gmail.com
 */
@Component
public class BuildDurationProcessor {

    //@Autowired
    GaugeDateUtils dateUtils;

    //@Autowired
    GaugeMathUtils mathUtils;

    //@Autowired
    GaugePeriodOfAnalysisUtils periodUtils;

    public BuildDurationProcessor(){
        this.mathUtils = new GaugeMathUtils();
        this.dateUtils = new GaugeDateUtils();
        this.periodUtils = new GaugePeriodOfAnalysisUtils();
    }

    /**
     * Calc history of build duration CI sub-practice during several period of analysis
     *
     * @param builds
     * @param start
     * @param end
     * @param period
     * @return
     */
    public List<PeriodOfAnalysis> calcBuildDurationHistory(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period, StatisticalMeasure measure, UnitOfTime unit) {

        List<PeriodOfAnalysis> periodsOfAnalysis = PeriodOfAnalysis.generatePeriodsOfAnalysis(start, end, period);

        List<PeriodOfAnalysis> returnList = new ArrayList<>();
        for (PeriodOfAnalysis periodOfAnalysis : periodsOfAnalysis){
            List<BuildOfAnalysis> buildOfPeriod = periodUtils.getBuildOfPeriod(builds, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd());
            returnList.add(calcBuildDurationOfPeriod(buildOfPeriod, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd(), periodOfAnalysis.getPeriod(), measure, unit));
        }

        return returnList;
    }


    private PeriodOfAnalysis calcBuildDurationOfPeriod(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period, StatisticalMeasure measure, UnitOfTime unit) {

        validateBuild(builds);

        List<BigDecimal> buildsDurations = calcBuildDurationValues(builds,  start,  end,  unit);

        return new PeriodOfAnalysis("Build Duration", start, end, period, measure == StatisticalMeasure.MEAN ?  mathUtils.meanOfValues(buildsDurations) : mathUtils.medianOfValues(buildsDurations));
    }

    public PeriodOfAnalysis calcBuildDuration(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, StatisticalMeasure measure, UnitOfTime unit) {

        validateBuild(builds);

        List<BigDecimal> buildsDurations = calcBuildDurationValues(builds,  start,  end,  unit);

        return new PeriodOfAnalysis("Build Duration", start, end, PeriodOfAnalysis.PERIOD.UNIQUE, measure == StatisticalMeasure.MEAN ?  mathUtils.meanOfValues(buildsDurations) : mathUtils.medianOfValues(buildsDurations));
    }


    public List<BigDecimal> calcBuildDurationValues(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, UnitOfTime unit) {

        List<BuildOfAnalysis> buildOfPeriod = periodUtils.getBuildOfPeriod(builds, start, end);

        validateBuild(buildOfPeriod);

        List<BigDecimal> buildsDurations = new ArrayList<>();

        for(BuildOfAnalysis buildInfo : buildOfPeriod){
            Duration duration = Duration.between(buildInfo.startedAt, buildInfo.finishedAt);
            buildsDurations.add(dateUtils.scaleTime(duration.getSeconds(), unit));
        }

        return buildsDurations;
    }


    private void validateBuild(List<BuildOfAnalysis> builds) {
        for(BuildOfAnalysis buildInfo : builds){
            if(buildInfo.startedAt == null || buildInfo.finishedAt == null){
                throw new IllegalArgumentException(" build \"startedAt\" and \"finishedAt\" field are required. They can not have null values.");
            }

        }
    }


}
