# gauge-ci

<img src="https://github.com/jadsonjs/gauge-ci/blob/master/gauge.png" width="800">

**Have you ever thought about the productivity and quality gains that certain CI practices can have on your project?**

These sub-practices emerged from our work that presents a study that evaluates the impact of five CI sub-practices concerning the productivity and quality of GitHub open-source projects.

Pre-print available: https://arxiv.org/abs/2208.02598

The supported CI sub-practices are:

   - **Build Duration**: It measures the duration of the build (build finished at timestamp - build started at timestamp).

   - **Build Activity**: It is a unit interval (i.e., a closed interval [0,1]) representing the rate of builds across days, i.e, if builds were made every day in the period of CAAnalysisInputData, the value would be 1. If builds were made in half of the days, the value would be 0.5. If there were no builds, the value would be 0.

   - **Build Health**: It is a unit interval representing the rate of build failures across days. If there were build failures every day, the value would be 0. if there were no build failures, the value would be 1.

   - **Time to Fix a Broken Build**: It consists of the median time in a period that builds remained broken. When a build breaks, we compute the time in seconds until the build returns to the "passed" status. If the CAAnalysisInputData period ends and the build did not return to the "passed" status, we consider the time since it was broken until the end of the period. When a period has no broken builds, the value would be 0.
   
   - **Commit Activity**: It is a unit interval representing the rate of commits across days. If commits were made every day in a period, the value would be 1. If commits were made in half of days the value would be 0.5. If there were no commits in a period the value would be 0.

   - **Commit Per Weekday**: Mean of absolute number of commits according to the week day of the analyzed period.

   - **Comments Per Change**: Mean or Median of the number of comments grouped by changes, normally, a change is a Pull Request when we are working at Github.


The coverage, related with "write automated developer tests" sub-practice, will not be supported because we already have specifics tools that calculate it value.

#### Change Logs:

- 1.0 - "Build Duration", "Build Activity", "Build Health", "Time to Fix a Broken Build", "Commit Activity" and "Commit Per Weekday" CI sub-practices implementation.
- 1.3 - "Comments Per Change" CI sub-practice implementation.


### Dependencies

    Java 11
    Gradle 7.5
    Junit 5.8.2

### How do I get set up?

#### From the source code:

Clone the project -> Import it as a gradle project on your IDE.


#### From the binary:

Gauge-CI has a binary distribution on **libs/gauge-ci-X.Y.Z-plain.jar** directory.

Include it on the classpath of your project. 


#### Run the application as a service:

Download the binary distribution on **libs/gauge-ci-X.Y.Z.jar** directory.

Run the command:

    java -jar -Dserver.port=808X gauge-ci.jar


#### Run the application at Docker:

Gauge is being published in Docker Hub, if you want to execute without need to install 
the JavaVM in your machine, you can just run the follow docker command:

    docker container run -d -p 808X:8080 jadsonjs/gauge-ci:vX.Y.Z


#### REST api documentation:

Execute the application and access the follow address, which will be shown to you a set of services available in the tool to calculate the CI sub-practices


    http://localhost:808X/swagger-ui/index.html



### How to use

Examples of how to use:

