package br.com.jadson.gaugeci.gauges;

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

@ExtendWith(MockitoExtension.class)
class TimeToFixBrokenBuildGaugeTest {

    @Spy
    GaugeDateUtils dateUnit;

    @Spy
    GaugeMathUtils mathUtil;

    @InjectMocks
    TimeToFixBrokenBuildGauge processor = new TimeToFixBrokenBuildGauge();

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calcTimeToFixBrokenBuild() {

        List<BuildOfAnalysis> buildsInfo = getSequenceOfBuildsWithFailure();

        Assertions.assertEquals(new BigDecimal("3600"), processor.calcTimeToFixBrokenBuild(buildsInfo, StatisticalMeasure.MEDIAN, UnitOfTime.SECONDS).getValue());
    }

    @Test
    void calcTimeToFixBrokenBuildOtherLabels() {

        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();

        BuildOfAnalysis b1 = new BuildOfAnalysis();
        b1.startedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.finishedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.state = "success";

        BuildOfAnalysis b2 = new BuildOfAnalysis();
        b2.startedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.state = "failure";

        BuildOfAnalysis b3 = new BuildOfAnalysis();
        b3.startedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.state = "failure";

        BuildOfAnalysis b4 = new BuildOfAnalysis();
        b4.startedAt = LocalDateTime.of(2021, 1, 10, 2, 40, 0);
        b4.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 40, 0);
        b4.state = "failure";

