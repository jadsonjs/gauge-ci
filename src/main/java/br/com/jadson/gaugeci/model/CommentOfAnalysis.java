package br.com.jadson.gaugeci.model;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class CommentOfAnalysis {


    public long commentId;

    @NotEmpty
    public LocalDateTime createdAt;

    /**
     * The number of change where this comment was made.
     */
    @NotEmpty
    public Long changeNumber;
}
