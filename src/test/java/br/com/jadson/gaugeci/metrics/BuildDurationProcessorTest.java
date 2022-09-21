package br.com.jadson.gaugeci.metrics;

import br.com.jadson.gaugeci.model.BuildOfAnalysis;
import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import br.com.jadson.gaugeci.model.StatisticalMeasure;
import br.com.jadson.gaugeci.model.UnitOfTime;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BuildDurationProcessorTest {

    @Spy
    GaugeDateUtils dateUnit;

    @Spy
    GaugeMathUtils mathUtil;

    @InjectMocks
    BuildDurationProcessor processor;

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calcBuildDurationMedian() {

        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();

        BuildOfAnalysis b1 = new BuildOfAnalysis();
        b1.startedAt = LocalDateTime.of(2021,1,1,1,0,0);
        b1.finishedAt = LocalDateTime.of(2021,1,1,1,30,0);

        BuildOfAnalysis b2 = new BuildOfAnalysis();
        b2.startedAt = LocalDateTime.of(2021,1,2,1,0,0);
        b2.finishedAt = LocalDateTime.of(2021,1,2,1,40,0);

        BuildOfAnalysis b3 = new BuildOfAnalysis();
        b3.startedAt = LocalDateTime.of(2021,1,3,1,0,0);
        b3.finishedAt = LocalDateTime.of(2021,1,3,1,20,0);

        BuildOfAnalysis b4 = new BuildOfAnalysis();
        b4.startedAt = LocalDateTime.of(2021,1,4,1,0,0);
        b4.finishedAt = LocalDateTime.of(2021,1,4,1,10,0);

        BuildOfAnalysis b5 = new BuildOfAnalysis();
        b5.startedAt = LocalDateTime.of(2021,1,5,1,0,0);
        b5.finishedAt = LocalDateTime.of(2021,1,5,2,0,0);


        buildsInfo.add(b1);
        buildsInfo.add(b2);
        buildsInfo.add(b3);
        buildsInfo.add(b4);
        buildsInfo.add(b5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 1, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 10, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("30.0000"), processor.calcBuildDuration(buildsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH, StatisticalMeasure.MEDIAN, UnitOfTime.MINUTES).getValue());
    }


    @Test
    void calcBuildDurationMean() {

        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();

        BuildOfAnalysis b1 = new BuildOfAnalysis();
        b1.startedAt = LocalDateTime.of(2021,1,1,1,0,0);
        b1.finishedAt = LocalDateTime.of(2021,1,1,1,30,0);

        BuildOfAnalysis b2 = new BuildOfAnalysis();
        b2.startedAt = LocalDateTime.of(2021,1,2,1,0,0);
        b2.finishedAt = LocalDateTime.of(2021,1,2,1,40,0);

        BuildOfAnalysis b3 = new BuildOfAnalysis();
        b3.startedAt = LocalDateTime.of(2021,1,3,1,0,0);
        b3.finishedAt = LocalDateTime.of(2021,1,3,1,20,0);

        BuildOfAnalysis b4 = new BuildOfAnalysis();
        b4.startedAt = LocalDateTime.of(2021,1,4,1,0,0);
        b4.finishedAt = LocalDateTime.of(2021,1,4,1,10,0);

        BuildOfAnalysis b5 = new BuildOfAnalysis();
        b5.startedAt = LocalDateTime.of(2021,1,5,1,0,0);
        b5.finishedAt = LocalDateTime.of(2021,1,5,2,0,0);


        buildsInfo.add(b1);
        buildsInfo.add(b2);
        buildsInfo.add(b3);
        buildsInfo.add(b4);
        buildsInfo.add(b5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 1, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 10, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("32.0000"), processor.calcBuildDuration(buildsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH, StatisticalMeasure.MEAN, UnitOfTime.MINUTES).getValue());
    }
}