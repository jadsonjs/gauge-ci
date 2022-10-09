package br.com.jadson.gaugeci.gauges;

import br.com.jadson.gaugeci.model.CommentOfAnalysis;
import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import br.com.jadson.gaugeci.model.StatisticalMeasure;
import br.com.jadson.gaugeci.utils.GaugeDateUtils;
import br.com.jadson.gaugeci.utils.GaugeMathUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class CommentsPerChangeGaugeTest {

    @Spy
    GaugeDateUtils dateUnit;

    @Spy
    GaugeMathUtils mathUtil;

    @InjectMocks
    CommentsPerChangeGauge gauge;

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calcCommentsPerChangeHistory() {

        LocalDateTime start = LocalDateTime.of(2022, 10, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 11, 30, 9, 0, 0);

        List<PeriodOfAnalysis> periods = gauge.calcCommentsPerChangeHistory(   getCommentOfAnalysesList(), start, end, PeriodOfAnalysis.PERIOD.MONTH, StatisticalMeasure.MEAN);

        Assertions.assertEquals(new BigDecimal("2.3333"), periods.get(0).getValue()); // 3, 2 and 2 on october
        Assertions.assertEquals(new BigDecimal("3.0000"), periods.get(1).getValue()); // 3 change on november
    }

    @Test
    void calcCommentsPerChange() {

        LocalDateTime start = LocalDateTime.of(2022, 10, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 11, 30, 9, 0, 0);

        PeriodOfAnalysis period = gauge.calcCommentsPerChange(   getCommentOfAnalysesList(), start, end, StatisticalMeasure.MEAN);

        Assertions.assertEquals(new BigDecimal("3.3333"), period.getValue());
        Assertions.assertEquals(PeriodOfAnalysis.PERIOD.UNIQUE, period.getPeriod());
    }

    @Test
    void calcCommentsPerChangeMedian() {


        LocalDateTime start = LocalDateTime.of(2022, 10, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 11, 30, 9, 0, 0);

        PeriodOfAnalysis period = gauge.calcCommentsPerChange(   getCommentOfAnalysesList(), start, end, StatisticalMeasure.MEDIAN);

        Assertions.assertEquals(new BigDecimal("3"), period.getValue());
        Assertions.assertEquals(PeriodOfAnalysis.PERIOD.UNIQUE, period.getPeriod());
    }


    @Test
    void calcCommentsPerChangeValues() {

        Map<Long, BigDecimal> map = gauge.calcCommentsPerChangeValues(   getCommentOfAnalysesList()   );

        Assertions.assertEquals(new BigDecimal("3"), map.get(10l));
        Assertions.assertEquals(new BigDecimal("2"), map.get(11l));
        Assertions.assertEquals(new BigDecimal("5"), map.get(12l));
    }


    /**
     * One change with 3 comments, other change with 2 comments and the last change with 5 comments
     *
     * 7 comment on october and 3 comments and november
     */
    private List<CommentOfAnalysis> getCommentOfAnalysesList() {

        List<CommentOfAnalysis> list = new ArrayList<>();

        CommentOfAnalysis c1 = new CommentOfAnalysis();
        c1.commentId = 1l;
        c1.createdAt = LocalDateTime.of(2022, 10, 9, 10, 0,0 );
        c1.changeNumber = 10l;

        CommentOfAnalysis c2 = new CommentOfAnalysis();
        c2.commentId = 2l;
        c2.createdAt = LocalDateTime.of(2022, 10, 9, 11, 0,0 );
        c2.changeNumber = 10l;

        CommentOfAnalysis c3 = new CommentOfAnalysis();
        c3.commentId = 3l;
        c3.createdAt = LocalDateTime.of(2022, 10, 9, 11, 10,0 );
        c3.changeNumber = 10l;

        CommentOfAnalysis c4 = new CommentOfAnalysis();
        c4.commentId = 4l;
        c4.createdAt = LocalDateTime.of(2022, 10, 9, 11, 11,0 );
        c4.changeNumber = 11l;

        CommentOfAnalysis c5 = new CommentOfAnalysis();
        c5.commentId = 5l;
        c5.createdAt = LocalDateTime.of(2022, 10, 9, 12, 10,0 );
        c5.changeNumber = 11l;

        CommentOfAnalysis c6 = new CommentOfAnalysis();
        c6.commentId = 6l;
        c6.createdAt = LocalDateTime.of(2022, 10, 10, 11, 10,0 );
        c6.changeNumber = 12l;

        CommentOfAnalysis c7 = new CommentOfAnalysis();
        c7.commentId = 7l;
        c7.createdAt = LocalDateTime.of(2022, 10, 10, 12, 10,0 );
        c7.changeNumber = 12l;

        CommentOfAnalysis c8 = new CommentOfAnalysis();
        c8.commentId = 8l;
        c8.createdAt = LocalDateTime.of(2022, 11, 9, 11, 10,0 );
        c8.changeNumber = 12l;


        CommentOfAnalysis c9 = new CommentOfAnalysis();
        c9.commentId = 9l;
        c9.createdAt = LocalDateTime.of(2022, 11, 10, 12, 10,0 );
        c9.changeNumber = 12l;


        CommentOfAnalysis c10 = new CommentOfAnalysis();
        c10.commentId = 10l;
        c10.createdAt = LocalDateTime.of(2022, 11, 15, 13, 10,0 );
        c10.changeNumber = 12l;

        list.add(c1);
        list.add(c2);
        list.add(c3);
        list.add(c4);
        list.add(c5);
        list.add(c6);
        list.add(c7);
        list.add(c8);
        list.add(c9);
        list.add(c10);
        return list;
    }
}