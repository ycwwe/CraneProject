package com.example.quartz.job.filter;

public enum JobTLCMethodEnum {
    JOBACCEPTEDREPORT("jobAcceptedReport", 1), JOBSTARTEDREPORT("jobStartedReport", 2), JOBDONEREPORT("jobDoneReport", 3),
    WAITINGREPORT("waitingReport", 4);

    private String methodName;
    private int methodId;

    JobTLCMethodEnum(String methodName, int methodId) {
        this.methodId = methodId;
        this.methodName = methodName;
    }


    public String getMethodName() {
        return methodName;
    }

    public int getMethodId() {
        return methodId;
    }
}
