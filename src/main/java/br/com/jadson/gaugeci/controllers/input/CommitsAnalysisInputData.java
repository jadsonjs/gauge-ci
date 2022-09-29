package br.com.jadson.gaugeci.controllers.input;

import br.com.jadson.gaugeci.model.CommitOfAnalysis;

import java.time.LocalDateTime;
import java.util.List;

public class CommitsAnalysisInputData {

    public List<CommitOfAnalysis> commits;

    public LocalDateTime start;
    public LocalDateTime end;

}
