package br.com.jadson.gaugeci.controllers.input;

import br.com.jadson.gaugeci.model.BuildOfAnalysis;

import java.time.LocalDateTime;
import java.util.List;

public class BuildsAnalysisInputDataHistory {

    public List<BuildOfAnalysis> builds;

    public LocalDateTime start;
    public LocalDateTime end;

    public String period;
    public String measure;
    public String unit;

}
