package com.example.quartz.job.filter;

import com.example.business.blockmap.Container;
import com.example.dao.entity.Armgjobq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/*作业类型判断并生成相应的作业队列，pollingDB调用*/
public class JobKindFilter {
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private static Logger loggError = LoggerFactory.getLogger("demo_error");
    private static boolean ISSEND = false;
    /*卸船列表*/
    private List<Map> DISCHARGR_LIST;
    /*装船列表*/
    private List<Map> LOAD_LIST;
    /*倒箱列表*/
    private List<Map> SHUFFLE_LIST;

    /*对传入的List<ArmgjobqEntity>进行作业类型判断，并生成相应的作业list*/
    private void allKindFilter(List<Armgjobq> listOfJob) {
        loggerInfo.info("XJD正在判断从数据库取出的记录所属的作业类型.......");
        DISCHARGR_LIST = new ArrayList();
        LOAD_LIST = new ArrayList<>();
        SHUFFLE_LIST = new ArrayList<>();
        for (Armgjobq armgjobqEntity : listOfJob) {
            if (armgjobqEntity.getChassisType() != null && armgjobqEntity.getChassisNo() != null && armgjobqEntity.getRfidNo() != null) {
                /*堆场收箱：卸船，外集卡送箱*/
                if (armgjobqEntity.getBayFrom() != null && armgjobqEntity.getBlockFrom() != null && armgjobqEntity.getRowFrom() != null && armgjobqEntity.getTierFrom() == null
                        && armgjobqEntity.getBayTo() != null && armgjobqEntity.getBlockTo() != null && armgjobqEntity.getRowTo() != null && armgjobqEntity.getTierTo() != null) {
                    loggerInfo.info("计算单条卸船信息中..........");
                    Map pickUpChassisMap = pickUpChassisJobProducer(armgjobqEntity);
                    DISCHARGR_LIST.add(pickUpChassisMap);
                } else {/*堆场发箱：装船，提箱*/
                    loggerInfo.info("计算单条装船信息中..........");
                    LOAD_LIST.add(setDownChassisJobProducer(armgjobqEntity));
                }
            } else {/*堆场交换箱作业*/
                loggerInfo.info("计算单条堆场换箱信息中..........");
                SHUFFLE_LIST.add(blockShuffleJobProducer(armgjobqEntity));
            }
        }
        loggerInfo.info("最终形成的卸船作业的队列：大小：" + DISCHARGR_LIST.size() + "最终形成的装船作业的队列：大小：" + LOAD_LIST.size() + "最终形成的卸船作业的队列：大小：" + SHUFFLE_LIST.size());

    }

    /*
    首先对集装箱的ISO编码进行解析
    然后返回生成的单条卸船作业指令：MAP
    */
    private Map pickUpChassisJobProducer(Armgjobq armgjobqEntity) {
        /*这里的getCntrType()是获取集装箱ISo*/
        String cntrISO = armgjobqEntity.getCntrType();
        Container container = new Container(cntrISO);
        int clearancePickUp = 2;
        int clearanceSetDown = 2;
        int cntrsize = container.getCntrsize();
        int cntrhigh = container.getCntrhigh();
        int cntrtype = container.getCntrtype();
      /*  char size=cntrType.charAt(0);
        int high=cntrType.charAt(1);
        char type=cntrType.charAt(2);
        switch(size){
            case '2':
                cntrsize=1;
                break;
            case '4' :
                cntrsize=2;
                break;
            case 'L':
                cntrsize=3;
                break;
        }
        switch (high){
            case 2:
                cntrhigh=1;
                break;
            case 5:
                cntrhigh=2;
                break;
        }
        switch(type){
            case'G':
                cntrtype=1;
                break;
            case'U':
                cntrtype=2;
                break;
            case'T':
                cntrtype=3;
                break;
            case'P':
                cntrtype=4;
                break;
            case 'R':
                cntrtype=5;
                break;
        }*/
        Map dischargemap = new HashMap<>();
        dischargemap.put("blockPickUp", armgjobqEntity.getBlockFrom());
        dischargemap.put("chassisRowPickUp", armgjobqEntity.getRowFrom().toString());
        dischargemap.put("chassisBayPickUp", armgjobqEntity.getBayFrom().toString());
        dischargemap.put("positionOnChassisPickUp", armgjobqEntity.getPosOnChassis().toString());
        dischargemap.put("chassisTypePickUp", armgjobqEntity.getChassisType().toString());
        dischargemap.put("chassisNoPickUp", armgjobqEntity.getChassisNo().toString());
        dischargemap.put("RFID", armgjobqEntity.getRfidNo());
        dischargemap.put("contNumberPickUp", armgjobqEntity.getCntrNo());
        dischargemap.put("weightPickUp", armgjobqEntity.getCntrWeight().toString());
        dischargemap.put("doorDirPickUp", armgjobqEntity.getDoorDirFrom().toString());
        dischargemap.put("contSizePickUp", String.valueOf(cntrsize));
        dischargemap.put("contHeightPickUp", String.valueOf(cntrhigh));
        dischargemap.put("contTypePickUp", String.valueOf(cntrtype));
        dischargemap.put("landingTypePickUp", armgjobqEntity.getPickupLanding().toString());
        dischargemap.put("clearancePickUp", String.valueOf(clearancePickUp));
        dischargemap.put("blockSetDown", armgjobqEntity.getBlockTo());
        dischargemap.put("rowSetDown", armgjobqEntity.getRowTo().toString());
        dischargemap.put("baySetDown", armgjobqEntity.getBayTo().toString());
        dischargemap.put("tierNumberSetDown", armgjobqEntity.getTierTo().toString());
        dischargemap.put("doorReqDirSetDown", armgjobqEntity.getDoorDirTo().toString());
        dischargemap.put("landingTypeSetDown", armgjobqEntity.getSetdownLanding().toString());
        dischargemap.put("clearanceSetDown", String.valueOf(clearanceSetDown));
        dischargemap.put("jobId", String.valueOf(armgjobqEntity.getJobOrderSeqMs()));
        dischargemap.put("armgId", armgjobqEntity.getArmgId());
        loggerInfo.info("生成的单条卸船Map" + dischargemap);
        return dischargemap;
    }

