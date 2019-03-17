package com.example.business;

import com.example.business.blockmap.Container;
import com.example.business.blockmap.SlotDataType;
import com.example.business.blockmap.StackDataType;
import com.example.dao.service.XjdDaoService;
import com.example.dao.service.XjdDaoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class BlockMapTLCCallBackImpl implements BloackMapTLCCallBack {

    @Autowired
    private XjdDaoServiceImpl xjdDaoServiceImpl;

    @Override
    public void parseGetStack(int block, int blockmapRow, int blockmapBay) {

    }

    /*对stackData进行分析，转化，写入数据库*/
    @Override
    public void parseWriteStack(String block, int blockMapRow, int blockMapBay, StackDataType stackData, int force) {
        SlotDataType slotData = stackData.getSlots();
        String contISO = new Container(stackData.getSize(), slotData.getHeight(), slotData.getType()).getContISO();
        xjdDaoServiceImpl.updateSlotXJDStackStatus(block, (short) blockMapBay, (short) blockMapRow, (short) stackData.getTier(), slotData.getIdentity(), contISO, slotData.getWeight(), (short) slotData.getDoorDirection(), StackDataType.STACK_STATUS_OK, new Date(), "Y");
    }

    @Override
    public void parseStackUtilizationReport(int block, int blockMapRow, int blockmapBay, int utilCode) {

    }

    @Override
    public void parseStackDataReport(int block, int blockMapRow, int blockmapBay, Map stackData) {

    }

    @Override
    public void parseBlockMapStatusReport(int block, int blockmapStatus, int corruptRow, int corruptBay) {

    }
}
