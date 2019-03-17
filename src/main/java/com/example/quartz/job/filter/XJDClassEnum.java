package com.example.quartz.job.filter;

public enum XJDClassEnum {
    HEADERCONFIRM("header_Confirm", 1), SYSTEM("system", 3), JOB_TLC("job_TLC", 101), CRANE_TLC("crane_TLC", 103), BLOCKMAP_TLC("blockmap_TLC", 105), BASE_CLASS("base_Class", 2);
    private String className;
    private int classId;

    XJDClassEnum(String className, int classId) {
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
