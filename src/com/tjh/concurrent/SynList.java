package com.tjh.concurrent;

import java.util.ArrayDeque;
import java.util.Queue;

//使用 wait notify 实现一个队列，队列有2个方法，add 和 get 。add方法往队列中添加元素，get方法往队列中获得元素。
// 队列必须是线程安全的。如果get执行时，队列为空，线程必须阻塞等待，直到有队列有数据。如果add时，队列已经满，
// 则add线程要等待，直到队列有空闲空间。

public class SynList{
    private static  Queue<Object> queue = new ArrayDeque<>(5);

    public synchronized Object getElement() {
        //队列空的时候需要等待，直到有队列中有元素
        if(queue.isEmpty()) {
            try {
                System.out.println("Queue is empty!");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //此时如果没有空的时候则可以通知新增方法
        Object object = queue.poll();
        notify();
        return object;
    }

    public synchronized void addElement(Object o) {
        //队列满了,则需要等待。
        if(queue.size() == 5) {
            try {
                System.out.print("Queue is full!");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //队列新增数据的时候，则表明列表有元素了，可以通知get方法获取元素
        queue.add(o);
        notify();
    }

    public static void main(String args []) {
        final SynList synList = new SynList();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(true) {
                    synList.addElement(i);
                    System.out.println( "t1 add element :" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Object object = synList.getElement();
                    System.out.println("t2 get element:" + object.toString());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t1.start();
        t2.start();
    }
}
