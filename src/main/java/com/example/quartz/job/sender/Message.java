package com.example.quartz.job.sender;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Message
        implements Serializable
{
    private static final long serialVersionUID = 6670407456922843040L;
    private static final int HEADER_LENGTH = 80;
    private static final int CORRECT_SYNC_CODE = 1094861345;
    private static final int REPLY_FLAG_IS_REPLY = 2;
    private static final int REPLY_FLAG_NO_REPLY = 1;
    private byte[] bytes;
    private Message replyTo = null;
    private long receiveTimeStamp;

    public Message(Message messageToCopy)
    {
        this.bytes = ((byte[])messageToCopy.bytes.clone());
    }

    public Message(byte[] bytes)
    {
        this.bytes = bytes;
    }

    public Message(int dataLength, int instance, int _class, int method)
    {
        this.bytes = new byte[dataLength + 80];


        setAbsoluteSLI(this.bytes, 0, dataLength + 80);


        setAbsoluteSLI(this.bytes, 4, 1094861345);


        setAbsoluteSLI(this.bytes, 32, instance);


        setAbsoluteSLI(this.bytes, 36, _class);


        setAbsoluteSLI(this.bytes, 40, method);

        setReplyFlag(1);
    }

    public Message(int dataLength, int instance, int _class, int method, Message replyToMsg)
    {
        this(dataLength, instance, _class, method);
        if (replyToMsg != null)
        {
            setReplyFlag(2);
            setSequenceNo(replyToMsg.getSequenceNo());
            this.replyTo = replyToMsg;
        }
    }

    public byte[] getBytes()
    {
        return this.bytes;
    }

    public static final int getHeaderLength()
    {
        return 80;
    }

    public static final int getCorrectSyncCode()
    {
        return 1094861345;
    }

    public static final int getReplyFlagIsReply()
    {
        return 2;
    }

    public static final int getReplyFlagNoReply()
    {
        return 1;
    }

    public static int getTotalLength(byte[] bytes)
    {
        return getAbsoluteSLI(bytes, 0);
    }

    public int getTotalLength()
    {
        return getAbsoluteSLI(this.bytes, 0);
    }

    public int getDataLength()
    {
        return getTotalLength() - 80;
    }

    public static int getSync(byte[] bytes)
    {
        return getAbsoluteSLI(bytes, 4);
    }

    public int getSync()
    {
        return getAbsoluteSLI(this.bytes, 4);
    }

    public String getSender()
    {
        return getAbsoluteString(8, 12);
    }

    public String getReceiver()
    {
        return getAbsoluteString(20, 12);
    }

    public int getInstanceNo()
    {
        return getAbsoluteSLI(this.bytes, 32);
    }

    public int getClassNo()
    {
        return getAbsoluteSLI(this.bytes, 36);
    }

    public int getMethodNo()
    {
        return getAbsoluteSLI(this.bytes, 40);
    }

    public int getSequenceNo()
    {
        return getAbsoluteSLI(this.bytes, 48);
    }

    public Date getDateAndTime()
    {
        Calendar cal = new GregorianCalendar();

        cal.set(1, getAbsoluteSLI(this.bytes, 52));
        cal.set(2, getAbsoluteSLI(this.bytes, 56) - 1);
        cal.set(5, getAbsoluteSLI(this.bytes, 60));
        cal.set(11, getAbsoluteSLI(this.bytes, 64));
        cal.set(12, getAbsoluteSLI(this.bytes, 68));
        cal.set(13, getAbsoluteSLI(this.bytes, 72));
        cal.set(14, getAbsoluteSLI(this.bytes, 76));

        return cal.getTime();
    }

    public int getReplyFlag()
    {
        return getAbsoluteSLI(this.bytes, 44);
    }

    public int getSLI(int startPos)
    {
        return getAbsoluteSLI(this.bytes, startPos + 80);
    }

    public String getAbsoluteString(int startPos, int length)
    {
        StringBuffer res = new StringBuffer();
        if (this.bytes.length >= startPos + length) {
            for (int i = 0; (i < length) && (this.bytes[(startPos + i)] != 0); i++) {
                res.append((char)this.bytes[(startPos + i)]);
            }
        }
        return res.toString();
    }

    public String getString(int startPos, int length)
    {
        return getAbsoluteString(startPos + 80, length);
    }

    public void setSender(String sender)
    {
        setAbsoluteString(this.bytes, 8, sender, 12);
    }

    public void setReceiver(String receiver)
    {
        setAbsoluteString(this.bytes, 20, receiver, 12);
    }

    public void setSequenceNo(int sequence)
    {
        setAbsoluteSLI(this.bytes, 48, sequence);
    }

    public Message getReplyTo()
    {
        return this.replyTo;
    }

    public void setDateAndTime(Date dateAndTime)
    {
        if (dateAndTime != null)
        {
            Calendar cal = new GregorianCalendar();
            cal.setTime(dateAndTime);

            setAbsoluteSLI(this.bytes, 52, cal.get(1));
            setAbsoluteSLI(this.bytes, 56, cal.get(2) + 1);
            setAbsoluteSLI(this.bytes, 60, cal.get(5));
            setAbsoluteSLI(this.bytes, 64, cal.get(11));
            setAbsoluteSLI(this.bytes, 68, cal.get(12));
            setAbsoluteSLI(this.bytes, 72, cal.get(13));
            setAbsoluteSLI(this.bytes, 76, cal.get(14));
        }
    }

    public void setCurrentDateAndTime()
    {
        Calendar cal = new GregorianCalendar();

        setAbsoluteSLI(this.bytes, 52, cal.get(1));
        setAbsoluteSLI(this.bytes, 56, cal.get(2) + 1);
        setAbsoluteSLI(this.bytes, 60, cal.get(5));
        setAbsoluteSLI(this.bytes, 64, cal.get(11));
        setAbsoluteSLI(this.bytes, 68, cal.get(12));
        setAbsoluteSLI(this.bytes, 72, cal.get(13));
        setAbsoluteSLI(this.bytes, 76, cal.get(14));
    }

    public void setReplyFlag(int replyFlag)
    {
        setAbsoluteSLI(this.bytes, 44, replyFlag);
    }

    public void setSLI(int startPos, int value)
    {
        setAbsoluteSLI(this.bytes, startPos + 80, value);
    }

    public void setString(int startPos, String value, int length)
    {
        setAbsoluteString(this.bytes, startPos + 80, value, length);
    }

    public void setReceiveTimeStamp(long timeStamp)
    {
        this.receiveTimeStamp = timeStamp;
    }

    public long getReceiveTimeStamp()
    {
        return this.receiveTimeStamp;
    }

    private static int getAbsoluteSLI(byte[] bytes, int startPos)
    {
        int value = -1;
        if (bytes.length >= startPos + 4)
        {
            int firstByte = 0xFF & bytes[startPos];
            int secondByte = 0xFF & bytes[(startPos + 1)];
            int thirdByte = 0xFF & bytes[(startPos + 2)];
            int fourthByte = 0xFF & bytes[(startPos + 3)];

            value = firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte;
        }
        return value;
    }

    private static void setAbsoluteSLI(byte[] bytes, int startPos, int value)
    {
        if (bytes.length >= startPos + 4)
        {
            bytes[startPos] = ((byte)(value >> 24 & 0xFF));
            bytes[(startPos + 1)] = ((byte)(value >> 16 & 0xFF));
            bytes[(startPos + 2)] = ((byte)(value >> 8 & 0xFF));
            bytes[(startPos + 3)] = ((byte)(value & 0xFF));
        }
    }

    private static void setAbsoluteString(byte[] bytes, int startPos, String value, int length)
    {
        if (bytes.length >= startPos + length) {
            for (int i = 0; i < length; i++) {
                if ((value != null) && (i < value.length())) {
                    bytes[(startPos + i)] = ((byte)value.charAt(i));
                } else {
                    bytes[(startPos + i)] = 0;
                }
            }
        }
    }

    public String toString()
    {
        return








                "AALP_Message [" + getSender() + "->" + getReceiver() + ", sequenceNo=" + getSequenceNo() + ", replyFlag=" + getReplyFlag() + ", time=" + getAbsoluteSLI(this.bytes, 52) + "-" + (getAbsoluteSLI(this.bytes, 56) - 1) + "-" + getAbsoluteSLI(this.bytes, 60) + "T" + getAbsoluteSLI(this.bytes, 64) + ":" + getAbsoluteSLI(this.bytes, 68) + ":" + getAbsoluteSLI(this.bytes, 72) + "." + getAbsoluteSLI(this.bytes, 76) + ", {" + getInstanceNo() + "," + getClassNo() + "," + getMethodNo() + "}]";
    }
}
