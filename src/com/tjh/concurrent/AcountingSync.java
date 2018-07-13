package com.tjh.concurrent;

public class AcountingSync implements Runnable {
    static AcountingSync instance = new AcountingSync();
    static int i = 0;

    @Override
    public void run() {
        for (int j = 0; j < 10000000; j++) {
            //当此处有关键字synchronized时，结果是200000000刚好是两者之和，如果没有则是17830376，
            //说明线程通过锁保证i是安全的。
            synchronized (instance) {
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
