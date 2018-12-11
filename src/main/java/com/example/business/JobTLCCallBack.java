package com.example.business;

public interface JobTLCCallBack {
    void parseJobAcceptedReport(long jobId);
    void parseJobStartedReport(long jobId, int predictedTime, int partOfJob);
    void parseJobDoneReport(long jobId, int regarding, int action, int rejectCode);
    void parseWaitingReport(long jobId, int waitType, int latchedWeight);
   /* void parseTransferPosReport(int actBlock,int actRow,int actBay,int actHeight,int blockTransferArea);

    void parseBlockShuffleReport(int blockPickUp,int rowPickUp,int bayPickUp,int tierNumberPickUp,int contSizePickUp,int landingTypePickUp,int clearancePickUp,int blockSetDown,int rowSetDown,int baySetDown,int tierNumberSetDown,int doorReqDirSetDown,int landingTypeSetDown,int clearanceSetDown,int jobId,int reportType);

    void parsePickUpChassisReport(int blockPickUp,int chassisRowPickUp,int chassisBayPickUp,int positionOnChassisPickUp,int chassisTypePickUp,int chassisNoPickUp,String RFID,String ContNumberPickUp,int weightPickUp,int doorPickUp,int contSizePickUp,int contHeightPickUp,int contTypePickUp,int landingTypePickUp,int clearancePickUp,int blockSetDown,int rowSetDown,int baySetDown,int tierNumberSetDown,int doorReqDirSetDown,int landingTypeSetDown,int clearanceSetDown,int jobId,int reportType);

    void parseSetDownChassisReport(int blockPickUp,int rowPickUp,int bayPickUp,int tierNumberPickUp,int contSizePickUp,int landingTypePickUp,int clearancePickUp,int blockSetDown,int chassisRowSetDown,int chassisBaySetDown,int posOnChassisSetDown,int chassisTypeSetDown,int chassisNoSetDown,String RFID,String contNumberSetDown,int doorReqDirSetDown,int landingTypeSetDown,int clearanceSetDown,int jobId,int reportType);
    void parseMoveReport(int block,int row,int bay,int height,int spreaderSize,int jobId,int reportType);
    void parseParkReport(int block,int parkPos,int jobId,int reportType);
    void parseCalibrateSensorsReport(int block,int calibrate,int startRow,int stopRow,int startBay,int stopBay,int rigNumber,int jobId,int reportType);

    void parseChassisShuffleReport(int blockPickUp,int chassisRowPickUp,int chassisBayPickUp,int positionOnChassisPickUp,int chassisTypePickUp,int chassisNoPickUp,String RFIDPickUp,String ContNumberPickUp,int weightPickUp,int doorPickUp,int contSizePickUp,int contHeightPickUp,int contTypePickUp,int landingTypePickUp,int clearancePickUp,int blockSetDown,int chassisRowSetDown,int chassisBaySetDown,int posOnChassisSetDown,int chassisTypeSetDown,int chassisNoSetDown,String RFIDSetDown,String contNumberSetDown,int doorReqDirSetDown,int landingTypeSetDown,int clearanceSetDown,int jobId,int reportType);

    void parsePickUpTransferAreaReport(int blockPickUp,int TransferRowPickUp,int transferBayPickUp,int tierNumberPickUp,String ContNumberPickUp,int weightPickUp,int doorPickUp,int contSizePickUp,int contHeightPickUp,int contTypePickUp,int landingTypePickUp,int clearancePickUp,int blockSetDown,int rowSetDown,int baySetDown,int tierNumberSetDown,int doorReqDirSetDown,int landingTypeSetDown,int clearanceSetDown,int jobId,int reportType);

    void parseSetDownTransferAreaReport(int blockPickUp,int rowPickUp,int bayPickUp,int tierNumberPickUp,int contSizePickUp,int landingTypePickUp,int clearancePickUp,int blockSetDown,int TransferRowSetDown,int transferBaySetDown,int tierNumberSetDown,String contNumberSetDown, int doorReqDirSetDown,int landingTypeSetDown,int clearanceSetDown,int jobId,int reportType);

*/
}
