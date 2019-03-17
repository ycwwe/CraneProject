package com.example.dao.service;

import java.util.Date;
import java.util.List;

import com.example.dao.entity.Armgjobq;

/**
 * XJD�ӿں���
 *
 * @author ���ں�
 */
public interface XjdDaoService {
    /**
     * 获取abbtable11数据库里的ARMGJOBQ表的ARMG的编号信息
     *
     * @param jobOrderSeqMs 根据JOBQ表的作业索引号字段JOB_ORDER_SEQ_MS来查找对应的ARMG编号
     * @return 返回找到了ARMG的ID
     * @version 2018.11.22
     */
    String findArmgIdByJobOrderSeqMs(long jobOrderSeqMs);

    /**
     * 获取abbtable11数据库里的ARMGJOBQ表的所有作业信息
     *
     * @return 返回查询得到的作业数据，以对象封装一条记录，以List封装多条记录
     * @version 2018.11.22
     */
    List<Armgjobq> findAllJob();

    /**
     * pollingDB()�����findAllByJobCreateTimeAfter����
     * ����ARMGJOBQ_MODIFY_DATE�ֶ�
     * ����ȡabbtable11���ݿ����ARMGJOBQ�����ҵ��Ϣ��ĳ��ʱ���(date)֮�����ҵ
     *
     * @param date �����������Ϊʱ�����ͣ���ӦARMGJOBQ�����ARMGJOBQ_MODIFY_DATE�ֶ�(��ҵ�޸�ʱ��)
     * @return ���ز�ѯ�õ�����ҵ���ݣ��Զ����װһ����¼����List��װ������¼
     * @version 2018.11.11
     */
    List<Armgjobq> findAllByJobCreateTimeAfter(Date date);

    /**
     * jobAcceptedRepor()�����upadteJobAccepted����
     * ����ARMGJOBQ_JOB_ORDER_SEQ_MS�ֶ�
     * ������abbtable11���ݿ����ARMGJOBQ�����ҵ��Ϣ��������ҵ״̬��Ϣ����ҵ���ŵ����ػ���ҵ���е�ʱ����Ϣ��
     *
     * @param jobOrderSeqMs   ARMGJOBQ_JOB_ORDER_SEQ_MS��ҵ������
     * @param jobStatusQfr    ARMGJOBQ_JOB_STATUS_QFR��ҵ״̬
     * @param jobAssignedTime ARMGJOBQ_JOB_ASSIGNED_TIME��ҵ���ŵ����ػ���ҵ���е�ʱ��
     * @return ���ظ��¼�¼������
     * @version 2018.11.11
     */
    int upadteJobAccepted(long jobOrderSeqMs, String jobStatusQfr, Date jobAssignedTime);

    /**
     * jobStartedReport()�����upadteJobStartedFirst����
     * ����ARMGJOBQ_JOB_ORDER_SEQ_MS�ֶ�
     * ������abbtable11���ݿ����ARMGJOBQ�����ҵ��Ϣ��������ҵ״̬��Ϣ���ϳ��Ƿ񵽴�ϳ��ȴ��Ƿ�ʱ��
     * ����ARMGSTATUS_ARMG_ID�ֶ�
     * ������abbtable11���ݿ����ARMGSTATUS������ػ���Ϣ���������ػ��ȴ����͡����ػ��Զ���״̬��
     *
     * @param jobOrderSeqMs      ARMGJOBQ_JOB_ORDER_SEQ_MS��ҵ������
     * @param jobStatusQfr       ARMGJOBQ_JOB_STATUS_QFR��ҵ״̬��Ϣ
     * @param chassisArriInd     ARMGJOBQ_ARRI_IND�ϳ��Ƿ񵽴�
     * @param chassisArriTimeout ARMGJOBQ_CHASSIS_ARRI_TIMEOUT�ϳ��ȴ��Ƿ�ʱ
     * @param armgId             ARMGSTATUS_ARMG_ID���ػ����
     * @param waitingType        ARMGSTATUS_WAITING_TYPE���ػ��ȴ�����
     * @param autoStatus         ARMGSTATUS_AUTO_STATUS���ػ��Զ���״̬
     * @return ���ظ��¼�¼������
     * @version 2018.11.11
     */
    int upadteJobStartedFirst(long jobOrderSeqMs, String jobStatusQfr, short chassisArriInd,
                              short chassisArriTimeout, String armgId, short waitingType, short autoStatus);

    /**
     * jobStartedReport()�����upadteJobStartedSecond����
     * ����ARMGSTATUS_ARMG_ID�ֶ�
     * ������abbtable11���ݿ����ARMGSTATUS������ػ���Ϣ���������ػ��Զ���״̬��
     *
     * @param armgId     ARMGJOBQ_ARMGID���ػ����
     * @param autoStatus ARMGSTATUS_AUTO_STATUS���ػ��Զ���״̬
     * @return ���ظ��¼�¼������
     * @version 2018.11.11
     */
    int upadteJobStartedSecond(String armgId, short autoStatus);

