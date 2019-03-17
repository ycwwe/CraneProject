package com.example.quartz.job.filter;

public enum XCCInstanceEnum {
    HEADERCONFIRM("header_Confirm", 1), SYSTEM("system", 2), JOB_CRANE("job_Crane", 3), CRANE_CRANE("crane_Crane", 4), BLOCKMAP_CRANE("blockmap_Crane", 5);
    private String instanceName;
    private int instanceId;

    XCCInstanceEnum(String instanceName, int instanceId) {
        this.instanceName = instanceName;
        this.instanceId = instanceId;
    }

    public String getClassName() {
        return instanceName;
    }

    public int getClassId() {
        return instanceId;
    }

}