```

#############################################
#####           Embedded                #####
#############################################

# Commit Activity

        List<CommitOfAnalysis> commits = new ArrayList<>();

        CommitOfAnalysis c1 = new CommitOfAnalysis();
        c1.date =  LocalDateTime.parse("2021-01-01T12:00:00Z", formatter);
        CommitOfAnalysis c2 = new CommitOfAnalysis();
        c2.date =  LocalDateTime.parse("2021-01-02T04:00:00Z", formatter);
        CommitOfAnalysis c3 = new CommitOfAnalysis();
        c3.date =  LocalDateTime.parse("2021-01-03T05:00:00Z", formatter);
        CommitOfAnalysis c4 = new CommitOfAnalysis();
        c4.date =  LocalDateTime.parse("2021-01-04T06:00:00Z", formatter);
        CommitOfAnalysis c5 = new CommitOfAnalysis();
        c5.date =  LocalDateTime.parse("2021-01-05T07:00:00Z", formatter);

        commits.add(c1);
        commits.add(c2);
        commits.add(c3);
        commits.add(c4);
        commits.add(c5);

       PeriodOfAnalysis periodOfAnalysis = new CommitActivityProcessor()
            .calcCommitsActivity(commits, startReleaseDate, endReleaseDate);
            
       List<PeriodOfAnalysis> periodOfAnalysis = new CommitActivityProcessor()
            .calcCommitsActivityHistory(commits, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH);
       
       BigDecimal value = new CommitActivityProcessor()
            .calcCommitsActivityValues(commits, startReleaseDate, endReleaseDate);
            
# Time to Fix Broken Builds

        List<BuildOfAnalysis> buildsInfo = new ArrayList<>();

        BuildOfAnalysis b1 = new BuildOfAnalysis();
        b1.startedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.finishedAt = LocalDateTime.of(2021, 1, 10, 1, 0, 0);
        b1.state = "passed";

        BuildOfAnalysis b2 = new BuildOfAnalysis();
        b2.startedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 0, 0);
        b2.state = "failed";

        BuildOfAnalysis b3 = new BuildOfAnalysis();
        b3.startedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.finishedAt = LocalDateTime.of(2021, 1, 10, 2, 30, 0);
        b3.state = "passed";
        
        buildsInfo.add(b1);
        buildsInfo.add(b2);
        buildsInfo.add(b3);
        
        LocalDateTime startReleaseDate = LocalDateTime.of(2021, 1, 10, 0, 0, 0);
        LocalDateTime endReleaseDate = LocalDateTime.of(2021, 1, 15, 9, 0, 0);


        PeriodOfAnalysis periodOfAnalysis = new TimeToFixBrokenBuildProcessor().calcTimeToFixBrokenBuild(buildsInfo, startReleaseDate, endReleaseDate, 
               PeriodOfAnalysis.PERIOD.MONTH, StatisticalMeasure.MEAN, UnitOfTime.HOURS);
               
        PeriodOfAnalysis periodOfAnalysis = new TimeToFixBrokenBuildProcessor().calcTimeToFixBrokenBuildHistory(buildsInfo, StatisticalMeasure.MEAN, UnitOfTime.HOURS);
               
        PeriodOfAnalysis periodOfAnalysis = new TimeToFixBrokenBuildProcessor().calcTimeToFixBrokenBuildValues(buildsInfo, UnitOfTime.HOURS);              

      
#############################################
#####          As a service             #####
#############################################

# Commit Activity

      POST to localhost:8080/commit-activity
      POST to localhost:8080/commit-activity/history
      POST to localhost:8080/commit-activity/values
      HEADER Content-Type: application/json
body

        {
          "start": "2022-01-01T01:00:00.000",
          "end": "2022-01-05T10:00:00.000",
          "period": "MONTH",
          "commits": [
            {
              "date": "2022-01-05T01:00:00.000"
            },
            {
              "date": "2022-01-05T02:00:00.000"
            },
            {
              "date": "2022-01-05T03:00:00.000"
            },
            {
              "date": "2022-01-05T04:00:00.000"
            },
            {
              "date": "2022-01-05T05:00:00.000"
            }
          ]
        
        }

returns

        {
            "subPractice": "Commit Activity",
            "start": "2022-01-01 01:00:00",
            "end": "2022-01-05 10:00:00",
            "period": "MONTH",
            "value": 0.2000
        }
        
        
# Time to Fix Broken Build

      POST to localhost:8080/time-to-fix-broken-build
      POST to localhost:8080/time-to-fix-broken-build/history
      POST to localhost:8080/time-to-fix-broken-build/values
      HEADER Content-Type: application/json

body

        {
          "start": "2022-01-01T01:00:00.000",
          "end": "2022-01-05T10:00:00.000",
          "period": "MONTH",
          "measure": "MEAN",
          "unit": "HOURS",
          "builds": [
            {
              "startedAt": "2022-01-05T01:00:00.000",
              "finishedAt": "2022-01-05T01:30:00.000",
              "state": "passed"
            },
            {
              "startedAt": "2022-01-05T02:00:00.000",
              "finishedAt": "2022-01-05T02:30:00.000",
              "state": "failed"
            },
            {
              "startedAt": "2022-01-05T03:00:00.000",
              "finishedAt": "2022-01-05T03:30:00.000",
              "state": "passed"
            }
          ]
        
        }

returns

        {
            "subPractice": "Time To Fix Broken Build",
            "start": "2022-01-01 01:00:00",
            "end": "2022-01-05 10:00:00",
            "daysBetweenDate": 5,
            "period": "MONTH",
            "value": 1.0000
        }      

```

### Contribution guidelines

Be free to implement new CI sub-practices or correct bugs and submit pull requests. Since, you write a correlated Unit Test that prove that your implementation is correct.


#### Authors:

    Jadson Santos - jadsonjs@gmail.com