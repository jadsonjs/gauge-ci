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
import java.util.ArrayList;
import java.util.List;

/**
 * *** Build Health ***: It is a unit interval representing the rate of build failures across days.
 * If there were build failures every day, the value would be 0. if there were no build failures, the value would be 1.
 *
 *  totalBuilds - qtdBrokenBuilds / totalBuilds
 *
 * @author  Jadson Santos - jadsonjs@gmail.com
 */
@Component
public class BuildHealthProcessor {

    GaugeDateUtils dateUtils;

    GaugeMathUtils mathUtils;

    GaugePeriodOfAnalysisUtils periodUtils;

    public BuildHealthProcessor(){
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
    public List<PeriodOfAnalysis> calcBuildHealthHistory(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period) {

        List<PeriodOfAnalysis> periodsOfAnalysis = PeriodOfAnalysis.generatePeriodsOfAnalysis(start, end, period);

        List<PeriodOfAnalysis> returnList = new ArrayList<>();
        for (PeriodOfAnalysis periodOfAnalysis : periodsOfAnalysis){
            List<BuildOfAnalysis> buildOfPeriod = periodUtils.getBuildOfPeriod(builds, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd());
            returnList.add(calcBuildHealthOfPeriod(buildOfPeriod, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd(), periodOfAnalysis.getPeriod()));
        }

        return returnList;
    }

    public PeriodOfAnalysis calcBuildHealthOfPeriod(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period) {
        return new PeriodOfAnalysis("Build Health", start, end, period, calcBuildHealthValues(builds) );
    }

    public PeriodOfAnalysis calcBuildHealth(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end) {
        return new PeriodOfAnalysis("Build Health", start, end, PeriodOfAnalysis.PERIOD.UNIQUE, calcBuildHealthValues(builds) );
    }


    public BigDecimal calcBuildHealthValues(List<BuildOfAnalysis> builds) {

        long totalBuilds = builds.size();

        BigDecimal buildHealth = BigDecimal.ZERO;

        if(totalBuilds > 0) {

            int qtdBrokenBuilds = 0;
            for (BuildOfAnalysis build : builds) {
                if (build.state.equals("failed")) {
                    qtdBrokenBuilds++;
                }
            }

            // (totalBuilds - qtdBrokenBuilds) /  totalBuilds
            buildHealth = (new BigDecimal(totalBuilds).subtract(new BigDecimal(qtdBrokenBuilds)))
                    .divide(new BigDecimal(totalBuilds), GaugeMathUtils.SCALE, RoundingMode.HALF_UP);
        }

        return buildHealth;

    }



}
