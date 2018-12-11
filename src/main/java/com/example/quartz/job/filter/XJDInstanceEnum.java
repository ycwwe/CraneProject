package com.example.quartz.job.filter;

public enum XJDInstanceEnum {
    HEADERCONFIRM("header_Confirm",1),SYSTEM("system",2),JOB_TLC("job_TLC",3),CRANE_TLC("crane_TLC",4),BLOCKMAP_TLC("blockmap_TLC",5);
    private String instanceName;
    private int instanceId;
    XJDInstanceEnum(String instanceName , int instanceId) {
        this.instanceName = instanceName;
        this.instanceId=instanceId;
    }

    public String getClassName() {
        return instanceName;
    }

    public int getClassId() {
        return instanceId;
    }

}
