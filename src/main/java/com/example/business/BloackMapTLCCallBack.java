package com.example.business;

import com.example.business.blockmap.StackDataType;

import java.util.Map;

public interface BloackMapTLCCallBack {
   void  parseGetStack(int block, int blockmapRow, int blockmapBay);
   void  parseWriteStack(String block, int blockMapRow, int blockmapBay, StackDataType stackData, int force);
   void parseStackUtilizationReport(int block, int blockMapRow, int blockmapBay, int utilCode);
   void parseStackDataReport(int block, int blockMapRow, int blockmapBay, Map stackData);
   void parseBlockMapStatusReport(int block, int blockmapStatus, int corruptRow, int corruptBay);
}
