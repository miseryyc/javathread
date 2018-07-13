package com.tjh.concurrent;

public class ThreadTest  {
    public static void main(String [] args) {
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if(Thread.currentThread().isInterrupted()) {
                        System.out.println("interrupted");
                        break;
                    }
                    try{
                        Thread.sleep(10000);
                    }
                    catch (InterruptedException e) {
                        System.out.print("Interruped when sleep");
                        //异常抛出之后中断标志会清除，需要重新中断
                        Thread.currentThread().interrupt();
                    }
                }
                Thread.yield();
            }
        });
        thread3.setName("thread3");
        thread3.start();
    }

}


