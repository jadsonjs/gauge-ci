package br.com.jadson.gaugeci.gauges;

import br.com.jadson.gaugeci.model.CommentOfAnalysis;
import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import br.com.jadson.gaugeci.model.StatisticalMeasure;
import br.com.jadson.gaugeci.utils.GaugeDateUtils;
import br.com.jadson.gaugeci.utils.GaugeMathUtils;
import br.com.jadson.gaugeci.utils.GaugePeriodOfAnalysisUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommentsPerChangeGauge {

    GaugeDateUtils dateUtils;

    GaugeMathUtils mathUtils;

    GaugePeriodOfAnalysisUtils periodUtils;

    public CommentsPerChangeGauge(){
        this.mathUtils = new GaugeMathUtils();
        this.dateUtils = new GaugeDateUtils();
        this.periodUtils = new GaugePeriodOfAnalysisUtils();
    }

    public List<PeriodOfAnalysis> calcCommentsPerChangeHistory(List<CommentOfAnalysis> comments, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period,  StatisticalMeasure measure) {

        List<PeriodOfAnalysis> periodsOfAnalysis = PeriodOfAnalysis.generatePeriodsOfAnalysis(start, end, period);

        List<PeriodOfAnalysis> returnList = new ArrayList<>();
        for (PeriodOfAnalysis periodOfAnalysis : periodsOfAnalysis){
            List<CommentOfAnalysis> commentsOfPeriod = periodUtils.getCommentsOfPeriod(comments, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd());
            returnList.add(calcCommentsPerChangeOfPeriod(commentsOfPeriod, periodOfAnalysis.getStart(), periodOfAnalysis.getEnd(), periodOfAnalysis.getPeriod(), measure));
        }

        return returnList;

    }


    public PeriodOfAnalysis calcCommentsPerChangeOfPeriod(List<CommentOfAnalysis> comments, LocalDateTime start, LocalDateTime end, PeriodOfAnalysis.PERIOD period, StatisticalMeasure measure) {
        Map<Long, BigDecimal> commentsPerPRMap = calcCommentsPerChangeValues(comments);
        List<BigDecimal> values = new ArrayList<>(commentsPerPRMap.values());
        return new PeriodOfAnalysis("Comments Per Change", start, end, period, measure == StatisticalMeasure.MEAN ?  mathUtils.meanOfValues(values) : mathUtils.medianOfValues(values));

    }

    public PeriodOfAnalysis calcCommentsPerChange(List<CommentOfAnalysis> comments, LocalDateTime start, LocalDateTime end, StatisticalMeasure measure) {
        Map<Long, BigDecimal> commentsPerPRMap = calcCommentsPerChangeValues(comments);
        List<BigDecimal> values = new ArrayList<>(commentsPerPRMap.values());
        return new PeriodOfAnalysis("Comments Per Change", start, end, PeriodOfAnalysis.PERIOD.UNIQUE, measure == StatisticalMeasure.MEAN ?  mathUtils.meanOfValues(values) : mathUtils.medianOfValues(values));

    }

    /**
     * Return a map with the change number and the qty of comments
     *
     * The metric is Just group the comments by change
     *
     * @param comments
     * @return
     */
    public Map<Long, BigDecimal> calcCommentsPerChangeValues(List<CommentOfAnalysis> comments) {

        Map<Long, BigDecimal> commentsPerPRMap = new HashMap<>();

        // split the qtd of comments by PR
        for (CommentOfAnalysis  c : comments){

            if(commentsPerPRMap.containsKey(c.changeNumber)){
                commentsPerPRMap.put(c.changeNumber, (commentsPerPRMap.get(c.changeNumber).add(BigDecimal.ONE) ) );
            }else{
                commentsPerPRMap.put(c.changeNumber, BigDecimal.ONE);
            }
        }
        return commentsPerPRMap;
    }


}
