package com.example.dao.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.dao.mapper.*;
import com.example.dao.entity.*;
import org.springframework.stereotype.Service;

@Service("xjdDaoService")
public class XjdDaoServiceImpl implements XjdDaoService {


    /*private static ApplicationContext applicationContext =
            new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
    private static ArmgjobqMapper armgjobqMapper =
            (ArmgjobqMapper) applicationContext.getBean("armgjobqMapper");
    private static ArmgstatusMapper armgstatusMapper =
            (ArmgstatusMapper) applicationContext.getBean("armgstatusMapper");
    private static ArmgslotxjdMapper armgslotxjdMapper =
            (ArmgslotxjdMapper) applicationContext.getBean("armgslotxjdMapper");*/
    @Autowired
    private ArmgjobqMapper armgjobqMapper;
    @Autowired
    private ArmgstatusMapper armgstatusMapper;
    @Autowired
    private ArmgslotxjdMapper armgslotxjdMapper;

    @Override
    public String findArmgIdByJobOrderSeqMs(long jobOrderSeqMs) {
        System.out.println(armgjobqMapper.selectByPrimaryKey(jobOrderSeqMs));
        return null;
    }

    @Override
    public List<Armgjobq> findAllByJobCreateTimeAfter(Date date) {
        ArmgjobqExample armgjobqExample = new ArmgjobqExample();
        ArmgjobqExample.Criteria criteria = armgjobqExample.createCriteria();
        /*criteria.andJobCreateTimeGreaterThanOrEqualTo(date);*/
        criteria.andJobCreateTimeGreaterThan(date);
        criteria.andJobStatusQfrEqualTo("W");
        return armgjobqMapper.selectByExample(armgjobqExample);
    }

    @Override
    public int upadteJobAccepted(long jobOrderSeqMs, String jobStatusQfr, Date jobAssignedTime) {
        Armgjobq armgjobq = new Armgjobq();
        armgjobq.setJobOrderSeqMs(jobOrderSeqMs);
        armgjobq.setJobStatusQfr(jobStatusQfr);
        armgjobq.setJobAssignedTime(jobAssignedTime);
        if (armgjobqMapper == null) {
            System.out.println("指针维欧空");
        }
        return armgjobqMapper.updateByPrimaryKeySelective(armgjobq);
    }

    @Override
    public int upadteJobStartedFirst(long jobOrderSeqMs, String jobStatusQfr, short chassisArriInd,
                                     short chassisArriTimeout, String armgId, short waitingType, short autoStatus) {
        Armgjobq armgjobq = new Armgjobq();
        armgjobq.setJobOrderSeqMs(jobOrderSeqMs);
        armgjobq.setJobStatusQfr(jobStatusQfr);
        armgjobq.setChassisArriInd(chassisArriInd);
        armgjobq.setChassisArriTimeout(chassisArriTimeout);
        Armgstatus armgstatus = new Armgstatus();
        armgstatus.setArmgId(armgId);
        armgstatus.setWaitingType(waitingType);
        armgstatus.setAutoStatus(autoStatus);
        return armgjobqMapper.updateByPrimaryKeySelective(armgjobq) + armgstatusMapper.updateByPrimaryKeySelective(armgstatus);
    }

    @Override
    public int upadteJobStartedSecond(String armgId, short autoStatus) {
        Armgstatus armgstatus = new Armgstatus();
        armgstatus.setArmgId(armgId);
        armgstatus.setAutoStatus(autoStatus);
        return armgstatusMapper.updateByPrimaryKeySelective(armgstatus);
    }

    @Override
    public int upadteJobDoneFirst(long jobOrderSeqMs, String pickupInd, Date pickupTime) {
        Armgjobq armgjobq = new Armgjobq();
        armgjobq.setJobOrderSeqMs(jobOrderSeqMs);
        armgjobq.setPickupInd(pickupInd);
        armgjobq.setPickupTime(pickupTime);
        return armgjobqMapper.updateByPrimaryKeySelective(armgjobq);
    }

    @Override
    public int upadteJobDoneSecond(long jobOrderSeqMs, String jobStatusQfr, String jobFinishInd, Date jobFinishTime,
                                   int timeElapsed) {
        Armgjobq armgjobq = new Armgjobq();
        armgjobq.setJobOrderSeqMs(jobOrderSeqMs);
        armgjobq.setJobStatusQfr(jobStatusQfr);
        armgjobq.setJobFinishInd(jobFinishInd);
        armgjobq.setJobFinishTime(jobFinishTime);
        armgjobq.setTimeElapsed(timeElapsed);
        return armgjobqMapper.updateByPrimaryKeySelective(armgjobq);
    }

