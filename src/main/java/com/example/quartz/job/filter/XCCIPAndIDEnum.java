package com.example.quartz.job.filter;

public enum XCCIPAndIDEnum {
    XCC_ONE("127.0.0.1", 8086, "A01"), XCC_TOW("192.168.0.101", 8086, "A02");
    private String ip;
    private String id;
    private int portg;

    public int getPortg() {
        return portg;
    }

    public String getIp() {
        return ip;
    }

    public String getId() {
        return id;
    }


    XCCIPAndIDEnum(String ip, int port, String id) {
        this.ip = ip;
        this.id = id;
    }

}
