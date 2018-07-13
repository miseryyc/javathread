package com.tjh.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {
    static AtomicInteger i = new AtomicInteger();
    static volatile Integer m = new Integer(0);
    static Integer n = new Integer(0);

    public static class AddThread implements Runnable {

        @Override
        public void run() {
            for (int k = 0; k < 10000; k++) {
                i.incrementAndGet();
                m++;
                n++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = new Thread[10];
        for (int k = 0; k < 10; k++) {
            ts[k] = new Thread(new AddThread());
        }

        for (int k = 0; k < 10; k++) {
            ts[k].start();
        }

        for (int k = 0; k < 10; k++) {
            ts[k].join();
        }

        //无锁运行下，AtomicInteger能保证操作的原子性。
        System.out.println(i);
        //无锁运行下，涉及到自身的运算例如自增操作并不能保证操作的原子性。
        System.out.println(m);
        //无锁运行下，一般的类型无法保证操作的原子性
        System.out.println(n);
    }

    //运行结果：
    //100000
    //80402
    //76009
}
