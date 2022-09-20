package br.com.jadson.gaugeci.metrics;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CommitActivityProcessorTest {

    @Spy
    GaugeDateUtils dateUnit;

    @Spy
    GaugeMathUtils mathUtil;

    @InjectMocks
    CommitActivityProcessor processor;

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void calcCommitsActivityMultiPeriods() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        List<CommitOfAnalysis> commitsInfo = new ArrayList<>();

        CommitOfAnalysis c1 = new CommitOfAnalysis();
        c1.date = LocalDateTime.parse("2021-01-01T03:00:00Z", formatter); // format.parse("2021-01-01T03:00:00Z");
        CommitOfAnalysis c2 = new CommitOfAnalysis();
        c2.date = LocalDateTime.parse("2021-01-02T04:00:00Z", formatter); //format.parse("2021-01-02T04:00:00Z");
        CommitOfAnalysis c3 = new CommitOfAnalysis();
        c3.date = LocalDateTime.parse("2021-01-03T05:00:00Z", formatter); //format.parse("2021-01-03T05:00:00Z");
        CommitOfAnalysis c4 = new CommitOfAnalysis();
        c4.date = LocalDateTime.parse("2021-01-04T06:00:00Z", formatter); //format.parse("2021-01-04T06:00:00Z");
        CommitOfAnalysis c5 = new CommitOfAnalysis();
        c5.date = LocalDateTime.parse("2021-01-05T07:00:00Z", formatter); //format.parse("2021-01-05T07:00:00Z");

        CommitOfAnalysis c6 = new CommitOfAnalysis();
        c6.date = LocalDateTime.parse("2021-05-10T10:24:30Z", formatter); //format.parse("2021-05-10T10:24:30Z");

        commitsInfo.add(c1);
        commitsInfo.add(c2);
        commitsInfo.add(c3);
        commitsInfo.add(c4);
        commitsInfo.add(c5);
        commitsInfo.add(c6);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 1, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 10, 5, 9, 0, 0);

        List<PeriodOfAnalysis> periodOfAnalysis = processor.calcCommitsActivityHistory(commitsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH);
        for (PeriodOfAnalysis p : periodOfAnalysis){
            System.out.println(p);
        }
        Assertions.assertEquals(10, periodOfAnalysis.size());
        Assertions.assertEquals(new BigDecimal("0.1563"), periodOfAnalysis.get(0).getValue());   // 5 days of commit / 32 days
        Assertions.assertEquals(new BigDecimal("0.0323"), periodOfAnalysis.get(4).getValue());   // 1 day of commit / 31 days
    }

    @Test
    void calcCommitsActivity100Percent() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


        List<CommitOfAnalysis> commitsInfo = new ArrayList<>();

        CommitOfAnalysis c1 = new CommitOfAnalysis();
        //c1.date = format.parse("2021-01-01T03:00:00Z");
        c1.date = LocalDateTime.parse("2021-01-01T03:00:00Z", formatter);
        CommitOfAnalysis c2 = new CommitOfAnalysis();
        c2.date = LocalDateTime.parse("2021-01-02T04:00:00Z", formatter); // format.parse("2021-01-02T04:00:00Z");
        CommitOfAnalysis c3 = new CommitOfAnalysis();
        c3.date = LocalDateTime.parse("2021-01-03T05:00:00Z", formatter); // format.parse("2021-01-03T05:00:00Z");
        CommitOfAnalysis c4 = new CommitOfAnalysis();
        c4.date = LocalDateTime.parse("2021-01-04T06:00:00Z", formatter); // format.parse("2021-01-04T06:00:00Z");
        CommitOfAnalysis c5 = new CommitOfAnalysis();
        c5.date = LocalDateTime.parse("2021-01-05T07:00:00Z", formatter); //format.parse("2021-01-05T07:00:00Z");

        commitsInfo.add(c1);
        commitsInfo.add(c2);
        commitsInfo.add(c3);
        commitsInfo.add(c4);
        commitsInfo.add(c5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 1, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 5, 9, 0, 0);

        PeriodOfAnalysis periodOfAnalysis = processor.calcCommitsActivity(commitsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH);
        Assertions.assertEquals(new BigDecimal("1.0000"), periodOfAnalysis.getValue());

    }

    @Test
    void calcCommitsActivity100PercentStartAfter() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        List<CommitOfAnalysis> commitsInfo = new ArrayList<>();

        CommitOfAnalysis c1 = new CommitOfAnalysis();
        c1.date = LocalDateTime.parse("2021-01-01T12:00:00Z", formatter); // format.parse("2021-01-01T12:00:00Z");
        CommitOfAnalysis c2 = new CommitOfAnalysis();
        c2.date = LocalDateTime.parse("2021-01-02T04:00:00Z", formatter); // format.parse("2021-01-02T04:00:00Z");
        CommitOfAnalysis c3 = new CommitOfAnalysis();
        c3.date = LocalDateTime.parse("2021-01-03T05:00:00Z", formatter); // format.parse("2021-01-03T05:00:00Z");
        CommitOfAnalysis c4 = new CommitOfAnalysis();
        c4.date = LocalDateTime.parse("2021-01-04T06:00:00Z", formatter); // format.parse("2021-01-04T06:00:00Z");
        CommitOfAnalysis c5 = new CommitOfAnalysis();
        c5.date = LocalDateTime.parse("2021-01-05T07:00:00Z", formatter); // format.parse("2021-01-05T07:00:00Z");

        commitsInfo.add(c1);
        commitsInfo.add(c2);
        commitsInfo.add(c3);
        commitsInfo.add(c4);
        commitsInfo.add(c5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 5, 9, 0, 0);

        PeriodOfAnalysis periodOfAnalysis = processor.calcCommitsActivity(commitsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH);

        Assertions.assertEquals(new BigDecimal("1.0000"), periodOfAnalysis.getValue());

    }



    @Test
    void calcCommitsActivity50Percent() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


        List<CommitOfAnalysis> commitsInfo = new ArrayList<>();

        CommitOfAnalysis c1 = new CommitOfAnalysis();
        c1.date = LocalDateTime.parse("2021-01-02T01:00:00Z", formatter); //  format.parse("2021-01-02T01:00:00Z");

        CommitOfAnalysis c2 = new CommitOfAnalysis();
        c2.date = LocalDateTime.parse("2021-01-03T01:00:00Z", formatter); //  format.parse("2021-01-03T01:00:00Z");

        CommitOfAnalysis c3 = new CommitOfAnalysis();
        c3.date = LocalDateTime.parse("2021-01-04T01:00:00Z", formatter); //  format.parse("2021-01-04T01:00:00Z");

        CommitOfAnalysis c4 = new CommitOfAnalysis();
        c4.date = LocalDateTime.parse("2021-01-06T01:00:00Z", formatter); // format.parse("2021-01-06T01:00:00Z");

        CommitOfAnalysis c5 = new CommitOfAnalysis();
        c5.date = LocalDateTime.parse("2021-01-07T01:00:00Z", formatter); // format.parse("2021-01-07T01:00:00Z");

        commitsInfo.add(c1);
        commitsInfo.add(c2);
        commitsInfo.add(c3);
        commitsInfo.add(c4);
        commitsInfo.add(c5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 10, 9, 0, 0);

        PeriodOfAnalysis periodOfAnalysis = processor.calcCommitsActivity(commitsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH);

        Assertions.assertEquals(new BigDecimal("0.5000"), periodOfAnalysis.getValue());

    }


    @Test
    void calcCommitsActivity00Percent() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


        List<CommitOfAnalysis> commitsInfo = new ArrayList<>();

        CommitOfAnalysis c1 = new CommitOfAnalysis();
        c1.date = LocalDateTime.parse("2021-01-12T01:00:00Z", formatter); // format.parse("2021-01-12T01:00:00Z");

        CommitOfAnalysis c2 = new CommitOfAnalysis();
        c2.date = LocalDateTime.parse("2021-01-13T01:00:00Z", formatter); // format.parse("2021-01-13T01:00:00Z");

        CommitOfAnalysis c3 = new CommitOfAnalysis();
        c3.date = LocalDateTime.parse("2021-01-14T01:00:00Z", formatter); // format.parse("2021-01-14T01:00:00Z");

        CommitOfAnalysis c4 = new CommitOfAnalysis();
        c4.date = LocalDateTime.parse("2021-01-16T01:00:00Z", formatter); // format.parse("2021-01-16T01:00:00Z");

        CommitOfAnalysis c5 = new CommitOfAnalysis();
        c5.date = LocalDateTime.parse("2021-01-17T01:00:00Z", formatter); // format.parse("2021-01-17T01:00:00Z");

        commitsInfo.add(c1);
        commitsInfo.add(c2);
        commitsInfo.add(c3);
        commitsInfo.add(c4);
        commitsInfo.add(c5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 10, 9, 0, 0);

        PeriodOfAnalysis periodOfAnalysis = processor.calcCommitsActivity(commitsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH);

        Assertions.assertEquals(new BigDecimal("0.0000"), periodOfAnalysis.getValue());

    }


    /**
     * Test in project where the spring @Autowired do not work
     * @throws ParseException
     */
    @Test
    void calcCommitsActivity100PercentNotSpringProjects() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


        List<CommitOfAnalysis> commitsInfo = new ArrayList<>();

        CommitOfAnalysis c1 = new CommitOfAnalysis();
        c1.date =  LocalDateTime.parse("2021-01-01T12:00:00Z", formatter); //format.parse("2021-01-01T12:00:00Z");
        CommitOfAnalysis c2 = new CommitOfAnalysis();
        c2.date =  LocalDateTime.parse("2021-01-02T04:00:00Z", formatter); //format.parse("2021-01-02T04:00:00Z");
        CommitOfAnalysis c3 = new CommitOfAnalysis();
        c3.date =  LocalDateTime.parse("2021-01-03T05:00:00Z", formatter); //format.parse("2021-01-03T05:00:00Z");
        CommitOfAnalysis c4 = new CommitOfAnalysis();
        c4.date =  LocalDateTime.parse("2021-01-04T06:00:00Z", formatter); //format.parse("2021-01-04T06:00:00Z");
        CommitOfAnalysis c5 = new CommitOfAnalysis();
        c5.date =  LocalDateTime.parse("2021-01-05T07:00:00Z", formatter); //format.parse("2021-01-05T07:00:00Z");

        commitsInfo.add(c1);
        commitsInfo.add(c2);
        commitsInfo.add(c3);
        commitsInfo.add(c4);
        commitsInfo.add(c5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 5, 9, 0, 0);

        PeriodOfAnalysis periodOfAnalysis = new CommitActivityProcessor().calcCommitsActivity(commitsInfo, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH);

        Assertions.assertEquals(new BigDecimal("1.0000"), periodOfAnalysis.getValue());

    }
}