    /*装船作业MAp*/
    private Map setDownChassisJobProducer(Armgjobq armgjobqEntity) {
        String cntrISO = armgjobqEntity.getCntrType();
        Container container = new Container(cntrISO);
        int clearancePickUp = 2;
        int clearanceSetDown = 2;
        int cntrsize = container.getCntrsize();
        Map loadmap = new HashMap<>();
        loadmap.put("blockPickUp", armgjobqEntity.getBlockFrom());
        loadmap.put("rowPickUp", armgjobqEntity.getRowFrom().toString());
        loadmap.put("bayPickUp", armgjobqEntity.getBayFrom().toString());
        loadmap.put("tierNumPickUp", armgjobqEntity.getTierFrom().toString());
        loadmap.put("contSizePickUp", String.valueOf(cntrsize));
        loadmap.put("landingTypePickUp", armgjobqEntity.getPickupLanding().toString());
        loadmap.put("clearancePickUp", String.valueOf(clearancePickUp));
        loadmap.put("blockSetDown", armgjobqEntity.getBlockTo());
        loadmap.put("chassisRowSetDown", armgjobqEntity.getRowTo().toString());
        loadmap.put("chassisBaySetDown", armgjobqEntity.getBayTo().toString());
        loadmap.put("positionOnChassisSetDown", armgjobqEntity.getPosOnChassis().toString());
        loadmap.put("chassisTypeSetDown", armgjobqEntity.getChassisType().toString());
        loadmap.put("chassisNoSetDown", armgjobqEntity.getChassisNo().toString());
        loadmap.put("RFID", armgjobqEntity.getRfidNo());
        loadmap.put("contNumberPickUp", armgjobqEntity.getCntrNo());
        ;
        loadmap.put("doorReqDirSetDown", armgjobqEntity.getDoorDirTo().toString());
        loadmap.put("landingTypeSetDown", armgjobqEntity.getSetdownLanding().toString());
        loadmap.put("clearanceSetDown", String.valueOf(clearanceSetDown));
        loadmap.put("jobId", String.valueOf(armgjobqEntity.getJobOrderSeqMs()));
        loadmap.put("armgId", armgjobqEntity.getArmgId());
        loggerInfo.info("生成的单条装船Map" + loadmap);
        return loadmap;
    }

    private Map blockShuffleJobProducer(Armgjobq armgjobqEntity) {
        String cntrISO = armgjobqEntity.getCntrType();
        Container container = new Container(cntrISO);
        int clearancePickUp = 2;
        int clearanceSetDown = 2;
        int cntrsize = container.getCntrsize();
        Map shufflemap = new HashMap<>();
        shufflemap.put("blockPickUp", armgjobqEntity.getBlockFrom());
        shufflemap.put("rowPickUp", armgjobqEntity.getRowFrom().toString());
        shufflemap.put("bayPickUp", armgjobqEntity.getBayFrom().toString());
        shufflemap.put("tierNumPickUp", armgjobqEntity.getTierFrom().toString());
        shufflemap.put("contSizePickUp", String.valueOf(cntrsize));
        shufflemap.put("landingTypePickUp", armgjobqEntity.getPickupLanding().toString());
        shufflemap.put("clearancePickUp", String.valueOf(clearancePickUp));
        shufflemap.put("blockSetDown", armgjobqEntity.getBlockTo());
        shufflemap.put("rowSetDown", armgjobqEntity.getRowTo().toString());
        shufflemap.put("baySetDown", armgjobqEntity.getBayTo().toString());
        shufflemap.put("tierNumberSetDown", armgjobqEntity.getTierTo().toString());
        shufflemap.put("doorReqDirSetDown", armgjobqEntity.getDoorDirTo().toString());
        shufflemap.put("landingTypeSetDown", armgjobqEntity.getSetdownLanding().toString());
        shufflemap.put("clearanceSetDown", String.valueOf(clearanceSetDown));
        shufflemap.put("jobId", String.valueOf(armgjobqEntity.getJobOrderSeqMs()));
        shufflemap.put("armgId", armgjobqEntity.getArmgId());
        loggerInfo.info("生成的单条堆场换箱Map" + shufflemap);
        return shufflemap;
    }

    /*
    外部调用这个函数获得生成的卸船作业队列
    */
    public void start(List<Armgjobq> listOfJob) {
        this.allKindFilter(listOfJob);
    }

    public List<Map> getDISCHARGR_LIST() {
        return DISCHARGR_LIST;
    }

    public List<Map> getLOAD_LIST() {
        return LOAD_LIST;
    }

    public List<Map> getSHUFFLE_LIST() {
        return SHUFFLE_LIST;
    }

    public void setISSEND(boolean ISSEND) {
        JobKindFilter.ISSEND = ISSEND;
    }

    /*清空DISCHARGR_LIST*/
    public void removeALLDISCHARGR_LIST() {
        if (!DISCHARGR_LIST.isEmpty() && ISSEND) {
            for (Map m : DISCHARGR_LIST) {
                DISCHARGR_LIST.remove(m);
            }
            ISSEND = false;
            loggerInfo.info(" DISCHARGR_LIST中的所有元素（map）已被删除！");
        } else {
            loggerInfo.info(" DISCHARGR_LIST已经为空，无需删除！");
        }
    }
}
