package com.example.quartz.job.filter;

public enum JobCraneMethodEnum {
    BLOCKSHUFFLE("blockShuffle",1),PICKUPCHASSIS("pickUpChassis",2),SETDOWNCHASSIS("setDownChassis",3);
    private String methodName;
    private int methodId;
    private JobCraneMethodEnum(String instanceName , int instanceId) {
        this.methodName = instanceName;
        this.methodId=instanceId;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getMethodId() {
        return methodId;
    }
}
