/*
 * Copyright 2013 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.concurrent.tools;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 * ��ThreadStack.java��ʵ���������鿴�̶߳�ջ 
 * ע:
 * ��δ�������ִ��jstack������Դ�ӡ���̵߳Ķ�ջ��Ϣ
 * </pre>
 * 
 * @author yangbolin Sep 20, 2013 5:12:04 PM
 */
public class ThreadStack {

    public void getStackInfo() {
        // ������ϵͳ�����е������߳�
        for (Map.Entry<Thread, StackTraceElement[]> stackTrace : Thread.getAllStackTraces().entrySet()) {
            // ��ȡ�߳�
            Thread thread = (Thread) stackTrace.getKey();
            // ��ȡ�̵߳Ķ�ջ��Ϣ
            StackTraceElement[] stack = (StackTraceElement[]) stackTrace.getValue();
            // ���Ե�ǰ�߳�
            if (thread.equals(Thread.currentThread())) {
                continue;
            }
            System.out.println("�̣߳�" + thread.getName());
            for (StackTraceElement element : stack) {
                System.out.println(element);
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 10; ++i) {
            MyThread myThread = new MyThread();
            Thread thread = new Thread(myThread);
            thread.setName("Thread " + i);
            pool.execute(thread);
        }

        ThreadStack threadStack = new ThreadStack();
        threadStack.getStackInfo();

        try {
            // ���ر��̳߳أ�����ͻ�һֱ��������״̬������main�����Ƴ����̳߳�Ҳ�����Լ��ر�
            Thread.sleep(10 * 1000);
            pool.shutdown();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " is running....");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
