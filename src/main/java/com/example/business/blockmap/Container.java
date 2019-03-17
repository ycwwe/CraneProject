
package com.example.business.blockmap;

import java.io.Serializable;

/*用于分析writeStack()接受的参数*/
/*集装箱ISO类*/
public class Container {

    private static final String RESVERSTION = "1";
    private String contISO;
    private String sizeISOCodes = null;
    private String heightISOCodes = null;
    private String typeISOCodes = null;
    private int cntrsize = -1;
    private int cntrhigh = 0;
    private int cntrtype = -1;

    /*构造函数实现多态*/
    public Container() {
    }

    /*实例化Container并传入箱子ISO编码，将ISO进行转换*/
    public Container(String cntrType) {
        this.parseContISo(cntrType);
    }

    /*实例化Container并传入箱子的size,higheit,type，转换为ISO*/
    public Container(int cntrsize, int cntrhigh, int cntrtype) {
        this.toContISO(cntrsize, cntrhigh, cntrtype);
        this.setContISO();
    }

    public static void main(String[] args) {
        /*Container c=new Container("22G1");
        System.out.println("h"+c.getCntrhigh());
        System.out.println("s"+c.getCntrsize());
        System.out.println("t"+c.getCntrtype());*/
        Container c = new Container(1, 1, 1);
        System.out.println(c.getContISO());
    }

    public String getContISO() {
        return contISO;
    }

    private void setContISO() {
        this.contISO = sizeISOCodes + heightISOCodes + typeISOCodes + RESVERSTION;
    }

    public int getCntrsize() {
        return cntrsize;
    }

    public int getCntrhigh() {
        return cntrhigh;
    }

    public int getCntrtype() {
        return cntrtype;
    }

    /*解析ISO*/
    private void parseContISo(String cntrISO) {
        char size = cntrISO.charAt(0);
        char high = cntrISO.charAt(1);
        char type = cntrISO.charAt(2);
        switch (size) {
            case '2':
                cntrsize = 1;
                break;
            case '4':
                cntrsize = 2;
                break;
            case 'L':
                cntrsize = 3;
                break;
        }
        switch (high) {
            case '2':
                cntrhigh = 1;
                break;
            case '5':
                cntrhigh = 2;
                break;
        }
        switch (type) {
            case 'G':
                cntrtype = 1;
                break;
            case 'U':
                cntrtype = 2;
                break;
            case 'T':
                cntrtype = 3;
                break;
            case 'P':
                cntrtype = 4;
                break;
            case 'R':
                cntrtype = 5;
                break;
        }
    }

