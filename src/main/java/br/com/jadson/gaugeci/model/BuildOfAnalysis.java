package br.com.jadson.gaugeci.model;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class BuildOfAnalysis {

    /**
     * Date of the build started.
     */
    @NotEmpty
    public LocalDateTime startedAt;

    /**
     * Date of the build finished.
     */
    @NotEmpty
    public LocalDateTime finishedAt;

    /**
     * Stage of the build:  "failed", "passed" etc...
     */
    public String state;
}
