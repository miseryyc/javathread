package com.tjh.concurrent;

public class PriorityThread extends Thread {
    static int count = 0;
    public class PriorityDeamon{

    }

    PriorityThread (String name) {
        setName(name);
    }

    @Override
    public void run() {
        while (true) {
            synchronized (PriorityDeamon.class){
                count ++;
                if(count > 10000000) {
                    System.out.println(getName() + "is complete!");
                    break;
                }
            }
        }
    }

    //高优先级先执行的可能性更大，但不是绝对的先执行高优先级的。有可能也会先执行低优先级的线程
    //经实验，此程序运行10次，高优先级线程先执行完成的次数是9次，低优先级程序先执行完成的次数是1次
    public static void main(String args[]){
        PriorityThread highPriorityThread = new PriorityThread("High Priority Thread");
        PriorityThread lowPriorityThread = new PriorityThread("Low Priority Thread");
        lowPriorityThread.setPriority(Thread.MIN_PRIORITY);
        highPriorityThread.setPriority(Thread.MAX_PRIORITY);
        lowPriorityThread.start();
        highPriorityThread.start();
    }
}
