package com.example.quartz.job.filter;

import com.example.business.blockmap.Container;
import com.example.dao.entity.Armgjobq;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/*作业类型判断并生成相应的作业队列，pollingDB调用*/
public class JobKindFilter {
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private  static Logger loggError = LoggerFactory.getLogger("demo_error");
    private  static boolean ISSEND=false;
    /*卸船列表*/
    private  List<Map> DISCHARGR_LIST ;
    /*装船列表*/
    private  List<Map> LOAD_LIST ;
    /*倒箱列表*/
    private  List<Map> SHUFFLE_LIST ;
    /*private  List LOAD_LIST;
    private List SHUFFLE_LIST;*/

    /*对传入的List<ArmgjobqEntity>进行作业类型判断，并生成相应的作业list*/
    private void allKindFilter(List<Armgjobq> listOfJob){
        loggerInfo.info("XJD正在判断从数据库取出的记录所属的作业类型.......");
        DISCHARGR_LIST = new ArrayList();
        LOAD_LIST=new ArrayList<>();
        SHUFFLE_LIST=new ArrayList<>();
        for(Armgjobq armgjobqEntity:listOfJob){
            if(armgjobqEntity.getChassisType()!=null&&armgjobqEntity.getChassisNo()!=null&&armgjobqEntity.getRfidNo()!=null)
            {
                /*堆场收箱：卸船，外集卡送箱*/
                if(armgjobqEntity.getBayFrom()!=null&&armgjobqEntity.getBlockFrom()!=null&&armgjobqEntity.getRowFrom()!=null&&armgjobqEntity.getTierFrom()==null
                    &&armgjobqEntity.getBayTo()!=null&&armgjobqEntity.getBlockTo()!=null&&armgjobqEntity.getRowTo()!=null&&armgjobqEntity.getTierTo()!=null)
                {
                    loggerInfo.info("计算单条卸船信息中..........");
                    Map pickUpChassisMap =pickUpChassisJobProducer(armgjobqEntity);
                    DISCHARGR_LIST.add(pickUpChassisMap);
                }else{/*堆场发箱：装船，提箱*/}
            }else{/*堆场交换箱作业*/}
        }
        loggerInfo.info("最终形成的卸船作业的队列：大小："+ DISCHARGR_LIST.size());
    }

    /*
    首先对集装箱的ISO编码进行解析
    然后返回生成的单条卸船作业指令：MAP
    */
    private Map pickUpChassisJobProducer(Armgjobq armgjobqEntity){
        /*这里的getCntrType()是获取集装箱ISo*/
        String cntrISO=armgjobqEntity.getCntrType();
        Container container=new Container( cntrISO);
        int clearancePickUp=2;
        int clearanceSetDown=2;
        int cntrsize = container.getCntrsize();
        int cntrhigh=container.getCntrhigh();
        int cntrtype=container.getCntrtype();
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
        Map map =new HashMap<>();
        map.put("blockPickUp",armgjobqEntity.getBlockFrom());
        map.put("chassisRowPickUp",armgjobqEntity.getRowFrom().toString());
        map.put("chassisBayPickUp",armgjobqEntity.getBayFrom().toString());
        map.put("positionOnChassisPickUp",armgjobqEntity.getPosOnChassis().toString());
        map.put("chassisTypePickUp",armgjobqEntity.getChassisType().toString());
        map.put("chassisNoPickUp",armgjobqEntity.getChassisNo().toString());
        map.put("RFID",armgjobqEntity.getRfidNo());
        map.put("contNumberPickUp",armgjobqEntity.getCntrNo());
        map.put("weightPickUp",armgjobqEntity.getCntrWeight().toString());
        map.put("doorDirPickUp",armgjobqEntity.getDoorDirFrom().toString());
        map.put("contSizePickUp",String.valueOf(cntrsize));
        map.put("contHeightPickUp",String.valueOf(cntrhigh));
        map.put("contTypePickUp",String.valueOf(cntrtype));
        map.put("landingTypePickUp",armgjobqEntity.getPickupLanding().toString());
        map.put("clearancePickUp",String.valueOf(clearancePickUp));
        map.put("blockSetDown",armgjobqEntity.getBlockTo());
        map.put("rowSetDown",armgjobqEntity.getRowTo().toString());
        map.put("baySetDown",armgjobqEntity.getBayTo().toString());
        map.put("tierNumberSetDown",armgjobqEntity.getTierTo().toString());
        map.put("doorReqDirSetDown",armgjobqEntity.getDoorDirTo().toString());
        map.put("landingTypeSetDown",armgjobqEntity.getSetdownLanding().toString());
        map.put("clearanceSetDown",String.valueOf(clearanceSetDown));
        map.put("jobId",String.valueOf(armgjobqEntity.getJobOrderSeqMs()));
        map.put("armgId",armgjobqEntity.getArmgId());
        loggerInfo.info("生成的Map"+map);
        return map;
    }
    /*装船作业MAp*/
    private Map setDownChassisJobProducer(Armgjobq armgjobqEntity){
        return  null;
    }
    /*
    外部调用这个函数获得生成的卸船作业队列
    */
    public List<Map> getDISCHARGR_LIST(List<Armgjobq> listOfJob) {
        allKindFilter(listOfJob);
        return DISCHARGR_LIST;
    }

    public  void setISSEND(boolean ISSEND) {
        JobKindFilter.ISSEND = ISSEND;
    }
/*清空DISCHARGR_LIST*/
    public void removeALLDISCHARGR_LIST(){
        if(!DISCHARGR_LIST.isEmpty()&&ISSEND){
            for(Map m:DISCHARGR_LIST){
                DISCHARGR_LIST.remove(m);
            }
            ISSEND=false;
            loggerInfo.info(" DISCHARGR_LIST中的所有元素（map）已被删除！");
        }else{
            loggerInfo.info(" DISCHARGR_LIST已经为空，无需删除！");
        }
    }
}
