package com.example.quartz.job.filter;

public enum XCCClassEnum {
    HEADERCONFIRM("header_Confirm", 1), BASE_CLASS("Base_Class", 2), SYSTEM("system", 3), JOB_CRANE("job_Crane", 100), CRANE_CRANE("crane_Crane", 102), BLOCKMAP_CRANE("blockmap_TLC", 104);
    private String className;
    private int classId;

    XCCClassEnum(String className, int classId) {
        this.className = className;
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public int getClassId() {
        return classId;
    }
}
