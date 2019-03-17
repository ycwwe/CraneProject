package com.example.business.blockmap;

/*用于分析writeStack()接受的参数*/
public class StackDataType {
    public static final int STACK_STATUS_OK = 0;
    private int size = 0;
    private int tier = 0;
    private int status = 0;
    private SlotDataType slots;

    /*构造函数，多态*/
    public StackDataType() {
        this.slots = new SlotDataType("empty", 0, 0, 0, 0);
    }

    public StackDataType(int size, int tier, int status) {
        this();
        this.size = size;
        this.tier = tier;
        this.status = status;
    }

    public StackDataType(int size, int tier, int status, SlotDataType slots) {
        this.size = size;
        this.tier = tier;
        this.status = status;
        this.slots = slots;
    }
    /*toString方法*/
    /*private String printData() {
        String tmpString = "size=" + this.size + " tier=" + this.tier + " status=" + this.status + "\n";

        StringBuffer res = new StringBuffer();
        res.append(tmpString);
        for (int i = 0; i < 8; i++) {
            res.append("slot " + i + ": " + this.slots[i] + "\n");
        }
        return res.toString();
    }
    public String toString() {
        return printData();
    }*/

    @Override
    public String toString() {
        return "StackDataType{" +
                "size=" + size +
                ", tier=" + tier +
                ", status=" + status +
                ", slots=" + slots.toString() +
                '}';
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTier() {
        return tier;
    }

    public int getStatus() {
        return status;
    }

    public SlotDataType getSlots() {
        return slots;
    }

    public void setSlots(SlotDataType slots) {
        this.slots = slots;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }


}
