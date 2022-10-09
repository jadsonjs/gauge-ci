package br.com.jadson.gaugeci.controllers.input;

import br.com.jadson.gaugeci.model.CommentOfAnalysis;

import java.time.LocalDateTime;
import java.util.List;

public class CommentsPerChangeAnalysisInputDataHistory {

    public List<CommentOfAnalysis> comments;

    public LocalDateTime start;
    public LocalDateTime end;

    public String period;
    public String measure;
}
