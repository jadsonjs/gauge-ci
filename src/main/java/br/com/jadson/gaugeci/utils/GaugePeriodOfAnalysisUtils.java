package br.com.jadson.gaugeci.utils;

import br.com.jadson.gaugeci.model.BuildOfAnalysis;
import br.com.jadson.gaugeci.model.CommentOfAnalysis;
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

                if ( ( build.startedAt.isAfter(start) && build.startedAt.isBefore(end) ) || build.startedAt.equals(start) || build.startedAt.equals(end)) { // this build if of this release
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

                if ( ( commitDate.isAfter(startRelease) && commitDate.isBefore(endRelease) ) || commitDate.equals(startRelease) || commitDate.equals(endRelease) ) { // this commit if of this period
                    commitsOfRelease.add(commit);
                }
            }
        }

        return commitsOfRelease;
    }

    public List<CommentOfAnalysis> getCommentsOfPeriod(List<CommentOfAnalysis> comments, LocalDateTime start, LocalDateTime end) {

        List<CommentOfAnalysis> commentsOfPeriod = new ArrayList<>();

        for (CommentOfAnalysis comment : comments) {

            if(comment.createdAt != null) {

                if ( ( comment.createdAt.isAfter(start) && comment.createdAt.isBefore(end) ) || comment.createdAt.equals(start) || comment.createdAt.equals(end)) { // this build if of this release
                    commentsOfPeriod.add(comment);
                }
            }
        }

        return commentsOfPeriod;
    }
}
