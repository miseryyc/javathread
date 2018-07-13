/*
 * Copyright (C), 2018-2018, XXX有限公司
 * FileName: ReentrantLockTest
 * Author:   yangchong
 * Date:     2018/7/2 0002 下午 3:15
 * Description: ReentrantLock的使用代码示例
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.tjh.concurrent.threads.tools;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1、与synchronized关键字相比，ReentranLock的锁释放由程序控制，而synchronized的锁释放由虚拟机控制<br>
 * 2、可重入，ReentranLock可以多次重复加锁，释放的时候也必须对应的多次释放锁。<br>
 * 3、可中断，当锁进入死锁或者等待时间过长时，需要对锁进行中断时，ReentranLock提供了可中断支持。<br>
 * 4、可限时，如果在有限时间内，没有拿到锁，则放弃锁申请，继续其他处理。<br>
 * 5、公平锁，保证先申请的线程先拥有锁，先来先使用的原则。防止出现饥饿现象。但性能比非公平锁差很多，通过使用构造函数ReentrantLock（boolean fair）实现公平锁。
 * 〈ReentrantLock的使用代码示例〉
 *
 * @author yangchong
 * @create 2018/7/2 0002
 * @since 1.0.0
 */
public class ReentrantLockTest {

    /**
     * ReentrantLock可重入性测试类
     */
    static class ReentrantLockReLock implements Runnable {
        private ReentrantLock lock = new ReentrantLock();
        private int i = 0;

        public int getValue() {
            return i;
        }

        @Override
        public void run() {
            for (int j = 0; j < 10000000; j++) {
                //这里进行了多次加锁，ReentrantLock的锁是可重入的
                lock.lock();
                lock.lock();
                try {
                    i++;
                } finally {
                    //lock的unlock方法必须在finally中使用，防止因为异常出现未处理的情况出现
                    //如果之前加锁了n次，那么这里也必须解锁n次。
                    lock.unlock();
                    lock.unlock();
                }
            }
        }
    }

    /**
     * ReentrantLock 支持可中断测试
     */
    static class ReentrantLockInterrupt implements Runnable {
        private static ReentrantLock lock1 = new ReentrantLock();
        private static ReentrantLock lock2 = new ReentrantLock();
        int lock;

        public ReentrantLockInterrupt(int lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            //lock == 1 和 lock ==2 的两个线程同时运行的时候，下述代码会产生一个死锁。lock == 1的线程和lock == 2的线程互相锁住。
            //如果使用lock（）方法就无法解锁了。
            try {
                if (lock == 1) {
                    lock1.lockInterruptibly();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    lock2.lockInterruptibly();
                } else {
                    lock2.lockInterruptibly();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    lock1.lockInterruptibly();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock1.isHeldByCurrentThread()) {
                    lock1.unlock();
                }
                if (lock2.isHeldByCurrentThread()) {
                    lock2.unlock();
                }
                System.out.println(Thread.currentThread().getId() + "：线程退出");
            }
        }
    }

    /**
     * ReentrantLock可限时性测试类
     */
    static class TimeLock implements Runnable {
        private static ReentrantLock lock = new ReentrantLock();

        @Override
        public void run() {
            try {
                try {
                    if (lock.tryLock(5, TimeUnit.SECONDS)) {
                        Thread.sleep(6000);
                    } else {
                        System.out.println("get lock failed");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                //检查当前线程是否保持此锁
                if (lock.isHeldByCurrentThread()) lock.unlock();
            }
        }
    }

    /**
     * 打印ReentrantLock可限时性测试结果
     * 运行结果：get lock failed
     */
    private static void printTimeLock() {
        TimeLock timeLock = new TimeLock();
        Thread t1 = new Thread(timeLock);
        Thread t2 = new Thread(timeLock);
        t1.start();
        t2.start();
    }

    /**
     * 打印ReentrantLockInterrupt可中断性测试结果
     */
    public static void printReentrantLockInterrupt() throws InterruptedException {
        ReentrantLockInterrupt r1 = new ReentrantLockInterrupt(1);
        ReentrantLockInterrupt r2 = new ReentrantLockInterrupt(2);

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        Thread.sleep(1000);
        //通过设置守护线程检查死锁线程，并且发起中断请求进行中断
        DeadLockChecker.check();
    }

    /**
     * 打印ReentrantLock可重入性测试结果
     */
    public static void printReentrantLockReLock() throws InterruptedException {
        ReentrantLockReLock t1 = new ReentrantLockReLock();
        Thread t2 = new Thread(t1);
        Thread t3 = new Thread(t1);
        t2.start();
        t3.start();
        t2.join();
        t3.join();
        //运行结果:20000000
        System.out.println(t1.getValue());
    }

    public static void main(String[] args) {
        /*try {
            //printReentrantLockReLock();
            //printReentrantLockInterrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        printTimeLock();
    }
}
