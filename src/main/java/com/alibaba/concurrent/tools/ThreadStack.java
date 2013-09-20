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
 * 类ThreadStack.java的实现描述：查看线程堆栈 
 * 注:
 * 这段代码类似执行jstack命令，可以打印出线程的堆栈信息
 * </pre>
 * 
 * @author yangbolin Sep 20, 2013 5:12:04 PM
 */
public class ThreadStack {

    public void getStackInfo() {
        // 遍历在系统中运行的所有线程
        for (Map.Entry<Thread, StackTraceElement[]> stackTrace : Thread.getAllStackTraces().entrySet()) {
            // 获取线程
            Thread thread = (Thread) stackTrace.getKey();
            // 获取线程的堆栈信息
            StackTraceElement[] stack = (StackTraceElement[]) stackTrace.getValue();
            // 忽略当前线程
            if (thread.equals(Thread.currentThread())) {
                continue;
            }
            System.out.println("线程：" + thread.getName());
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
            // 不关闭线程池，程序就会一直处于运行状态，计算main函数推出，线程池也不会自己关闭
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
