package com.example.business.blockmap;

/*用于分析writeStack()接受的参数*/
public class SlotDataType {
    private String identity;/*集装箱独立ID*/
    private int doorDirection;
    private int weight;
    private int type;
    private int height;

    public int getType() {
        return type;
    }

    public int getDoorDirection() {
        return doorDirection;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setDoorDirection(int doorDirection) {
        this.doorDirection = doorDirection;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }


    public SlotDataType() {
    }

    public SlotDataType(String identity, int type, int height, int doorDirection, int weight) {
        this.identity = identity;
        this.type = type;
        this.height = height;
        this.doorDirection = doorDirection;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "SlotDataType{" +
                "identity='" + identity +
                ", doorDirection=" + doorDirection +
                ", weight=" + weight +
                ", type=" + type +
                ", height=" + height +
                '}';
    }
}
