package com.tjh.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {
    final static AtomicReference<String> atomicStr = new AtomicReference<String>("abc");

    /*运行结果：
    Thread:11Change Value
    Thread:15Failed
    Thread:19Failed
    Thread:12Failed
    Thread:16Failed
    Thread:20Failed
    Thread:14Failed
    Thread:18Failed
    Thread:13Failed
    Thread:17Failed*/
    //从结果可以看出，只有一个线程修改成功，这是符合无锁状态下线程特点，即都可以进入临界区进行竞争资源，但保证只有一个能胜出，CAS算法。
    public static void main(String [] args) {
        for(int i=0; i<10;i++) {
            final int num =i;
            new Thread(){
                public void run() {
                    try {
                        Thread.sleep(Math.abs((int)Math.random()*100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(atomicStr.compareAndSet("abc","def")){
                        System.out.println("Thread:"+Thread.currentThread().getId() + "Change Value");
                    }else {
                        System.out.println("Thread:"+Thread.currentThread().getId() + "Failed");
                    }
                }
            }.start();
        }
    }
}
