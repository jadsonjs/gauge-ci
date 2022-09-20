package br.com.jadson.gaugeci.controllers;

import br.com.jadson.gaugeci.controllers.input.CommitsAnalysisInputData;
import br.com.jadson.gaugeci.metrics.CommitActivityProcessor;
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
    CommitActivityProcessor processor;

    @ApiOperation(value = "Calculate the Commit Activity CI sub-practice for multi periods of analysis")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the list of Commit Activity CI sub-practices for each period of analysis"),
    })
    @PostMapping(path = "/history", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PeriodOfAnalysis>> calcCommitsActivityHistory(@RequestBody CommitsAnalysisInputData inputData) {

        return new ResponseEntity<>(processor.calcCommitsActivityHistory(inputData.commits, inputData.start, inputData.end, PeriodOfAnalysis.PERIOD.valueOf(inputData.period)), HttpStatus.OK);
    }


    @ApiOperation(value = "Calculate the Commit Activity CI sub-practice for specific period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the Commit Activity CI sub-practices in the period"),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PeriodOfAnalysis> calcCommitsActivity(@RequestBody CommitsAnalysisInputData inputData) {
        return new ResponseEntity<>(processor.calcCommitsActivity(inputData.commits, inputData.start, inputData.end, PeriodOfAnalysis.PERIOD.valueOf(inputData.period)), HttpStatus.OK);
    }


}
