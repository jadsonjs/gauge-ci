package br.com.jadson.gaugeci.utils;

import br.com.jadson.gaugeci.model.BuildOfAnalysis;
import br.com.jadson.gaugeci.model.CommitOfAnalysis;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class GaugePeriodOfAnalysisUtils {

    /**
     * Return build between dates
     *
     * @param builds
     * @param start
     * @param end
     * @return
     */
    public List<BuildOfAnalysis> getBuildOfPeriod(List<BuildOfAnalysis> builds, LocalDateTime start, LocalDateTime end) {

        List<BuildOfAnalysis> buildsOfPeriod = new ArrayList<>();

        for (BuildOfAnalysis build : builds) {

            if(build.startedAt != null) {

                if (build.startedAt.isAfter(start) && build.startedAt.isBefore(end)) { // this build if of this release
                    buildsOfPeriod.add(build);
                }
            }
        }

        return buildsOfPeriod;
    }

    /**
     * Return commits between dates
     *
     * @param commits
     * @param startRelease
     * @param endRelease
     * @return
     */
    public List<CommitOfAnalysis> getCommitsOfPeriod(List<CommitOfAnalysis> commits, LocalDateTime startRelease, LocalDateTime endRelease) {

        List<CommitOfAnalysis> commitsOfRelease = new ArrayList<>();

        for (CommitOfAnalysis commit : commits) {

            if(commit.date != null) {

                LocalDateTime commitDate = commit.date;

                if (commitDate.isAfter(startRelease) && commitDate.isBefore(endRelease)) { // this commit if of this period
                    commitsOfRelease.add(commit);
                }
            }
        }

        return commitsOfRelease;
    }

}
