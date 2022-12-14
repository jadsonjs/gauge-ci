package br.com.jadson.gaugeci.gauges;

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
class BuildHealthGaugeTest {

    @Spy
    GaugeDateUtils dateUnit;

    @Spy
    GaugeMathUtils mathUtil;

    @InjectMocks
    BuildHealthGauge processor = new BuildHealthGauge();

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calcBuildHealth() {

        List<BuildOfAnalysis> buildsInfo = getSequenceOfBuildsWithFailure();

        // 9  builds, and 5 broken builds  ( (9-5) / 9 = 0.4444)
        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 10, 0, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 15, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("0.4444"), processor.calcBuildHealth(buildsInfo, startReleaseDate, endReleaseDate).getValue());
    }


    @Test
    void calcBuildHealthPerfect() {

        List<BuildOfAnalysis> buildsInfo = getSequenceOfBuildsWithOutFailure();


        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 10, 0, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 15, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("1.0000"), processor.calcBuildHealth(buildsInfo, startReleaseDate, endReleaseDate).getValue());
    }

    @Test
    void calcBuildHealthProblem() {

        List<BuildOfAnalysis> buildsInfo = getSequenceOfBuildsOnlyFailure();

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 10, 0, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 15, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("0.0000"), processor.calcBuildHealth(buildsInfo, startReleaseDate, endReleaseDate).getValue());
    }


    @Test
    void calcBuildHealthHistory() {

        List<BuildOfAnalysis> buildsInfo = getSequenceOfBuildsMultiYears();


        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 10, 0, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2022, 1, 15, 9, 0, 0);
        List<PeriodOfAnalysis> periods = processor.calcBuildHealthHistory(buildsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.YEAR);


        Assertions.assertEquals(new BigDecimal("1.0000"), periods.get(0).getValue()); // 100% build health in 2021
        // 9  builds, and 5 broken builds  ( (9-5) / 9 = 0.4444)
        Assertions.assertEquals(new BigDecimal("0.4444"), periods.get(1).getValue()); // 44% build health in 2022
    }


    //////////////////////////////////////////////////////////////////////



    private List<BuildOfAnalysis> getSequenceOfBuildsWithFailure () {

        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();

        BuildOfAnalysis b1 = new BuildOfAnalysis();
        b1.startedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.finishedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.state = "passed";

        BuildOfAnalysis b2 = new BuildOfAnalysis();
        b2.startedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.state = "failed";

        BuildOfAnalysis b3 = new BuildOfAnalysis();
        b3.startedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.state = "failed";

        BuildOfAnalysis b4 = new BuildOfAnalysis();
        b4.startedAt = LocalDateTime.of(2021, 1, 10, 2, 40, 0);
        b4.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 40, 0);
        b4.state = "failed";

        // 50 min = 3.000 seconds
        BuildOfAnalysis b5 = new BuildOfAnalysis();
        b5.startedAt = LocalDateTime.of(2021, 1, 10, 2, 50, 0);
        b5.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 50, 0);
        b5.state = "passed";

        BuildOfAnalysis b6 = new BuildOfAnalysis();
        b6.startedAt = LocalDateTime.of(2021, 1, 11, 1, 0, 0);
        b6.finishedAt = LocalDateTime.of(2021, 1, 11, 1, 0, 0);
        b6.state = "failed";

        // 24 h = 86.400 seconds
        BuildOfAnalysis b7 = new BuildOfAnalysis();
        b7.startedAt = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        b7.finishedAt = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        b7.state = "passed";

        // 1h = 3600s
        BuildOfAnalysis b8 = new BuildOfAnalysis();
        b8.startedAt = LocalDateTime.of(2021, 1, 12, 10, 0, 0);
        b8.finishedAt = LocalDateTime.of(2021, 1, 12, 10, 0, 0);
        b8.state = "failed";

        BuildOfAnalysis b9 = new BuildOfAnalysis();
        b9.startedAt = LocalDateTime.of(2021, 1, 12, 11, 0, 0);
        b9.finishedAt = LocalDateTime.of(2021, 1, 12, 11, 0, 0);
        b9.state = "passed";

        // median of 44.700 seconds to fix broken build

        buildsInfo.add(b1);
        buildsInfo.add(b2);
        buildsInfo.add(b3);
        buildsInfo.add(b4);
        buildsInfo.add(b5);
        buildsInfo.add(b6);
        buildsInfo.add(b7);
        buildsInfo.add(b8);
        buildsInfo.add(b9);
        return buildsInfo;
    }


    private List<BuildOfAnalysis> getSequenceOfBuildsWithOutFailure() {

        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();

        BuildOfAnalysis b1 = new BuildOfAnalysis();
        b1.startedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.finishedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.state = "passed";

        BuildOfAnalysis b2 = new BuildOfAnalysis();
        b2.startedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.state = "passed";

        BuildOfAnalysis b3 = new BuildOfAnalysis();
        b3.startedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.state = "passed";

        BuildOfAnalysis b4 = new BuildOfAnalysis();
        b4.startedAt = LocalDateTime.of(2021, 1, 10, 2, 40, 0);
        b4.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 40, 0);
        b4.state = "passed";

        BuildOfAnalysis b5 = new BuildOfAnalysis();
        b5.startedAt = LocalDateTime.of(2021, 1, 10, 2, 50, 0);
        b5.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 50, 0);
        b5.state = "passed";

        BuildOfAnalysis b6 = new BuildOfAnalysis();
        b6.startedAt = LocalDateTime.of(2021, 1, 11, 1, 0, 0);
        b6.finishedAt = LocalDateTime.of(2021, 1, 11, 1, 0, 0);
        b6.state = "passed";

        BuildOfAnalysis b7 = new BuildOfAnalysis();
        b7.startedAt = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        b7.finishedAt = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        b7.state = "passed";

        BuildOfAnalysis b8 = new BuildOfAnalysis();
        b8.startedAt = LocalDateTime.of(2021, 1, 12, 10, 0, 0);
        b8.finishedAt = LocalDateTime.of(2021, 1, 12, 10, 0, 0);
        b8.state = "passed";

        BuildOfAnalysis b9 = new BuildOfAnalysis();
        b9.startedAt = LocalDateTime.of(2021, 1, 12, 11, 0, 0);
        b9.finishedAt = LocalDateTime.of(2021, 1, 12, 11, 0, 0);
        b9.state = "passed";

        // median of 44.700 seconds to fix broken build

        buildsInfo.add(b1);
        buildsInfo.add(b2);
        buildsInfo.add(b3);
        buildsInfo.add(b4);
        buildsInfo.add(b5);
        buildsInfo.add(b6);
        buildsInfo.add(b7);
        buildsInfo.add(b8);
        buildsInfo.add(b9);
        return buildsInfo;
    }

    private List<BuildOfAnalysis> getSequenceOfBuildsOnlyFailure() {

        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();

        BuildOfAnalysis b1 = new BuildOfAnalysis();
        b1.startedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.finishedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.state = "failed";

        BuildOfAnalysis b2 = new BuildOfAnalysis();
        b2.startedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.state = "failed";

        BuildOfAnalysis b3 = new BuildOfAnalysis();
        b3.startedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.state = "failed";

        BuildOfAnalysis b4 = new BuildOfAnalysis();
        b4.startedAt = LocalDateTime.of(2021, 1, 10, 2, 40, 0);
        b4.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 40, 0);
        b4.state = "failed";

        BuildOfAnalysis b5 = new BuildOfAnalysis();
        b5.startedAt = LocalDateTime.of(2021, 1, 10, 2, 50, 0);
        b5.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 50, 0);
        b5.state = "failed";

        BuildOfAnalysis b6 = new BuildOfAnalysis();
        b6.startedAt = LocalDateTime.of(2021, 1, 11, 1, 0, 0);
        b6.finishedAt = LocalDateTime.of(2021, 1, 11, 1, 0, 0);
        b6.state = "failed";

        BuildOfAnalysis b7 = new BuildOfAnalysis();
        b7.startedAt = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        b7.finishedAt = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        b7.state = "failed";

        BuildOfAnalysis b8 = new BuildOfAnalysis();
        b8.startedAt = LocalDateTime.of(2021, 1, 12, 10, 0, 0);
        b8.finishedAt = LocalDateTime.of(2021, 1, 12, 10, 0, 0);
        b8.state = "failed";

        BuildOfAnalysis b9 = new BuildOfAnalysis();
        b9.startedAt = LocalDateTime.of(2021, 1, 12, 11, 0, 0);
        b9.finishedAt = LocalDateTime.of(2021, 1, 12, 11, 0, 0);
        b9.state = "failed";

        // median of 44.700 seconds to fix broken build

        buildsInfo.add(b1);
        buildsInfo.add(b2);
        buildsInfo.add(b3);
        buildsInfo.add(b4);
        buildsInfo.add(b5);
        buildsInfo.add(b6);
        buildsInfo.add(b7);
        buildsInfo.add(b8);
        buildsInfo.add(b9);
        return buildsInfo;
    }





    private List<BuildOfAnalysis> getSequenceOfBuildsMultiYears() {

        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();

        BuildOfAnalysis b1 = new BuildOfAnalysis();
        b1.startedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.finishedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.state = "passed";

        BuildOfAnalysis b2 = new BuildOfAnalysis();
        b2.startedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.state = "passed";

        BuildOfAnalysis b3 = new BuildOfAnalysis();
        b3.startedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.state = "passed";

        BuildOfAnalysis b4 = new BuildOfAnalysis();
        b4.startedAt = LocalDateTime.of(2021, 1, 10, 2, 40, 0);
        b4.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 40, 0);
        b4.state = "passed";

        BuildOfAnalysis b5 = new BuildOfAnalysis();
        b5.startedAt = LocalDateTime.of(2021, 1, 10, 2, 50, 0);
        b5.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 50, 0);
        b5.state = "passed";

        BuildOfAnalysis b6 = new BuildOfAnalysis();
        b6.startedAt = LocalDateTime.of(2021, 1, 11, 1, 0, 0);
        b6.finishedAt = LocalDateTime.of(2021, 1, 11, 1, 0, 0);
        b6.state = "passed";

        BuildOfAnalysis b7 = new BuildOfAnalysis();
        b7.startedAt = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        b7.finishedAt = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        b7.state = "passed";

        BuildOfAnalysis b8 = new BuildOfAnalysis();
        b8.startedAt = LocalDateTime.of(2021, 1, 12, 10, 0, 0);
        b8.finishedAt = LocalDateTime.of(2021, 1, 12, 10, 0, 0);
        b8.state = "passed";

        BuildOfAnalysis b9 = new BuildOfAnalysis();
        b9.startedAt = LocalDateTime.of(2021, 1, 12, 11, 0, 0);
        b9.finishedAt = LocalDateTime.of(2021, 1, 12, 11, 0, 0);
        b9.state = "passed";

        // median of 44.700 seconds to fix broken build

        buildsInfo.add(b1);
        buildsInfo.add(b2);
        buildsInfo.add(b3);
        buildsInfo.add(b4);
        buildsInfo.add(b5);
        buildsInfo.add(b6);
        buildsInfo.add(b7);
        buildsInfo.add(b8);
        buildsInfo.add(b9);


        BuildOfAnalysis b10 = new BuildOfAnalysis();
        b10.startedAt = LocalDateTime.of(2022, 1, 12, 1, 0, 0);
        b10.finishedAt = LocalDateTime.of(2022, 1, 12, 1, 0, 0);
        b10.state = "passed";

        BuildOfAnalysis b11 = new BuildOfAnalysis();
        b11.startedAt = LocalDateTime.of(2022, 1, 12, 2, 0, 0);
        b11.finishedAt = LocalDateTime.of(2022, 1, 12, 2, 0, 0);
        b11.state = "failed";

        BuildOfAnalysis b12 = new BuildOfAnalysis();
        b12.startedAt = LocalDateTime.of(2022, 1, 13, 2, 30, 0);
        b12.finishedAt = LocalDateTime.of(2022, 1, 13, 2, 30, 0);
        b12.state = "failed";

        BuildOfAnalysis b13 = new BuildOfAnalysis();
        b13.startedAt = LocalDateTime.of(2022, 1, 13, 2, 40, 0);
        b13.finishedAt = LocalDateTime.of(2022, 1, 13, 2, 40, 0);
        b13.state = "failed";

        // 50 min = 3.000 seconds
        BuildOfAnalysis b14 = new BuildOfAnalysis();
        b14.startedAt = LocalDateTime.of(2022, 1, 14, 2, 50, 0);
        b14.finishedAt = LocalDateTime.of(2022, 1, 14, 2, 50, 0);
        b14.state = "passed";

        BuildOfAnalysis b15 = new BuildOfAnalysis();
        b15.startedAt = LocalDateTime.of(2022, 1, 14, 8, 0, 0);
        b15.finishedAt = LocalDateTime.of(2022, 1, 14, 8, 30, 20);
        b15.state = "failed";

        // 24 h = 86.400 seconds
        BuildOfAnalysis b16 = new BuildOfAnalysis();
        b16.startedAt = LocalDateTime.of(2022, 1, 14, 9, 0, 0);
        b16.finishedAt = LocalDateTime.of(2022, 1, 14, 9, 30, 0);
        b16.state = "passed";

        // 1h = 3600s
        BuildOfAnalysis b17 = new BuildOfAnalysis();
        b17.startedAt = LocalDateTime.of(2022, 1, 14, 10, 0, 0);
        b17.finishedAt = LocalDateTime.of(2022, 1, 14, 10, 0, 0);
        b17.state = "failed";

        BuildOfAnalysis b18 = new BuildOfAnalysis();
        b18.startedAt = LocalDateTime.of(2022, 1, 14, 11, 0, 0);
        b18.finishedAt = LocalDateTime.of(2022, 1, 14, 11, 0, 0);
        b18.state = "passed";

        // median of 44.700 seconds to fix broken build

        buildsInfo.add(b10);
        buildsInfo.add(b11);
        buildsInfo.add(b12);
        buildsInfo.add(b13);
        buildsInfo.add(b14);
        buildsInfo.add(b15);
        buildsInfo.add(b16);
        buildsInfo.add(b17);
        buildsInfo.add(b18);


        return buildsInfo;
    }


}