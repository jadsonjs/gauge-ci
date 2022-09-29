package br.com.jadson.gaugeci.controllers;

import br.com.jadson.gaugeci.controllers.input.CommitsAnalysisInputData;
import br.com.jadson.gaugeci.controllers.input.CommitsAnalysisInputDataHistory;
import br.com.jadson.gaugeci.metrics.CommitsPerWeekDayProcessor;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/commit-per-week_day")
public class CommitsPerWeekDayController {

    @Autowired
    CommitsPerWeekDayProcessor processor;

    @ApiOperation(value = "Calculate the Commits Per Weekday CI sub-practice for multi periods of analysis")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the list of Commits Per Weekday CI sub-practices for each period of analysis"),
    })
    @PostMapping(path = "/history", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PeriodOfAnalysis>> calcCommitsPerWeekDayHistory(@RequestBody CommitsAnalysisInputDataHistory inputData) {

        return new ResponseEntity<>(processor.calcCommitsPerWeekDayHistory(inputData.commits, inputData.start, inputData.end,
                PeriodOfAnalysis.PERIOD.valueOf(inputData.period) ), HttpStatus.OK);
    }


    @ApiOperation(value = "Calculate the Commits Per Weekday CI sub-practice values")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the list of Commits Per Weekday CI sub-practices values"),
    })
    @PostMapping(path = "/values", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BigDecimal>> calcCommitsPerWeekDayValues(@RequestBody CommitsAnalysisInputData inputData) {

        return new ResponseEntity<>(processor.calcCommitsPerWeekDayValues(inputData.commits, inputData.start, inputData.end), HttpStatus.OK);
    }


    @ApiOperation(value = "Calculate the Commits Per Weekday CI sub-practice for specific period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the Commits Per Weekday CI sub-practices in the period"),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PeriodOfAnalysis> calcCommitsPerWeekDay(@RequestBody CommitsAnalysisInputData inputData) {
        return new ResponseEntity<>(processor.calcCommitsPerWeekDay(inputData.commits, inputData.start, inputData.end), HttpStatus.OK);
    }
}
