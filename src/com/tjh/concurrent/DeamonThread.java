package com.tjh.concurrent;

public class DeamonThread extends Thread {

    DeamonThread(String name) {
        setName(name);
    }

    @Override
    public void run() {
        while(true)  {
            System.out.println(getName() + "is alive!");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void  main(String [] args){

        DeamonThread deamon = new DeamonThread("deamon");
        //没设置成守护线程时，运行的结果是：
        // deamonis alive!
        //deamonis alive!
        //deamonis alive!
        //deamonis alive!
        //deamonis alive!
        //deamonis alive!

        //设置成守护线程时程序直接退出，说明java虚拟机如果判断如果只有守护线程时，就退出。
        deamon.setDaemon(true);
        deamon.start();
    }
}