        // 50 min = 3.000 seconds
        BuildOfAnalysis b5 = new BuildOfAnalysis();
        b5.startedAt = LocalDateTime.of(2021, 1, 10, 2, 50, 0);
        b5.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 50, 0);
        b5.state = "success";

        BuildOfAnalysis b6 = new BuildOfAnalysis();
        b6.startedAt = LocalDateTime.of(2021, 1, 11, 1, 0, 0);
        b6.finishedAt = LocalDateTime.of(2021, 1, 11, 1, 0, 0);
        b6.state = "failure";

        // 24 h = 86.400 seconds
        BuildOfAnalysis b7 = new BuildOfAnalysis();
        b7.startedAt = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        b7.finishedAt = LocalDateTime.of(2021, 1, 12, 1, 0, 0);
        b7.state = "success";

        // 1h = 3600s
        BuildOfAnalysis b8 = new BuildOfAnalysis();
        b8.startedAt = LocalDateTime.of(2021, 1, 12, 10, 0, 0);
        b8.finishedAt = LocalDateTime.of(2021, 1, 12, 10, 0, 0);
        b8.state = "failure";

        BuildOfAnalysis b9 = new BuildOfAnalysis();
        b9.startedAt = LocalDateTime.of(2021, 1, 12, 11, 0, 0);
        b9.finishedAt = LocalDateTime.of(2021, 1, 12, 11, 0, 0);
        b9.state = "success";

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

        Assertions.assertEquals(new BigDecimal("3600"), new TimeToFixBrokenBuildGauge("success", "failure")
                .calcTimeToFixBrokenBuild(buildsInfo, StatisticalMeasure.MEDIAN, UnitOfTime.SECONDS).getValue());
    }


    @Test
    void calcTimeToFixBrokenBuildMean() {

        List<BuildOfAnalysis> buildsInfo = getSequenceOfBuildsWithFailure();

        Assertions.assertEquals(new BigDecimal("31000.0000"), processor.calcTimeToFixBrokenBuild(buildsInfo, StatisticalMeasure.MEAN, UnitOfTime.SECONDS).getValue());
    }


    @Test
    void calcTimeToFixBrokenBuildInHours() {

        List<BuildOfAnalysis> buildsInfo = getSequenceOfBuildsWithFailure();


        Assertions.assertEquals(new BigDecimal("1.0000"), processor.calcTimeToFixBrokenBuild(buildsInfo, StatisticalMeasure.MEDIAN, UnitOfTime.HOURS).getValue());
    }


    @Test
    void calcTimeToFixBrokenBuildInDays() {

        List<BuildOfAnalysis> buildsInfo = getSequenceOfBuildsWithFailure();


        Assertions.assertEquals(new BigDecimal("0.0417"), processor.calcTimeToFixBrokenBuild(buildsInfo, StatisticalMeasure.MEDIAN, UnitOfTime.DAYS).getValue());
    }


    /**
     * If there is not fail builds, in the period, the time to fix is ZERO
     */
    @Test
    void calcTimeToFixBrokenBuildOnlyPassed() {

        List<BuildOfAnalysis> buildsInfo = getSequenceOfBuildsWithOutFailure();

        Assertions.assertEquals(new BigDecimal("0"), processor.calcTimeToFixBrokenBuild(buildsInfo, StatisticalMeasure.MEDIAN, UnitOfTime.SECONDS).getValue());
    }


    /**
     * WHen All build fail, the time to fix is the total time of period
     */
    @Test
    void calcTimeToFixBrokenBuildOnlyFailure() {

        List<BuildOfAnalysis> buildsInfo = getSequenceOfBuildsOnlyFailure();


        // 58h or 208800 seconds has the whole period
        Assertions.assertEquals(new BigDecimal("208800"), processor.calcTimeToFixBrokenBuild(buildsInfo, StatisticalMeasure.MEDIAN, UnitOfTime.SECONDS).getValue());
    }


    /**
     * Test multi periods
     */
    @Test
    void calcTimeToFixBrokenBuildHistory() {

        List<BuildOfAnalysis> buildsInfo = getSequenceOfBuildsMultiMonths();

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 6, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 3, 30, 19, 0, 0);

        List<PeriodOfAnalysis> history = processor.calcTimeToFixBrokenBuildHistory(buildsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH, StatisticalMeasure.MEAN, UnitOfTime.HOURS);

        Assertions.assertEquals(new BigDecimal("8.6111")    , history.get(0).getValue());
        Assertions.assertEquals(new BigDecimal("4.5000"), history.get(1).getValue());
        Assertions.assertEquals(new BigDecimal("57.5000"), history.get(2).getValue());

    }



    //////////////////////////////////////////////////////////////////////////////////////////


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


    /**
     * Return builds broken during several months
     * @return
     */
    private List<BuildOfAnalysis> getSequenceOfBuildsMultiMonths() {

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

        /// next month  -> 4h and 30 min of fail

        BuildOfAnalysis b10 = new BuildOfAnalysis();
        b10.startedAt = LocalDateTime.of(2021, 2, 12, 11, 0, 0);
        b10.finishedAt = LocalDateTime.of(2021, 2, 12, 11, 0, 0);
        b10.state = "failed";

        BuildOfAnalysis b11 = new BuildOfAnalysis();
        b11.startedAt = LocalDateTime.of(2021, 2, 12, 13, 0, 0);
        b11.finishedAt = LocalDateTime.of(2021, 2, 12, 13, 0, 0);
        b11.state = "failed";

        BuildOfAnalysis b12 = new BuildOfAnalysis();
        b12.startedAt = LocalDateTime.of(2021, 2, 12, 15, 30, 0);
        b12.finishedAt = LocalDateTime.of(2021, 2, 12, 15, 30, 0);
        b12.state = "failed";

        /// next month = 33 hour and 30 minutes to fix the broken build

        BuildOfAnalysis b13 = new BuildOfAnalysis();
        b13.startedAt = LocalDateTime.of(2021, 3, 12, 11, 0, 0);
        b13.finishedAt = LocalDateTime.of(2021, 3, 12, 11, 0, 0);
        b13.state = "passed";

        BuildOfAnalysis b14 = new BuildOfAnalysis();
        b14.startedAt = LocalDateTime.of(2021, 3, 15, 9, 0, 0);
        b14.finishedAt = LocalDateTime.of(2021, 3, 15, 9, 0, 0);
        b14.state = "failed";

        BuildOfAnalysis b15 = new BuildOfAnalysis();
        b15.startedAt = LocalDateTime.of(2021, 3, 17, 18, 30, 0);
        b15.finishedAt = LocalDateTime.of(2021, 3, 17, 18, 30, 0);
        b15.state = "passed";

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
        buildsInfo.add(b11);
        buildsInfo.add(b12);
        buildsInfo.add(b13);
        buildsInfo.add(b14);
        buildsInfo.add(b15);
        return buildsInfo;
    }
}