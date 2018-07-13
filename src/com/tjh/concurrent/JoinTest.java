package com.tjh.concurrent;

public class JoinTest extends Thread {

    @Override
    public void run() {
        for(int i=0; i<1000000; i++);
        System.out.println("Thread end!");
    }

    public static void main(String args[]) {

        JoinTest joinTest = new JoinTest();
        joinTest.setName("JoinThread");
        joinTest.start();
        try {
            joinTest.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Program end!");
    }
}
