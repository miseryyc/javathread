 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: QueueCompare
  * Author:   yangchong
  * Date:     2018/7/5 0005 上午 10:42
  * Description: 阻塞队列和非阻塞队列性能测试
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.threads.tools;

 import java.util.concurrent.BlockingQueue;
 import java.util.concurrent.ConcurrentLinkedQueue;
 import java.util.concurrent.CopyOnWriteArrayList;
 import java.util.concurrent.LinkedBlockingQueue;

 /**
  * 〈阻塞队列和非阻塞队列性能测试〉<br> 下面是BlockingQueue和ConcurrentLinkedQueue的性能测试。程序实现的是一个装球游戏，有两个篮子，分别分两组，每组10个人，当篮子中的球满了代表改组胜利。
  *
  * @author yangchong
  * @create 2018/7/5 0005
  * @since 1.0.0
  */
 public class QueueCompare {

     private static final int CAPACITY = 10000;
     private static final int PERSONS = 100;

     //A组的人
     static class AGroupPerson extends Thread {

         private BlockingQueue basket;

         AGroupPerson(BlockingQueue basket) {
             this.basket = basket;
         }

         @Override
         public void run() {
             for (int i = 0; i < 10000; i++) {
                 basket.add(new Object());
             }
         }
     }

     //B组的人
     static class BGroupPerson extends Thread {

         private ConcurrentLinkedQueue basket;

         BGroupPerson(ConcurrentLinkedQueue basket) {
             this.basket = basket;
         }

         @Override
         public void run() {
             for (int i = 0; i < 10000; i++) {
                 basket.add(new Object());
             }
         }
     }

     //运行结果：
     //A组耗时：2737
     //B组耗时：136
     public static void main(String[] args) throws InterruptedException {
         //A组用的是阻塞队列
         BlockingQueue basketA = new LinkedBlockingQueue();
         //B组用的是非阻塞队列
         //ConcurrentLinkedQueue的API 原来.size() 是要遍历一遍集合的,尽量不用size
         ConcurrentLinkedQueue basketB = new ConcurrentLinkedQueue();

         AGroupPerson[] ap = new AGroupPerson[PERSONS];
         long start = System.currentTimeMillis();
         //A组进行装载
         for (int i = 0; i < PERSONS; i++) {
             ap[i] = new AGroupPerson(basketA);
             ap[i].start();
         }

         for (int i = 0; i < PERSONS; i++) {
            ap[i].join();
         }
         long end = System.currentTimeMillis();
         System.out.println("A组耗时：" + (end-start));

         start = System.currentTimeMillis();
         BGroupPerson[] bp = new BGroupPerson[PERSONS];
         for (int i = 0; i < PERSONS; i++) {
             bp[i] = new BGroupPerson(basketB);
             bp[i].start();
         }
         for (int i = 0; i < PERSONS; i++) {
             bp[i].join();
         }
         end = System.currentTimeMillis();
         System.out.println("B组耗时：" + (end-start));
     }
 }
