package br.com.jadson.gaugeci.gauges;

import br.com.jadson.gaugeci.model.CommitOfAnalysis;
import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
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

@ExtendWith(MockitoExtension.class)
class CommitsPerWeekDayGaugeTest {

    @Spy
    GaugeDateUtils dateUnit;

    @Spy
    GaugeMathUtils mathUtil;

    @InjectMocks
    CommitsPerWeekDayGauge processor;

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calcCommitsPerWeekDayHistory() {

        List<CommitOfAnalysis> commitsInfo = new ArrayList<>();

        CommitOfAnalysis c1 = new CommitOfAnalysis();
        c1.date = LocalDateTime.of(2021,3,1,1,0,0);

        CommitOfAnalysis c2 = new CommitOfAnalysis();
        c2.date = LocalDateTime.of(2021,3,2,1,0,0);

        CommitOfAnalysis c3 = new CommitOfAnalysis();
        c3.date = LocalDateTime.of(2021,3,3,1,0,0);

        CommitOfAnalysis c4 = new CommitOfAnalysis();
        c4.date = LocalDateTime.of(2021,3,4,1,0,0);

        CommitOfAnalysis c5 = new CommitOfAnalysis();
        c5.date = LocalDateTime.of(2021,3,5,1,0,0);

        commitsInfo.add(c1);
        commitsInfo.add(c2);
        commitsInfo.add(c3);
        commitsInfo.add(c4);
        commitsInfo.add(c5);

        // next period

        CommitOfAnalysis c6 = new CommitOfAnalysis();
        c6.date = LocalDateTime.of(2021,4,5,1,0,0);

        CommitOfAnalysis c7 = new CommitOfAnalysis();
        c7.date = LocalDateTime.of(2021,4,6,1,0,0);

        //10 commints day 5
        commitsInfo.add(c6);commitsInfo.add(c6);commitsInfo.add(c6);commitsInfo.add(c6);commitsInfo.add(c6);
        commitsInfo.add(c6);commitsInfo.add(c6);commitsInfo.add(c6);commitsInfo.add(c6);commitsInfo.add(c6);

        //10 commints day 6
        commitsInfo.add(c7);commitsInfo.add(c7);commitsInfo.add(c7);commitsInfo.add(c7);commitsInfo.add(c7);
        commitsInfo.add(c7);commitsInfo.add(c7);commitsInfo.add(c7);commitsInfo.add(c7);commitsInfo.add(c7);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 3, 1, 1, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 4, 30, 9, 0, 0);

        List<PeriodOfAnalysis> periods = processor.calcCommitsPerWeekDayHistory(commitsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH);

        Assertions.assertEquals(2, periods.size());
        Assertions.assertEquals(new BigDecimal("0.1563"), periods.get(0).getValue()); // 5 commtis in 32 days
        Assertions.assertEquals(new BigDecimal("0.6897"), periods.get(1).getValue()); // 20 commits in 29 days

    }


    @Test
    void calcCommitsPerWeekDay() {

        List<CommitOfAnalysis> commitsInfo = new ArrayList<>();

        CommitOfAnalysis c1 = new CommitOfAnalysis();

        c1.date = LocalDateTime.of(2021,1,1,1,0,0);

        CommitOfAnalysis c2 = new CommitOfAnalysis();
        c2.date = LocalDateTime.of(2021,1,2,1,0,0);

        CommitOfAnalysis c3 = new CommitOfAnalysis();
        c3.date = LocalDateTime.of(2021,1,3,1,0,0);

        CommitOfAnalysis c4 = new CommitOfAnalysis();
        c4.date = LocalDateTime.of(2021,1,4,1,0,0);

        CommitOfAnalysis c5 = new CommitOfAnalysis();
        c5.date = LocalDateTime.of(2021,1,5,1,0,0);

        // 5 commits day 1
        commitsInfo.add(c1);commitsInfo.add(c1);commitsInfo.add(c1);commitsInfo.add(c1);commitsInfo.add(c1);
        // 4 commits day 2
        commitsInfo.add(c2);commitsInfo.add(c2);commitsInfo.add(c2);commitsInfo.add(c2);
        // 3 commits day 3
        commitsInfo.add(c3);commitsInfo.add(c3);commitsInfo.add(c3);
        // 10 commits day 4
        commitsInfo.add(c4);commitsInfo.add(c4);commitsInfo.add(c4);commitsInfo.add(c4);commitsInfo.add(c4);commitsInfo.add(c4);commitsInfo.add(c4);commitsInfo.add(c4);commitsInfo.add(c4);commitsInfo.add(c4);
        // 1 commits day 5
        commitsInfo.add(c5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 1, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 5, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("4.6000"), processor.calcCommitsPerWeekDay(commitsInfo, startReleaseDate, endReleaseDate).getValue());
    }


    @Test
    void calcCommitsPerWeekDayNotSpringBootApplication() {

        List<CommitOfAnalysis> commitsInfo = new ArrayList<>();

        CommitOfAnalysis c1 = new CommitOfAnalysis();

        c1.date = LocalDateTime.of(2021,1,1,1,0,0);

        CommitOfAnalysis c2 = new CommitOfAnalysis();
        c2.date = LocalDateTime.of(2021,1,2,1,0,0);

        CommitOfAnalysis c3 = new CommitOfAnalysis();
        c3.date = LocalDateTime.of(2021,1,3,1,0,0);

        CommitOfAnalysis c4 = new CommitOfAnalysis();
        c4.date = LocalDateTime.of(2021,1,4,1,0,0);

        CommitOfAnalysis c5 = new CommitOfAnalysis();
        c5.date = LocalDateTime.of(2021,1,5,1,0,0);

        commitsInfo.add(c1);
        commitsInfo.add(c2);
        commitsInfo.add(c3);
        commitsInfo.add(c4);
        commitsInfo.add(c5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 1, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 5, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("1.0000"), new CommitsPerWeekDayGauge().calcCommitsPerWeekDay(commitsInfo, startReleaseDate, endReleaseDate).getValue());
    }

}