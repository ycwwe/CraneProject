package com.example.quartz.job.filter;

public enum BlockMapTLCEnum {
    GETSTACK("getStack", 1), WRITESTACK("writeStack", 2);
    private String methodName;
    private int methodId;

    private BlockMapTLCEnum(String instanceName, int instanceId) {
        this.methodName = instanceName;
        this.methodId = instanceId;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getMethodId() {
        return methodId;
    }
}
