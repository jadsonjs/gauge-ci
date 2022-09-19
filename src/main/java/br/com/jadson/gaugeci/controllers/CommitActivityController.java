package br.com.jadson.gaugeci.controllers;

import br.com.jadson.gaugeci.metrics.CommitActivityProcessor;
import br.com.jadson.gaugeci.model.Analysis;
import br.com.jadson.gaugeci.model.CommitOfAnalysis;
import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This controller allow call the commit activity using REST endpoints.
 * So you can call using another language that not Java.
 *
 * @author jadson santos - jadsonjs@gmail.com
 */
@RestController
@RequestMapping(path = "/commit-activity")
public class CommitActivityController {

    @Autowired
    CommitActivityProcessor commitActivityProcessor;

    @ApiOperation(value = "Calculate the Commit Activity CI sub-practice")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the list of Commit Activity CI sub-practices for each period of analysis"),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PeriodOfAnalysis>> calcCommitsActivity(@RequestBody Analysis analysis) {

        for (CommitOfAnalysis c : analysis.commits){
            System.out.println(c.date);
        }

        System.out.println(analysis.start);

        System.out.println(analysis.end);

        System.out.println(analysis.period);


        return new ResponseEntity<>(commitActivityProcessor.calcCommitsActivity(analysis.commits, analysis.start, analysis.end, PeriodOfAnalysis.PERIOD.valueOf(analysis.period)), HttpStatus.OK);
    }


}
