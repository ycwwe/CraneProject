package com.example.quartz.job.sender;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* public class MessageForm implements Serializable{
    private int instanceNum;
    private int classNum;
    private int methodNum;
    *//* private int replyFlag;*//**//*replyFlag=1，表示是回复消息，replyFlag=2，表示不是回复消息*//**//*
    private int sequenceNum;*//**//*消息是回复消息时，sequenceNum应与要被回复的消息sequenceNum一致。消息不是回复消息时，XJD段使用递增的偶数来唯一标识一条消息，XCC使用递增的奇数来唯一标示消息*//*
    private Map parameters;


    public int getInstanceNum() {
        return instanceNum;
    }

    public void setInstanceNum(int instanceNum) {
        this.instanceNum = instanceNum;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    public int getMethodNum() {
        return methodNum;
    }

    public void setMethodNum(int methodNum) {
        this.methodNum = methodNum;
    }

    public Map getParameter() {
        return parameters;
    }

    public void setParameter(Map parameter) {
        this.parameters = parameter;
    }

    *//*public int getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public int getReplyFlag() {
        return replyFlag;
    }

    public void setReplyFlag(int replyFlag) {
        this.replyFlag = replyFlag;
    }*//*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageForm that = (MessageForm) o;
        return instanceNum == that.instanceNum &&
                classNum == that.classNum &&
                methodNum == that.methodNum &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceNum, classNum, methodNum, parameters);
    }

    @Override
    public String toString() {
        return "MessageForm{" +
                "instanceNum=" + instanceNum +
                ", classNum=" + classNum +
                ", methodNum=" + methodNum +
                ", parameter=" + parameters +
                '}';
    }

    *//*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageForm that = (MessageForm) o;
        return instanceNum == that.instanceNum &&
                classNum == that.classNum &&
                methodNum == that.methodNum &&
                replyFlag == that.replyFlag &&
                sequenceNum == that.sequenceNum &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceNum, classNum, methodNum, replyFlag, sequenceNum, parameters);
    }

    public MessageForm(int instanceNum,
                       int classNum,
                       int methodNum, Map parameter
            ) {
        this.classNum=classNum;
        this.instanceNum=instanceNum;
        this.parameters=parameter;
        this.methodNum=methodNum;
    }

    @Override
    public String toString() {
        return "MessageForm{" +
                "instanceNum=" + instanceNum +
                ", classNum=" + classNum +
                ", methodNum=" + methodNum +
                ", replyFlag=" + replyFlag +
                ", sequenceNum=" + sequenceNum +
                ", parameters=" + parameters +
                '}';
    }*//*

    public MessageForm(int instanceNum,
                       int classNum,
                       int methodNum,
                       *//*int replyFlag,
                       int sequenceNum,*//* Map parameter
    ) {
        this.classNum=classNum;
        this.instanceNum=instanceNum;
        this.parameters=parameter;
        *//*this.replyFlag=replyFlag;
        this.sequenceNum=sequenceNum;*//*
        this.methodNum=methodNum;
    }

    public MessageForm() {
    }
}*/ public class MessageForm implements Serializable{
    private int instanceNum;
    private int classNum;
    private int methodNum;
    private int replyFlag;/*replyFlag=2，表示是回复消息，replyFlag=1，表示不是回复消息*/
    private int sequenceNum;/*消息是回复消息时，sequenceNum应与要被回复的消息sequenceNum一致。消息不是回复消息时，XJD段使用从零开始的递增的偶数来唯一标识一条消息，XCC使用递增的奇数来唯一标示消息*/
    private String sender;
    private String reciver;
    private Map parameters;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }




    public int getInstanceNum() {
        return instanceNum;
    }

    public void setInstanceNum(int instanceNum) {
        this.instanceNum = instanceNum;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    public int getMethodNum() {
        return methodNum;
    }

    public void setMethodNum(int methodNum) {
        this.methodNum = methodNum;
    }

    public Map getParameter() {
        return parameters;
    }

    public void setParameter(Map parameter) {
        this.parameters = parameter;
    }

    public int getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public int getReplyFlag() {
        return replyFlag;
    }

    public void setReplyFlag(int replyFlag) {
        this.replyFlag = replyFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageForm that = (MessageForm) o;
        return instanceNum == that.instanceNum &&
                classNum == that.classNum &&
                methodNum == that.methodNum &&
                replyFlag == that.replyFlag &&
                sequenceNum == that.sequenceNum &&
                Objects.equals(sender, that.sender) &&
                Objects.equals(reciver, that.reciver) &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceNum, classNum, methodNum, replyFlag, sequenceNum, sender, reciver, parameters);
    }

    public MessageForm(int instanceNum,
                       int classNum,
                       int methodNum, Map parameter
            ) {
        this.classNum=classNum;
        this.instanceNum=instanceNum;
        this.parameters=parameter;
        this.methodNum=methodNum;
    }

    @Override
    public String toString() {
        return "MessageForm{" +
                "instanceNum=" + instanceNum +
                ", classNum=" + classNum +
                ", methodNum=" + methodNum +
                ", replyFlag=" + replyFlag +
                ", sequenceNum=" + sequenceNum +
                ", sender='" + sender + '\'' +
                ", reciver='" + reciver + '\'' +
                ", parameters=" + parameters +
                '}';
    }

    public MessageForm(int instanceNum,
                       int classNum,
                       int methodNum,
                       int replyFlag,
                       int sequenceNum,
                       String sender,
                       String reciver,Map parameter
    ) {
        this.classNum=classNum;
        this.instanceNum=instanceNum;
        this.parameters=parameter;
        this.replyFlag=replyFlag;
        this.sequenceNum=sequenceNum;
        this.reciver=reciver;
        this.sender=sender;
        this.methodNum=methodNum;
    }

    public MessageForm() {
    }
}
