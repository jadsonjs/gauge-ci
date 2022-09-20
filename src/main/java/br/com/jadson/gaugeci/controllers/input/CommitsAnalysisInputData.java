package br.com.jadson.gaugeci.controllers.input;

import br.com.jadson.gaugeci.model.CommitOfAnalysis;

import java.time.LocalDateTime;
import java.util.List;

public class CommitsAnalysisInputData {

    public LocalDateTime start;
    public LocalDateTime end;
    public String period;
    public String unit;
    public List<CommitOfAnalysis> commits;
}
