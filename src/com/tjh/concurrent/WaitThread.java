package com.tjh.concurrent;

public class WaitThread {
    final static Object object = new Object();
    public static class T1 extends Thread{
        @Override
        public void run() {
            synchronized (object) {
                System.out.println(System.currentTimeMillis()+":T1 start!");
                try{
                    System.out.println(System.currentTimeMillis()+":T1 wait for object");
                    //当前线程释放对象的所有权，其他线程可以访问此对象，通知线程释放锁之后，此线程才能获取到所有权。
                    object.wait();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis()+":T1 end!");
            }
        }
    }

    public static class T2 extends Thread{
        @Override
        public void run() {
            //synchronized关键字里面的程序执行完之后就释放锁。
            synchronized (object) {
                System.out.println(System.currentTimeMillis()+":T2 start! notify one thread");
                //唤醒一个正在等待的线程，获取线程的所有权之后才能唤醒。
                object.notify();
                System.out.println(System.currentTimeMillis()+":T2 end!");
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String args[]) {
        Thread t1 = new T1();
        Thread t2 = new T2();
        //即使程序代码中t1先开始，但实验证明实际上仍存在t2比t1先获得锁的可能性。
        t1.start();
        t2.start();
    }

}
