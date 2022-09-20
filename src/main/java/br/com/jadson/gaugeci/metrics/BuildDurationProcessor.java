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
            returnList.add(calcBuildDuration(builds, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd(), periodOfAnalysis.getPeriod(), measure, unit));
        }

        return returnList;
    }



    public PeriodOfAnalysis calcBuildDuration(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period, StatisticalMeasure measure, UnitOfTime unit) {

        List<BuildOfAnalysis> buildOfPeriod = periodUtils.getBuildOfPeriod(builds, start, end);

        List<BigDecimal> buildsDurations = new ArrayList<>();

        for(BuildOfAnalysis buildInfo : buildOfPeriod){
            buildsDurations.add(dateUtils.scaleTime(buildInfo.finishedAt.getSecond()- buildInfo.startedAt.getSecond(), unit));
        }

        return new PeriodOfAnalysis("Build Duration", start, end, period, measure == StatisticalMeasure.MEAN ?  mathUtils.meanOfValues(buildsDurations) : mathUtils.medianOfValues(buildsDurations));
    }



}