    /*转化为ISO*/
    private void toContISO(int cntrsize, int cntrhigh, int cntrtype) {
        switch (cntrsize) {
            case 1:
                sizeISOCodes = "2";
                break;
            case 2:
                sizeISOCodes = "4";
                break;
            case 3:
                sizeISOCodes = "L";
                break;
        }
        switch (cntrhigh) {
            case 1:
                heightISOCodes = "2";
                break;
            case 2:
                heightISOCodes = "5";
                break;
        }
        switch (cntrtype) {
            case 1:
                typeISOCodes = "G";
                break;
            case 2:
                typeISOCodes = "U";
                break;
            case 3:
                typeISOCodes = "T";
                break;
            case 4:
                typeISOCodes = "P";
                break;
            case 5:
                typeISOCodes = "R";
                break;
        }
    }

}
/*
        implements Cloneable, Serializable
{
    private static final long serialVersionUID = -6877674488978982032L;
    private static int lengthIsoCodeInContainerInformation = 0;
    private static String sizeISOCodes = null;
    private static String heightISOCodes = null;
    private static String typeISOCodes = null;
    public static final int WEIGHT_UNDEFINED = -1;
    public static final int SIZE_ILLEGAL = 0;
    public static final int SIZE_20FEET = 1;
    public static final int SIZE_40FEET = 2;
    public static final int SIZE_45FEET = 3;
    public static final int SIZE_TWIN_20FEET = 4;
    public static int MAX_CONTAINER_HEIGHT_MM = getMmFromHeightInt(2);
    public static int MAX_CONTAINER_SIZE_MM = getMmFromSizeInt(3);
    public static int CONTAINER_WIDTH_MM = 2438;
    private static final String[] sizeDeclarators = { "20ft:", "40ft:", "45ft:" };
    private static final int[] sizeOfSizeDeclarators = { 5, 5, 5 };
    public static final int HEIGHT_ILLEGAL = 0;
    public static final int HEIGHT_8_5_FEET = 1;
    public static final int HEIGHT_9_5_FEET = 2;
    public static final int HEIGHT_8_0_FEET = 3;
    public static final int HEIGHT_9_0_FEET = 4;

    public static int getSize(String isoCode)
    {
        if ((isoCode == null) || (isoCode.length() < 1)) {
            return 0;
        }
        char codeIn = isoCode.charAt(0);
        switch (codeIn)
        {
            case '2':
                return checkSizeParameters(codeIn, sizeISOCodes, 1);
            case '4':
                return checkSizeParameters(codeIn, sizeISOCodes, 2);
            case 'L':
                return checkSizeParameters(codeIn, sizeISOCodes, 3);
        }
        return checkSizeParameters(codeIn, sizeISOCodes, 0);
    }

    public static int getDoorDirectionAsInt(DoorDirection doorDir)
    {
        switch (doorDir.ordinal())
        {
            case 1:
                return 2;
            case 2:
                return 1;
            case 3:
                return 4;
            case 4:
                return 3;
            case 5:
                return -1;
        }
        return 99;
    }

    public static DoorDirection getDoorDirectionFromInt(int doorDir)
    {
        switch (doorDir)
        {
            case 2:
                return DoorDirection.HighBayNumber;
            case 1:
                return DoorDirection.LowBayNumber;
            case 4:
                return DoorDirection.HighRowNumber;
            case 3:
                return DoorDirection.LowRowNumber;
            case -1:
                return DoorDirection.NotSpecified;
        }
        return DoorDirection.Illegal;
    }

    private static int checkSizeParameters(char sizeCodeIn, String sizeISOCodes, int callersSizeCodeSuggestion)
    {
        if ((sizeISOCodes == null) || (sizeISOCodes.isEmpty())) {
            return callersSizeCodeSuggestion;
        }
        String tmpSizeSubString = sizeISOCodes;
        for (int i = 0; i < sizeOfSizeDeclarators.length; i++)
        {
            int foundAtPos = tmpSizeSubString.indexOf(sizeDeclarators[i]);
            if (foundAtPos != -1)
            {
                String tmpCodesStr = tmpSizeSubString.substring(foundAtPos, tmpSizeSubString.indexOf(";") + 1);
                tmpSizeSubString = tmpSizeSubString.substring(tmpSizeSubString.indexOf(";") + 1, tmpSizeSubString.length());
                tmpCodesStr = tmpCodesStr.substring(sizeOfSizeDeclarators[i], tmpCodesStr.length() - 1);
                if ((tmpCodesStr != null) && (tmpCodesStr.length() > 0)) {
                    for (int j = 0; j < tmpCodesStr.length(); j++) {
                        if (tmpCodesStr.charAt(j) == sizeCodeIn) {
                            return i + 1;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static final String[] heightDeclarators = { "8.5ft:", "9.5ft:", "8.0ft:", "9.0ft:" };
    private static final int[] sizeOfHeightDeclarators = { 6, 6, 6, 6 };
    public static final int TYPE_ILLEGAL = -1;
    public static final int TYPE_GENERAL = 1;
    public static final int TYPE_OPEN_TOP = 2;
    public static final int TYPE_TANK = 3;
    public static final int TYPE_FLAT_RACK = 4;
    public static final int TYPE_REEFER = 5;
    public static final int TYPE_TANK_TBR = 6;
    public static final int TYPE_TANK_TR = 7;
    public static final int TYPE_TANK_BR = 8;
    public static final int TYPE_TANK_NoR = 9;
    public static final int TYPE_PLATFORM = 10;

    public static int getHeight(String isoCode)
    {
        if ((isoCode == null) || (isoCode.length() < 2)) {
            return 0;
        }
        char codeIn = isoCode.charAt(1);
        switch (codeIn)
        {
            case '0':
                return checkHeightParameters(codeIn, heightISOCodes, 3);
            case '2':
            case 'C':
                return checkHeightParameters(codeIn, heightISOCodes, 1);
            case '3':
                return checkHeightParameters(codeIn, heightISOCodes, 2);
            case '4':
            case 'D':
                return checkHeightParameters(codeIn, heightISOCodes, 4);
            case '5':
            case 'E':
                return checkHeightParameters(codeIn, heightISOCodes, 2);
        }
        return checkHeightParameters(codeIn, heightISOCodes, 0);
    }

    private static int checkHeightParameters(char heightCodeIn, String heightISOCodes, int callersHeightCodeSuggestion)
    {
        if ((heightISOCodes == null) || (heightISOCodes.isEmpty())) {
            return callersHeightCodeSuggestion;
        }
        String tmpHeightSubString = heightISOCodes;
        for (int i = 0; i < sizeOfHeightDeclarators.length; i++)
        {
            int foundAtPos = tmpHeightSubString.indexOf(heightDeclarators[i]);
            if (foundAtPos != -1)
            {
                String tmpCodesStr = tmpHeightSubString.substring(foundAtPos, tmpHeightSubString.indexOf(";") + 1);
                tmpHeightSubString = tmpHeightSubString.substring(tmpHeightSubString.indexOf(";") + 1, tmpHeightSubString.length());
                tmpCodesStr = tmpCodesStr.substring(sizeOfHeightDeclarators[i], tmpCodesStr.length() - 1);
                if ((tmpCodesStr != null) && (tmpCodesStr.length() > 0)) {
                    for (int j = 0; j < tmpCodesStr.length(); j++) {
                        if (tmpCodesStr.charAt(j) == heightCodeIn) {
                            return i + 1;
                        }
                    }
                }
            }
        }
        return 0;
    }

    private static final String[] typeDeclarators = { "General:", "OpenTop:", "Tank:", "FlatRack:", "Reefer:", "TankTBR:", "TankTR:", "TankBR:", "TankNoR:" };
    private static final int[] sizeOfTypeDeclarators = { 8, 8, 5, 9, 7, 8, 7, 7, 8 };

    public static int getType(String isoCode)
    {
        if ((isoCode == null) || (isoCode.length() < 3)) {
            return -1;
        }
        char codeIn = isoCode.charAt(2);
        switch (codeIn)
        {
            case 'G':
                return checkTypeParameters(codeIn, typeISOCodes, 1);
            case 'U':
                return checkTypeParameters(codeIn, typeISOCodes, 2);
            case 'T':
                return checkTypeParameters(codeIn, typeISOCodes, 3);
            case 'P':
                return checkTypeParameters(codeIn, typeISOCodes, 4);
            case 'R':
                return checkTypeParameters(codeIn, typeISOCodes, 5);
        }
        return checkTypeParameters(codeIn, typeISOCodes, -1);
    }

    private static int checkTypeParameters(char typeCodeIn, String typeISOCodes, int callersTypeCodeSuggestion)
    {
        if ((typeISOCodes == null) || (typeISOCodes.isEmpty())) {
            return callersTypeCodeSuggestion;
        }
        String tmpTypeSubString = typeISOCodes;
        for (int i = 0; i < sizeOfTypeDeclarators.length; i++)
        {
            int foundAtPos = tmpTypeSubString.indexOf(typeDeclarators[i]);
            if (foundAtPos != -1)
            {
                String tmpCodesStr = tmpTypeSubString.substring(foundAtPos, tmpTypeSubString.indexOf(";") + 1);
                tmpTypeSubString = tmpTypeSubString.substring(tmpTypeSubString.indexOf(";") + 1, tmpTypeSubString.length());
                tmpCodesStr = tmpCodesStr.substring(sizeOfTypeDeclarators[i], tmpCodesStr.length() - 1);
                if ((tmpCodesStr != null) && (tmpCodesStr.length() > 0)) {
                    for (int j = 0; j < tmpCodesStr.length(); j++) {
                        if (tmpCodesStr.charAt(j) == typeCodeIn) {
                            return i + 1;
                        }
                    }
                }
            }
        }
        return -1;
    }

    public static boolean isValid(String isoCode)
    {
        boolean isValid = (isoCode != null) && (getSize(isoCode) != 0) && (getHeight(isoCode) != 0) && (getType(isoCode) != -1);
        return isValid;
    }

    public static String fromValues(int size, int height, int type)
    {
        char sizeChar;

        switch (size)
        {
            case 1:
                sizeChar = '2';
                break;
            case 2:
                sizeChar = '4';
                break;
            case 3:
                sizeChar = 'L';
                break;
            default:
                sizeChar = 'X';
        }
        char heightChar;

        switch (height)
        {
            case 1:
                heightChar = '2';
                break;
            case 2:
                heightChar = '5';
                break;
            case 3:
                heightChar = '0';
                break;
            case 4:
                heightChar = '6';
                break;
            default:
                heightChar = 'X';
        }
        char typeChar;

        switch (type)
        {
            case 1:
                typeChar = 'G';
                break;
            case 2:
                typeChar = 'U';
                break;
            case 3:
                typeChar = 'T';
                break;
            case 4:
                typeChar = 'P';
                break;
            case 5:
                typeChar = 'R';
                break;
            default:
                typeChar = 'X';
        }
        return
                Character.toString(sizeChar) + Character.toString(heightChar) + Character.toString(typeChar) + "1";
    }

    private String containerNo = null;
    private String isoCode = null;
    private String displayIsoCode = null;
    private int weight = -1;
    private int latchedWeight = -1;
    private DoorDirection doorDirection = DoorDirection.NotSpecified;
    public static final int INVALID_DOORDIRECTION_INT = 99;
    public static final int ERROR_OK = 0;
    public static final int ERROR_MISSING_CONTAINER_NO = 1;
    public static final int ERROR_MISSING_ISOCODE = 2;
    public static final int ERROR_MISSING_WEIGHT = 3;
    public static final int ERROR_INVALID_ISOCODE = 4;
    public static final int ERROR_INVALID_DOORDIRECTION = 5;

    public Container() {}

    public Container(String containerNo, String isoCode, int weight, DoorDirection doorDirection)
    {
        this.containerNo = containerNo;
        this.isoCode = isoCode;
        this.weight = weight;
        this.doorDirection = doorDirection;
    }

    public Container(Container instanceToCopy)
    {
        this.containerNo = instanceToCopy.containerNo;
        this.doorDirection = instanceToCopy.doorDirection;
        this.isoCode = instanceToCopy.isoCode;
        this.displayIsoCode = instanceToCopy.displayIsoCode;
        this.weight = instanceToCopy.weight;
        this.latchedWeight = instanceToCopy.latchedWeight;
    }

    public String getContainerNo()
    {
        return this.containerNo;
    }

    public void setContainerNo(String val)
    {
        this.containerNo = val;
    }

    public String getContainerInformation()
    {
        if (lengthIsoCodeInContainerInformation <= 0) {
            return this.containerNo;
        }
        if (this.displayIsoCode == null) {
            return rightpad(this.isoCode, lengthIsoCodeInContainerInformation) + this.containerNo;
        }
        return rightpad(this.displayIsoCode, lengthIsoCodeInContainerInformation) + this.containerNo;
    }

    public void setContainerInformation(String val)
    {
        if (lengthIsoCodeInContainerInformation <= 0)
        {
            this.containerNo = val;
        }
        else if (val.length() < lengthIsoCodeInContainerInformation)
        {
            this.displayIsoCode = "";
            this.containerNo = "";
        }
        else
        {
            this.displayIsoCode = val.substring(0, lengthIsoCodeInContainerInformation);
            if (val.length() <= lengthIsoCodeInContainerInformation) {
                this.containerNo = "";
            } else {
                this.containerNo = val.substring(lengthIsoCodeInContainerInformation);
            }
        }
    }

    private static String rightpad(String text, int length)
    {
        return String.format("%-" + length + "." + length + "s", new Object[] { text });
    }

    public DoorDirection getDoorDirection()
    {
        return this.doorDirection;
    }

    public int getDoorDirectionAsInt()
    {
        return getDoorDirectionAsInt(this.doorDirection);
    }

    public void setDoorDirection(DoorDirection val)
    {
        this.doorDirection = val;
    }

    public String getIsoCode()
    {
        return this.isoCode;
    }

    public void setIsoCode(String val)
    {
        this.isoCode = val;
    }

    public void setIsoCode(int size, int height, int type)
    {
        String tmpIsoCode = fromValues(size, height, type);

        this.isoCode = tmpIsoCode;
    }

    public String getDisplayIsoCode()
    {
        return this.displayIsoCode;
    }

    public void setDisplayIsoCode(String displayIsoCode)
    {
        this.displayIsoCode = displayIsoCode;
    }

    public int getWeight()
    {
        return this.weight;
    }

    public void setWeight(int val)
    {
        this.weight = val;
    }

    public int getLatchedWeight()
    {
        return this.latchedWeight;
    }

    public void setLatchedWeight(int val)
    {
        this.latchedWeight = val;
    }

    public String toString()
    {
        if (this.containerNo != null) {
            return this.containerNo;
        }
        return "(empty)";
    }

    public int hashCode()
    {
        if (this.containerNo != null) {
            return this.containerNo.hashCode();
        }
        return 0;
    }

    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Container other = (Container)obj;
        if ((this.containerNo != other.containerNo) && ((this.containerNo == null) ||

                (!this.containerNo.equals(other.containerNo)))) {
            return false;
        }
        return true;
    }

    public int getSize()
    {
        return getSize(this.isoCode);
    }

    public int getSizeFeet()
    {
        return getFeetsFromSizeInt(getSize());
    }

    public int getSizeMm()
    {
        return getMmFromSizeInt(getSize());
    }

    public int getHeight()
    {
        return getHeight(this.isoCode);
    }

    public int getHeightMm()
    {
        return getMmFromHeightInt(getHeight(this.isoCode));
    }

    public float getHeightFeet()
    {
        return getFeetFromHeightInt(getHeight(this.isoCode));
    }

    public int getType()
    {
        return getType(this.isoCode);
    }

    public Object clone()
            throws CloneNotSupportedException
    {
        return new Container(this);
    }

    public int validate()
    {
        if (!isUsed()) {
            return 0;
        }
        if ((this.containerNo == null) || (this.containerNo.isEmpty())) {
            return 1;
        }
        if (this.isoCode == null) {
            return 2;
        }
        if (this.weight == -1) {
            return 3;
        }
        if ((this.doorDirection == null) || (this.doorDirection == DoorDirection.Illegal)) {
            return 5;
        }
        if (!isValid(this.isoCode)) {
            return 4;
        }
        return 0;
    }

    public boolean isUsed()
    {
        if ((this.isoCode == null) && (this.containerNo == null) && (this.weight == -1) && (this.doorDirection == DoorDirection.NotSpecified)) {
            return false;
        }
        return true;
    }

    public boolean isContainerNoValid()
    {
        return (this.containerNo != null) && (this.containerNo.length() > 0) && (!this.containerNo.equals("-1"));
    }

    public static void setSizeISOCodes(String _sizeISOCodes)
    {
        sizeISOCodes = _sizeISOCodes;
    }

    public static String getSizeISOCodes()
    {
        return sizeISOCodes;
    }

    public static void setHeightISOCodes(String _heightISOCodes)
    {
        heightISOCodes = _heightISOCodes;
    }

    public static String getHeightISOCodes()
    {
        return heightISOCodes;
    }

    public static void setTypeISOCodes(String _typeISOCodes)
    {
        typeISOCodes = _typeISOCodes;
    }

    public static String getTypeISOCodes()
    {
        return typeISOCodes;
    }

    public static int getFeetsFromSizeInt(int sizeInt)
    {
        int result = -1;
        switch (sizeInt)
        {
            case 1:
                result = 20;
                break;
            case 2:
                result = 40;
                break;
            case 3:
                result = 45;
                break;
        }
        return result;
    }

    public static int getMmFromSizeInt(int sizeInt, int defaultValue)
    {
        int result = defaultValue;
        switch (sizeInt)
        {
            case 1:
                result = 6100;
                break;
            case 2:
                result = 12190;
                break;
            case 3:
                result = 13720;
                break;
        }
        return result;
    }

    public static int getMmFromSizeInt(int sizeInt)
    {
        return getMmFromSizeInt(sizeInt, -1);
    }

    public static int getMmWidth()
    {
        return CONTAINER_WIDTH_MM;
    }

    public static int getMmFromHeightInt(int heightInt)
    {
        int result = -1;
        switch (heightInt)
        {
            case 1:
                result = 2591;
                break;
            case 2:
                result = 2896;
                break;
            case 3:
                result = 2438;
                break;
            case 4:
                result = 2743;
                break;
        }
        return result;
    }

    public static float getFeetFromHeightInt(int heightInt)
    {
        float result = -1.0F;
        switch (heightInt)
        {
            case 1:
                result = 8.5F;
                break;
            case 2:
                result = 9.5F;
                break;
            case 3:
                result = 8.0F;
                break;
            case 4:
                result = 9.0F;
                break;
        }
        return result;
    }

    public static int getLengthIsoCodeInContainerInformation()
    {
        return lengthIsoCodeInContainerInformation;
    }

    public static void setLengthIsoCodeInContainerInformation(int lengthIsoCodeInContainerInformation)
    {
        lengthIsoCodeInContainerInformation = lengthIsoCodeInContainerInformation;
    }
    public enum DoorDirection
    {
        NotSpecified,  LowBayNumber,  HighBayNumber,  LowRowNumber,  HighRowNumber,  Illegal;

        private DoorDirection() {}
    }
}
*/
