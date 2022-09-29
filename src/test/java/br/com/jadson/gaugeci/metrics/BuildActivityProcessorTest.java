package br.com.jadson.gaugeci.metrics;

import br.com.jadson.gaugeci.model.BuildOfAnalysis;
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
class BuildActivityProcessorTest {

    @Spy
    GaugeDateUtils dateUnit;

    @Spy
    GaugeMathUtils mathUtil;

    @InjectMocks
    BuildActivityProcessor processor;

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void calcBuildActivity100Percent() {

        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();

        BuildOfAnalysis b1 = new BuildOfAnalysis();
        b1.startedAt = LocalDateTime.of(2021,1,1,1,0,0);

        BuildOfAnalysis b2 = new BuildOfAnalysis();
        b2.startedAt = LocalDateTime.of(2021,1,2,1,0,0);

        BuildOfAnalysis b3 = new BuildOfAnalysis();
        b3.startedAt = LocalDateTime.of(2021,1,3,1,0,0);

        BuildOfAnalysis b4 = new BuildOfAnalysis();
        b4.startedAt = LocalDateTime.of(2021,1,4,1,0,0);

        BuildOfAnalysis b5 = new BuildOfAnalysis();
        b5.startedAt = LocalDateTime.of(2021,1,5,1,0,0);

        BuildOfAnalysis b6 = new BuildOfAnalysis();
        b6.startedAt = LocalDateTime.of(2021,1,6,1,0,0);

        BuildOfAnalysis b7 = new BuildOfAnalysis();
        b7.startedAt = LocalDateTime.of(2021,1,7,1,0,0);

        BuildOfAnalysis b8 = new BuildOfAnalysis();
        b8.startedAt = LocalDateTime.of(2021,1,8,1,0,0);

        BuildOfAnalysis b9 = new BuildOfAnalysis();
        b9.startedAt = LocalDateTime.of(2021,1,9,1,0,0);

        BuildOfAnalysis b10 = new BuildOfAnalysis();
        b10.startedAt = LocalDateTime.of(2021,1,10,1,0,0);


        buildsInfo.add(b1);
        buildsInfo.add(b2);
        buildsInfo.add(b3);
        buildsInfo.add(b4);
        buildsInfo.add(b5);
        buildsInfo.add(b6);
        buildsInfo.add(b7);
        buildsInfo.add(b8);
        buildsInfo.add(b9);
        buildsInfo.add(b10);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 10, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("1.0000"), processor.calcBuildActivity(buildsInfo, startReleaseDate, endReleaseDate).getValue());

    }


    @Test
    void calcBuildActivity50Percent() {

        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();

        BuildOfAnalysis b1 = new BuildOfAnalysis();
        b1.startedAt = LocalDateTime.of(2021,1,1,1,0,0);

        BuildOfAnalysis b2 = new BuildOfAnalysis();
        b2.startedAt = LocalDateTime.of(2021,1,2,1,0,0);

        BuildOfAnalysis b3 = new BuildOfAnalysis();
        b3.startedAt = LocalDateTime.of(2021,1,3,1,0,0);

        BuildOfAnalysis b4 = new BuildOfAnalysis();
        b4.startedAt = LocalDateTime.of(2021,1,4,1,0,0);

        BuildOfAnalysis b5 = new BuildOfAnalysis();
        b5.startedAt = LocalDateTime.of(2021,1,5,1,0,0);


        buildsInfo.add(b1);
        buildsInfo.add(b2);
        buildsInfo.add(b3);
        buildsInfo.add(b4);
        buildsInfo.add(b5);


        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 10, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("0.5000"), processor.calcBuildActivity(buildsInfo, startReleaseDate, endReleaseDate).getValue());

    }

    @Test
    void calcBuildsActivity0Percent() {


        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();


        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 10, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("0.0000"), processor.calcBuildActivity(buildsInfo, startReleaseDate, endReleaseDate).getValue());

    }

    @Test
    void calcBuildActivityHistory15Percent10Percent() {

        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();

        BuildOfAnalysis b1 = new BuildOfAnalysis();
        b1.startedAt = LocalDateTime.of(2021,1,1,1,0,0);

        BuildOfAnalysis b2 = new BuildOfAnalysis();
        b2.startedAt = LocalDateTime.of(2021,1,2,1,0,0);

        BuildOfAnalysis b3 = new BuildOfAnalysis();
        b3.startedAt = LocalDateTime.of(2021,1,3,1,0,0);

        BuildOfAnalysis b4 = new BuildOfAnalysis();
        b4.startedAt = LocalDateTime.of(2021,1,4,1,0,0);

        BuildOfAnalysis b5 = new BuildOfAnalysis();
        b5.startedAt = LocalDateTime.of(2021,1,5,1,0,0);


        buildsInfo.add(b1);
        buildsInfo.add(b2);
        buildsInfo.add(b3);
        buildsInfo.add(b4);
        buildsInfo.add(b5);

        // next month

        BuildOfAnalysis b6 = new BuildOfAnalysis();
        b6.startedAt = LocalDateTime.of(2021,2,2,1,0,0);
        BuildOfAnalysis b7 = new BuildOfAnalysis();
        b7.startedAt = LocalDateTime.of(2021,2,3,1,0,0);

        buildsInfo.add(b6);
        buildsInfo.add(b7);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 2, 11, 20, 0, 0);

        List<PeriodOfAnalysis> periods = processor.calcBuildActivityHistory(buildsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH);

        Assertions.assertEquals(2, periods.size());
        Assertions.assertEquals(new BigDecimal("0.1563"), periods.get(0).getValue());  //  5 builds in different in 32 days
        Assertions.assertEquals(new BigDecimal("0.2000"), periods.get(1).getValue());  //  2 builds in 10 days

    }
}