    @Override
    public int updateWaitingStatus(long jobOrderSeqMs, short chassisArriInd, short chassisArriTimeout, String armgId,
                                   short waitingType, short autoStatus) {
        Armgjobq armgjobq = new Armgjobq();
        armgjobq.setJobOrderSeqMs(jobOrderSeqMs);
        armgjobq.setChassisArriInd(chassisArriInd);
        armgjobq.setChassisArriTimeout(chassisArriTimeout);
        Armgstatus armgstatus = new Armgstatus();
        armgstatus.setArmgId(armgId);
        armgstatus.setWaitingType(waitingType);
        armgstatus.setAutoStatus(autoStatus);
        return armgjobqMapper.updateByPrimaryKeySelective(armgjobq) + armgstatusMapper.updateByPrimaryKeySelective(armgstatus);
    }

    @Override
    public int updateSlotXJDStackStatus(String blockNo, short bayNo, short rowNo, short tierNo, String cntrNo,
                                        String cntrType, int cntrWeight, short doorDirect, int slotStatus, Date modifyDate, String updateInd) {
        ArmgslotxjdExample armgslotxjdExample = new ArmgslotxjdExample();
        ArmgslotxjdExample.Criteria criteria = armgslotxjdExample.createCriteria();
        criteria.andBlockNoEqualTo(blockNo);
        criteria.andBayNoEqualTo(bayNo);
        criteria.andRowNoEqualTo(rowNo);
        criteria.andTierNoEqualTo(tierNo);
        Armgslotxjd armgslotxjd = new Armgslotxjd();
        armgslotxjd.setCntrNo(cntrNo);
        armgslotxjd.setCntrType(cntrType);
        armgslotxjd.setCntrWeight(cntrWeight);
        armgslotxjd.setDoorDirect(doorDirect);
        armgslotxjd.setSlotStatus(slotStatus);
        armgslotxjd.setModifyDate(modifyDate);
        armgslotxjd.setUpdateInd(updateInd);
        return armgslotxjdMapper.updateByExampleSelective(armgslotxjd, armgslotxjdExample);
    }

    @Override
    public List<Armgjobq> findAllJob() {
        ArmgjobqExample armgjobqExample = new ArmgjobqExample();
        ArmgjobqExample.Criteria criteria = armgjobqExample.createCriteria();
        criteria.andJobOrderSeqMsIsNotNull();
        criteria.andJobStatusQfrEqualTo("W");

        if (armgjobqMapper == null) {
            System.out.println("@@@指针维欧空");
        }
        return armgjobqMapper.selectByExample(armgjobqExample);
    }

    public static void main(String[] args) throws ParseException {
        XjdDaoServiceImpl method = new XjdDaoServiceImpl();
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/


//		//1.测试方法List<Armgjobq> findAllByJobCreateTimeAfter(Date date);
//		List<Armgjobq> res = new ArrayList<Armgjobq>();
//		String date="2018-08-30 12:00:00";
//		res = method.findAllByJobCreateTimeAfter(sdf.parse(date));
//		for (Armgjobq armgjobq : res) {
//			System.out.println(armgjobq.getJobOrderSeqMs());
//		}
//		System.out.println("JOBQ表里符合创建时间在"+date+"之后的共有"+res.size()+"条作业记录");


//		//2.测试方法int upadteJobAccepted(long jobOrderSeqMs, String jobStatusQfr, Date jobAssignedTime);
//		int num1;
//		long jobOrderSeqMs1=1L;
//		String jobStatusQfr="A";
//		String jobAssignedTime="2018-11-19 12:00:00";
//		num1=method.upadteJobAccepted(jobOrderSeqMs1, jobStatusQfr, sdf.parse(jobAssignedTime));
//		System.out.println("更新条数：\t"+num1);


//		//3.测试方法int updateWaitingStatus(long jobOrderSeqMs,short chassisArriInd,short chassisArriTimeout,String armgId,
//		// short waitingType, short autoStatus);
//		int num2;
//		long jobOrderSeqMs2=2L;
//		short chassisArriInd=0;//拖车还未到达
//		short chassisArriTimeout=0;//起重机仍在超时时间内等待
//		String armgId="A07";//起重机ID
//		short waitingType=7;//起重机等待类型：起重机在等待拖车
//		short autoStatus=3;//起重机自动化状态
//		num2=method.updateWaitingStatus(jobOrderSeqMs2, chassisArriInd, chassisArriTimeout, armgId, waitingType, autoStatus);
//		System.out.println("更新条数：\t"+num2);


        //4.测试方法List<Armgjobq> findAllByJob();
//		res=method.findAllJob();
//		for (Armgjobq armgjobq : res) {
//			System.out.println(armgjobq.getJobOrderSeqMs()+"作业");
//		}
//		System.out.println("共"+res.size());


        //5.测试方法String findArmgIdByJobOrderSeqMs(long jobOrderSeqMs);
//		System.out.println(method.findArmgIdByJobOrderSeqMs(3));
        System.out.println(method.findAllJob());

    }

}
