 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: CyclicBarrierTest
  * Author:   yangchong
  * Date:     2018/7/3 0003 下午 9:57
  * Description: 循环栅栏测试类
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.threads.tools;

 import java.util.Random;
 import java.util.concurrent.BrokenBarrierException;
 import java.util.concurrent.CyclicBarrier;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;

 /**
  * 循环栅栏：Cyclic意为循环，也就是说这个计数器可以反复使用。比如，假设我们将计数器设置为10，那么当前一批10个线程执行之后，计算器就会归零， 然后接着凑齐下一批10个线程<br>
  * 〈循环栅栏测试类〉 下面是一个士兵集合跑步的例子，首先士兵需要全部集合，结合完毕之后再开始进行准备工作，所有人准备完毕之后，长官发布命令：“全体起步跑”。
  *
  * @author yangchong
  * @create 2018/7/3 0003
  * @since 1.0.0
  */
 public class CyclicBarrierTest {

     private static int S_PREPARE_ACTION = 1;

     /**
      * 士兵类
      */
     static class Soldier implements Runnable {

         //士兵的名称
         private String soldierName;
         private CyclicBarrier cyclicBarrier;

         public Soldier(String soldierName, CyclicBarrier cyclicBarrier) {
             this.soldierName = soldierName;
             this.cyclicBarrier = cyclicBarrier;
         }

         /**
          * 士兵的整备工作
          */
         private void prepare() {
             try {
                 Thread.sleep(new Random().nextInt(10) * 1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             System.out.println(soldierName + "整备完毕！");
         }

         /**
          * 士兵进行集合动作
          */
         private void fallIn() {
             try {
                 Thread.sleep(new Random().nextInt(10) * 1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             System.out.println(soldierName + "集合完毕！");
         }

         @Override
         public void run() {
             try {
                 //开始集合
                 fallIn();
                 cyclicBarrier.await();
                 //开始准备
                 prepare();
                 cyclicBarrier.await();
             } catch (BrokenBarrierException e) {
                 e.printStackTrace();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
     }

     /**
      * 栅栏触发动作类，当所有的线程都到栅栏之后触发动作
      */
     static class BarrierAction implements Runnable {

         //flag == 1 代表集合动作，flag == 2 代表整备工作
         private int flag;

         BarrierAction(int action) {
             flag = action;
         }

         @Override
         public void run() {
             if (flag == 1) {
                 System.out.println("所有士兵集合完毕！");
                 flag++;
             } else {
                 System.out.println("所有士兵整备完毕！");
                 System.out.println("全体起步跑！");
             }
         }
     }

     /*
        开始集合！
        士兵4集合完毕！
        士兵0集合完毕！
        士兵2集合完毕！
        士兵1集合完毕！
        士兵3集合完毕！
        所有士兵集合完毕！
        士兵1整备完毕！
        士兵3整备完毕！
        士兵2整备完毕！
        士兵0整备完毕！
        士兵4整备完毕！
        所有士兵整备完毕！
        全体起步跑！
      */
     public static void main(String args[]) {
         CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new BarrierAction(S_PREPARE_ACTION));
         //ExecutorService pool = Executors.newFixedThreadPool(10);
         //假设有5个士兵
         System.out.println("开始集合！");
         Thread[] threads = new Thread[5];
         for (int i = 0; i < 5; i++) {
             threads[i] = new Thread(new Soldier("士兵" + i, cyclicBarrier));
             threads[i].start();
         }
     }

 }
