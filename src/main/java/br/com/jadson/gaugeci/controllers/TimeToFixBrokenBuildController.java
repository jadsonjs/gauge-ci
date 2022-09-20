package br.com.jadson.gaugeci.controllers;

import br.com.jadson.gaugeci.controllers.input.BuildsAnalysisInputData;
import br.com.jadson.gaugeci.metrics.TimeToFixBrokenBuildProcessor;
import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import br.com.jadson.gaugeci.model.StatisticalMeasure;
import br.com.jadson.gaugeci.model.UnitOfTime;
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

@RestController
@RequestMapping(path = "/time-to-fix-broken-build")
public class TimeToFixBrokenBuildController {

    @Autowired
    TimeToFixBrokenBuildProcessor processor;

    @ApiOperation(value = "Calculate the Time to Fix a Broken Build CI sub-practice for multi periods of analysis")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the list of Time to Fix a Broken Build CI sub-practices for each period of analysis"),
    })
    @PostMapping(path = "/history", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PeriodOfAnalysis>> calcTimeToFixHistory(@RequestBody BuildsAnalysisInputData inputData) {

        return new ResponseEntity<>(processor.calcTimeToFixBrokenBuildHistory(inputData.builds,
                inputData.start, inputData.end,
                PeriodOfAnalysis.PERIOD.valueOf(inputData.period),
                StatisticalMeasure.valueOf(inputData.measure),
                UnitOfTime.valueOf(inputData.unit)), HttpStatus.OK);
    }


    @ApiOperation(value = "Calculate the Time to Fix a Broken Build CI sub-practice for specific period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the Time to Fix a Broken Build CI sub-practices in the period"),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PeriodOfAnalysis> calcTimeToFix(@RequestBody BuildsAnalysisInputData inputData) {
        return new ResponseEntity<>(processor.calcTimeToFixBrokenBuild(inputData.builds, inputData.start, inputData.end,
                PeriodOfAnalysis.PERIOD.valueOf(inputData.period),
                StatisticalMeasure.valueOf(inputData.measure),
                UnitOfTime.valueOf(inputData.unit)), HttpStatus.OK);
    }
}
