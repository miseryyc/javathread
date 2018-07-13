package com.tjh.concurrent;

public class TestRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("This is a second create method");
    }
}
