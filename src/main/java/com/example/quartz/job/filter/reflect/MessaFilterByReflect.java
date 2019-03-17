package com.example.quartz.job.filter.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MessaFilterByReflect {
    private String str = "com.example.business.BaseClass";

    public void test() {
        try {
            Class clazz = Class.forName(str);
            Object object = clazz.newInstance();
            Method m = clazz.getMethod("ack", null);
            Method m1 = clazz.getMethod("ack", null);
            m.invoke(object, null);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        MessaFilterByReflect m = new MessaFilterByReflect();
        m.test();
    }
}
