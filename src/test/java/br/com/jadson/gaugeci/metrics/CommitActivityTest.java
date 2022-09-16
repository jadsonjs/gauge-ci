package br.com.jadson.gaugeci.metrics;

import br.com.jadson.gaugeci.model.CommitMetric;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class CommitActivityTest {


    @Test
    void calcCommitsActivity100Percent() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        List<CommitMetric> commitsInfo = new ArrayList<>();

        CommitMetric c1 = new CommitMetric();
       
        
        c1.date = format.parse("2021-01-01T01:00:00Z");

        System.out.println(c1.date);

        CommitMetric c2 = new CommitMetric();
        c2.date = format.parse("2021-01-02T01:00:00Z");

        CommitMetric c3 = new CommitMetric();
        c3.date = format.parse("2021-01-03T01:00:00Z");

        CommitMetric c4 = new CommitMetric();
        c4.date = format.parse("2021-01-04T01:00:00Z");

        CommitMetric c5 = new CommitMetric();
        c5.date = format.parse("2021-01-05T01:00:00Z");

        commitsInfo.add(c1);
        commitsInfo.add(c2);
        commitsInfo.add(c3);
        commitsInfo.add(c4);
        commitsInfo.add(c5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 5, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("1.0000000000"), new CommitActivity().calcCommitsActivity(commitsInfo, startReleaseDate, endReleaseDate));

    }


    @Test
    void calcCommitsActivity50Percent() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        List<CommitMetric> commitsInfo = new ArrayList<>();

        CommitMetric c1 = new CommitMetric();
        c1.date = format.parse("2021-01-02T01:00:00Z");

        System.out.println(c1.date);

        CommitMetric c2 = new CommitMetric();
        c2.date = format.parse("2021-01-03T01:00:00Z");

        CommitMetric c3 = new CommitMetric();
        c3.date = format.parse("2021-01-04T01:00:00Z");

        CommitMetric c4 = new CommitMetric();
        c4.date = format.parse("2021-01-06T01:00:00Z");

        CommitMetric c5 = new CommitMetric();
        c5.date = format.parse("2021-01-07T01:00:00Z");

        commitsInfo.add(c1);
        commitsInfo.add(c2);
        commitsInfo.add(c3);
        commitsInfo.add(c4);
        commitsInfo.add(c5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 10, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("0.5000000000"), new CommitActivity().calcCommitsActivity(commitsInfo, startReleaseDate, endReleaseDate));

    }


    @Test
    void calcCommitsActivity00Percent() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        List<CommitMetric> commitsInfo = new ArrayList<>();

        CommitMetric c1 = new CommitMetric();
        c1.date = format.parse("2021-01-12T01:00:00Z");

        System.out.println(c1.date);

        CommitMetric c2 = new CommitMetric();
        c2.date = format.parse("2021-01-13T01:00:00Z");

        CommitMetric c3 = new CommitMetric();
        c3.date = format.parse("2021-01-14T01:00:00Z");

        CommitMetric c4 = new CommitMetric();
        c4.date = format.parse("2021-01-16T01:00:00Z");

        CommitMetric c5 = new CommitMetric();
        c5.date = format.parse("2021-01-17T01:00:00Z");

        commitsInfo.add(c1);
        commitsInfo.add(c2);
        commitsInfo.add(c3);
        commitsInfo.add(c4);
        commitsInfo.add(c5);

        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 10, 9, 0, 0);

        Assertions.assertEquals(new BigDecimal("0.0000000000"), new CommitActivity().calcCommitsActivity(commitsInfo, startReleaseDate, endReleaseDate));

    }
}