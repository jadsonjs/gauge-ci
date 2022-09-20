package br.com.jadson.gaugeci.controllers;

import br.com.jadson.gaugeci.controllers.input.BuildsAnalysisInputData;
import br.com.jadson.gaugeci.metrics.BuildHealthProcessor;
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

@RestController
@RequestMapping(path = "/build-health")
public class BuildHealthController {

    @Autowired
    BuildHealthProcessor processor;

    @ApiOperation(value = "Calculate the Build Health CI sub-practice for multi periods of analysis")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the list of Build Health CI sub-practices for each period of analysis"),
    })
    @PostMapping(path = "/history", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PeriodOfAnalysis>> calcBuildHealthHistory(@RequestBody BuildsAnalysisInputData inputData) {

        return new ResponseEntity<>(processor.calcBuildHealthHistory(inputData.builds, inputData.start, inputData.end,
                PeriodOfAnalysis.PERIOD.valueOf(inputData.period) ), HttpStatus.OK);
    }


    @ApiOperation(value = "Calculate the Build Health CI sub-practice for specific period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the Build Health CI sub-practices in the period"),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PeriodOfAnalysis> calcBuildHealth(@RequestBody BuildsAnalysisInputData inputData) {
        return new ResponseEntity<>(processor.calcBuildHealth(inputData.builds, inputData.start, inputData.end,
                PeriodOfAnalysis.PERIOD.valueOf(inputData.period)), HttpStatus.OK);
    }
}