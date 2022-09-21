# gauge-ci

<img src="https://github.com/jadsonjs/gauge-ci/blob/master/gauge.png" width="800">

Project to calc CI sub-practices values

   - **Build Duration**: It measures the duration of the build (build finished at timestamp - build started at timestamp).

   - **Build Activity**: It is a unit interval (i.e., a closed interval [0,1]) representing the rate of builds across days, i.e, if builds were made every day in the period of CAAnalysisInputData, the value would be 1. If builds were made in half of the days, the value would be 0.5. If there were no builds, the value would be 0.

   - **Build Health**: It is a unit interval representing the rate of build failures across days. If there were build failures every day, the value would be 0. if there were no build failures, the value would be 1.

   - **Time to Fix a Broken Build**: It consists of the median time in a period that builds remained broken. When a build breaks, we compute the time in seconds until the build returns to the "passed" status. If the CAAnalysisInputData period ends and the build did not return to the "passed" status, we consider the time since it was broken until the end of the period. When a period has no broken builds, the value would be 0.
   
   - **Commit Activity**: It is a unit interval representing the rate of commits across days. If commits were made every day in a period, the value would be 1. If commits were made in half of days the value would be 0.5. If there were no commits in a period the value would be 0.

   - **Commit Per Weekday**: Mean of absolute number of commits according to the week day of the analyzed period.


#### Change Logs:

- 1.0.0 - "Build Duration", "Build Activity", "Build Health", "Time to Fix a Broken Build", "Commit Activity" and "Commit Per Weekday" CI subpracties implementation.


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

    java -jar -Dserver.port=8080 gauge-ci.jar

Access:

    http://localhost:8080/swagger-ui/index.html

Which will be shown to you a set of services available in the tool to calculate the CI sub-practices

#### Run the application at Docker:

Gauge is being published in Docker Hub, if you want to execute without need to install 
the JavaVM in your machine, you can just run the follow docker command:

    docker container run -d -p 808X:8080 jadsonjs/gauge-ci:vX.Y.Z


### How to use

Examples of how to use:

```

#############################################
#####           Embedded                #####
#############################################

# Commit Activity

        List<CommitOfAnalysis> commits = new ArrayList<>();

        CommitOfAnalysis c1 = new CommitOfAnalysis();
        c1.date =  LocalDateTime.parse("2021-01-01T12:00:00Z", formatter); //format.parse("2021-01-01T12:00:00Z");
        CommitOfAnalysis c2 = new CommitOfAnalysis();
        c2.date =  LocalDateTime.parse("2021-01-02T04:00:00Z", formatter); //format.parse("2021-01-02T04:00:00Z");
        CommitOfAnalysis c3 = new CommitOfAnalysis();
        c3.date =  LocalDateTime.parse("2021-01-03T05:00:00Z", formatter); //format.parse("2021-01-03T05:00:00Z");
        CommitOfAnalysis c4 = new CommitOfAnalysis();
        c4.date =  LocalDateTime.parse("2021-01-04T06:00:00Z", formatter); //format.parse("2021-01-04T06:00:00Z");
        CommitOfAnalysis c5 = new CommitOfAnalysis();
        c5.date =  LocalDateTime.parse("2021-01-05T07:00:00Z", formatter); //format.parse("2021-01-05T07:00:00Z");

        commits.add(c1);
        commits.add(c2);
        commits.add(c3);
        commits.add(c4);
        commits.add(c5);

       PeriodOfAnalysis periodOfAnalysis = new CommitActivityProcessor()
            .calcCommitsActivity(commits, startReleaseDate, endReleaseDate, PeriodOfAnalysis.PERIOD.MONTH);

#############################################
#####          As a service             #####
#############################################

# Commit Activity

      POST to localhost:8080/commit-activity

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

```

### Contribution guidelines

Be free to implement new CI sub-practices or correct bugs and submit pull requests. Since, you write a correlated Unit Test that prove that your implementation is correct.


#### Authors:

    Jadson Santos - jadsonjs@gmail.com