    /**
     * jobDoneReport()�����upadteJobDoneFirst����
     * ����ARMGJOBQ_JOB_ORDER_SEQ_MS�ֶ�
     * ������abbtable11���ݿ����ARMGJOBQ�����ҵ��Ϣ������ץȡִ�б�־��ץȡ��ʱ��㣩
     *
     * @param jobOrderSeqMs ARMGJOBQ_JOB_ORDER_SEQ_MS��ҵ������
     * @param pickupInd     PICKUP_INDץȡִ�б�־
     * @param pickupTime    PICKUP_TIMEץȡ��ʱ���
     * @return ���ظ��¼�¼������
     * @version 2018.11.11
     */
    int upadteJobDoneFirst(long jobOrderSeqMs, String pickupInd, Date pickupTime);

    /**
     * jobDoneReport()�����upadteJobDoneSecond����
     * ����ARMGJOBQ_JOB_ORDER_SEQ_MS�ֶ�
     * ������abbtable11���ݿ����ARMGJOBQ�����ҵ��Ϣ��������ҵ״̬��Ϣ����ҵ������־����ҵ������ʱ��㡢��ҵ��ɺ�ʱ��
     *
     * @param jobOrderSeqMs ARMGJOBQ_JOB_ORDER_SEQ_MS��ҵ������
     * @param jobStatusQfr  ARMGJOBQ_JOB_STATUS_QFR��ҵ״̬��Ϣ
     * @param jobFinishInd  ARMGJOBQ_JOB_FINISH_IND��ҵ������־
     * @param jobFinishTime ARMGJOBQ_JOB_FINISH_TIME��ҵ������ʱ���
     * @param timeElapsed   ARMGJOBQ_TIME_ELAPSED��ҵ��ɺ�ʱ
     * @return ���ظ��¼�¼������
     * @version 2018.11.11
     */
    int upadteJobDoneSecond(long jobOrderSeqMs, String jobStatusQfr, String jobFinishInd,
                            Date jobFinishTime, int timeElapsed);

    /**
     * waitingReport()�����updateWaitingStatus����
     * ����ARMGJOBQ_JOB_ORDER_SEQ_MS�ֶ�
     * ������abbtable11���ݿ����ARMGJOBQ�����ҵ��Ϣ�������ϳ��Ƿ񵽴�ϳ��ȴ��Ƿ�ʱ��
     * ����ARMGSTATUS_ARMG_ID�ֶ�
     * ������abbtable11���ݿ����ARMGSTATUS������ػ���Ϣ���������ػ��ȴ����͡����ػ��Զ���״̬��
     *
     * @param jobOrderSeqMs      ARMGJOBQ_JOB_ORDER_SEQ_MS��ҵ������
     * @param chassisArriInd     ARMGJOBQ_ARRI_IND�ϳ��Ƿ񵽴�
     * @param chassisArriTimeout ARMGJOBQ_CHASSIS_ARRI_TIMEOUT�ϳ��ȴ��Ƿ�ʱ
     * @param armgId             ARMGSTATUS_ARMG_ID���ػ����
     * @param waitingType        ARMGSTATUS_WAITING_TYPE���ػ��ȴ�����
     * @param autoStatus         ARMGSTATUS_AUTO_STATUS���ػ��Զ���״̬
     * @return ���ظ��¼�¼������
     * @version 2018.11.11
     */
    int updateWaitingStatus(long jobOrderSeqMs, short chassisArriInd, short chassisArriTimeout, String armgId,
                            short waitingType, short autoStatus);

    /**
     * writeStack()�����updateSlotXJDStackStatus����
     * ����ARMGSLOTXJD_BLOCK_NO��ARMGSLOTXJD_BAY_NO��ARMGSLOTXJD_ROW_NO��ARMGSLOTXJD_TIER_NO�ֶ�
     * ������abbtable11���ݿ����ARMGSLOTXJD��Ķѳ���ͼ��Ϣ��������װ���š���װ�����͡�������������װ���ŵķ��򡢶Ѷ�(slot)�ĵ�ǰ״̬��������Ϣ��ʱ�䡢���±�־��
     *
     * @param blockNo    ARMGJOBQ_BLOCK_NO������
     * @param bayNo      ARMGJOBQ_BAY_NO��λ��
     * @param rowNo      ARMGJOBQ_ROW_NO�ź�
     * @param tierNo     ARMGJOBQ_TIER_NO���
     * @param cntrNo     ARMGJOBQ_CNTR_NO��װ����
     * @param cntrType   ARMGJOBQ_CNTR_TYPE��װ������
     * @param cntrWeight ARMGJOBQ_CNTR_WEIGHT��������
     * @param doorDirect ARMGJOBQ_DOOR_DIRECT��װ���ŵķ���
     * @param slotStatus ARMGJOBQ_SLOT_STATUS�Ѷ�(slot)�ĵ�ǰ״̬
     * @param modifyDate ARMGJOBQ_MODIFY_DATE������Ϣ��ʱ��
     * @param updateInd  ARMGJOBQ_UPDATE_IND���±�־
     * @return ���ظ��¼�¼������
     * @version 2018.11.11
     */
    int updateSlotXJDStackStatus(String blockNo, short bayNo, short rowNo, short tierNo, String cntrNo,
                                 String cntrType, int cntrWeight, short doorDirect, int slotStatus, Date modifyDate, String updateInd);
}