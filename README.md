# gauge-ci

<img src="https://github.com/jadsonjs/gauge-ci/blob/master/gauge.png" width="800">

Project to calc CI sub-practices values

   - **Build Duration**: It measures the duration of the build (build finished at timestamp - build started at timestamp).

   - **Build Activity**: It is a unit interval (i.e., a closed interval [0,1]) representing the rate of builds across days, i.e, if builds were made every day in the period of CAAnalysisInputData, the value would be 1. If builds were made in half of the days, the value would be 0.5. If there were no builds, the value would be 0.

   - **Build Health**: It is a unit interval representing the rate of build failures across days. If there were build failures every day, the value would be 0. if there were no build failures, the value would be 1.

   - **Time to Fix a Broken Build**: It consists of the median time in a period that builds remained broken. When a build breaks, we compute the time in seconds until the build returns to the "passed" status. If the CAAnalysisInputData period ends and the build did not return to the "passed" status, we consider the time since it was broken until the end of the period. When a period has no broken builds, the value would be 0.
   
   - **Commit Activity**: It is a unit interval representing the rate of commits across days. If commits were made every day in a period, the value would be 1. If commits were made in half of days the value would be 0.5. If there were no commits in a period the value would be 0.

