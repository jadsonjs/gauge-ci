package br.com.jadson.gaugeci.model;

import java.time.LocalDateTime;
import java.util.List;

public class Analysis {

    public LocalDateTime start;
    public LocalDateTime end;
    public String period;
    public List<CommitOfAnalysis> commits;
}
