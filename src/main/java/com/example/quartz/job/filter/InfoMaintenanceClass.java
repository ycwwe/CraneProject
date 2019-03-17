package com.example.quartz.job.filter;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InfoMaintenanceClass {


    public static Map<String, String> addressAndIdMap = new HashMap<>();/*记录xcc的ip：port（value） 和 ID(key)*/
    public static Map<String, Integer> addressAndSuffixMap = new HashMap<>();/*记录注册到IOC容器中的comlink对象的IP地址+端口号(key) 和 注册到容器的comlink的后缀（value）*/

    private static final List<Integer> INSTANCEIDLIST;

    static {
        INSTANCEIDLIST = new ArrayList<>();
        INSTANCEIDLIST.add(1);
        INSTANCEIDLIST.add(2);
        INSTANCEIDLIST.add(3);
        INSTANCEIDLIST.add(4);
        INSTANCEIDLIST.add(5);
    }

    private static final List<Integer> CLASSIDLIST;

    static {
        CLASSIDLIST = new ArrayList<>();
        CLASSIDLIST.add(1);
        CLASSIDLIST.add(2);
        CLASSIDLIST.add(3);
        CLASSIDLIST.add(4);
        CLASSIDLIST.add(5);
        CLASSIDLIST.add(6);
        CLASSIDLIST.add(100);
        CLASSIDLIST.add(101);
        CLASSIDLIST.add(102);
        CLASSIDLIST.add(103);
        CLASSIDLIST.add(104);
        CLASSIDLIST.add(105);
        CLASSIDLIST.add(110);
        CLASSIDLIST.add(111);
        CLASSIDLIST.add(201);

    }

    private static final List<Integer> JOB_TLCMETOHDIDLIST;

    static {
        JOB_TLCMETOHDIDLIST = new ArrayList<>();
        JOB_TLCMETOHDIDLIST.add(1);
        JOB_TLCMETOHDIDLIST.add(2);
        JOB_TLCMETOHDIDLIST.add(3);
        JOB_TLCMETOHDIDLIST.add(4);
        JOB_TLCMETOHDIDLIST.add(5);
        JOB_TLCMETOHDIDLIST.add(6);
        JOB_TLCMETOHDIDLIST.add(7);
        JOB_TLCMETOHDIDLIST.add(8);
        JOB_TLCMETOHDIDLIST.add(9);
        JOB_TLCMETOHDIDLIST.add(10);
        JOB_TLCMETOHDIDLIST.add(11);
        JOB_TLCMETOHDIDLIST.add(12);
        JOB_TLCMETOHDIDLIST.add(13);
        JOB_TLCMETOHDIDLIST.add(14);

    }

    public static List<Integer> getBlockmapTlcmetohdidlist() {
        return BLOCKMAP_TLCMETOHDIDLIST;
    }

    private static final List<Integer> BLOCKMAP_TLCMETOHDIDLIST;

    static {
        BLOCKMAP_TLCMETOHDIDLIST = new ArrayList<>();
        BLOCKMAP_TLCMETOHDIDLIST.add(1);
        BLOCKMAP_TLCMETOHDIDLIST.add(2);
        BLOCKMAP_TLCMETOHDIDLIST.add(3);
        BLOCKMAP_TLCMETOHDIDLIST.add(4);
        BLOCKMAP_TLCMETOHDIDLIST.add(5);
    }

    private static final Map<Integer, String> INSTANCEIDMAP;
    private static final Map<Integer, String> CLASSIDMAP;

    static {
        INSTANCEIDMAP = new HashMap<Integer, String>();
        INSTANCEIDMAP.put(XJDInstanceEnum.HEADERCONFIRM.getClassId(), XJDInstanceEnum.HEADERCONFIRM.getClassName());
        INSTANCEIDMAP.put(XJDInstanceEnum.SYSTEM.getClassId(), XJDInstanceEnum.SYSTEM.getClassName());
        INSTANCEIDMAP.put(XJDInstanceEnum.BLOCKMAP_TLC.getClassId(), XJDInstanceEnum.BLOCKMAP_TLC.getClassName());
        INSTANCEIDMAP.put(XJDInstanceEnum.CRANE_TLC.getClassId(), XJDInstanceEnum.CRANE_TLC.getClassName());
        INSTANCEIDMAP.put(XJDInstanceEnum.JOB_TLC.getClassId(), XJDInstanceEnum.JOB_TLC.getClassName());
    }

    static {
        CLASSIDMAP = new HashMap<Integer, String>();
        CLASSIDMAP.put(XJDClassEnum.HEADERCONFIRM.getClassId(), XJDClassEnum.HEADERCONFIRM.getClassName());
        CLASSIDMAP.put(XJDClassEnum.BASE_CLASS.getClassId(), XJDClassEnum.BASE_CLASS.getClassName());
        CLASSIDMAP.put(XJDClassEnum.SYSTEM.getClassId(), XJDClassEnum.SYSTEM.getClassName());
        CLASSIDMAP.put(XJDClassEnum.JOB_TLC.getClassId(), XJDClassEnum.JOB_TLC.getClassName());
        CLASSIDMAP.put(XJDClassEnum.CRANE_TLC.getClassId(), XJDClassEnum.CRANE_TLC.getClassName());
        CLASSIDMAP.put(XJDClassEnum.BLOCKMAP_TLC.getClassId(), XJDClassEnum.BLOCKMAP_TLC.getClassName());

    }

    public static List getINSTANCEIDLIST() {
        return INSTANCEIDLIST;
    }

    public static List getCLASSIDLIST() {
        return CLASSIDLIST;
    }

    public static List getJobTlcmetohdidlist() {
        return JOB_TLCMETOHDIDLIST;
    }

    public static Map<Integer, String> getINSTANCEIDMAP() {
        return INSTANCEIDMAP;
    }

    public static Map<Integer, String> getCLASSIDMAP() {
        return CLASSIDMAP;
    }

    public static void main(String[] args) {
        System.out.println(INSTANCEIDLIST.contains(1));

    }
}
