package br.com.jadson.gaugeci.controllers;

import br.com.jadson.gaugeci.controllers.input.CommentsPerChangeAnalysisInputData;
import br.com.jadson.gaugeci.controllers.input.CommentsPerChangeAnalysisInputDataHistory;
import br.com.jadson.gaugeci.controllers.input.CommentsPerChangeAnalysisInputDataValues;
import br.com.jadson.gaugeci.gauges.CommentsPerChangeGauge;
import br.com.jadson.gaugeci.model.PeriodOfAnalysis;
import br.com.jadson.gaugeci.model.StatisticalMeasure;
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
import java.util.Map;

@RestController
@RequestMapping(path = "/comments-per-change")
public class CommentsPerChangeController {

    @Autowired
    CommentsPerChangeGauge gauge;

    @ApiOperation(value = "Calculate the Comments Per Change CI sub-practice for multi periods of analysis")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the list of Comments Per Change CI sub-practices for each period of analysis"),
    })
    @PostMapping(path = "/history", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PeriodOfAnalysis>> calcCommentsPerChangeHistory(@RequestBody CommentsPerChangeAnalysisInputDataHistory inputData) {

        return new ResponseEntity<>(gauge.calcCommentsPerChangeHistory(inputData.comments, inputData.start, inputData.end, PeriodOfAnalysis.PERIOD.valueOf(inputData.period), StatisticalMeasure.valueOf(inputData.measure) ), HttpStatus.OK);
    }


    @ApiOperation(value = "Calculate the Comments Per Change CI sub-practice for specific period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the Comments Per Change CI sub-practices in the period"),
    })
    @PostMapping(path = "/values", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<Long, BigDecimal>> calcCommentsPerChangeValues(@RequestBody CommentsPerChangeAnalysisInputDataValues inputData) {
        return new ResponseEntity<>(gauge.calcCommentsPerChangeValues(inputData.comments), HttpStatus.OK);
    }

    @ApiOperation(value = "Calculate the Comments Per Change CI sub-practice for specific period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the Comments Per Change CI sub-practices in the period"),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PeriodOfAnalysis> calcCommentsPerChange(@RequestBody CommentsPerChangeAnalysisInputData inputData) {
        return new ResponseEntity<>(gauge.calcCommentsPerChange(inputData.comments, inputData.start, inputData.end, StatisticalMeasure.valueOf(inputData.measure)), HttpStatus.OK);
    }

}
