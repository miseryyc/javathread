 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: CountTask
  * Author:   yangchong
  * Date:     2018/7/5 0005 下午 9:37
  * Description: ForkJoin测试类
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.threads.pool;

 import java.util.ArrayList;
 import java.util.concurrent.ExecutionException;
 import java.util.concurrent.ForkJoinPool;
 import java.util.concurrent.ForkJoinTask;
 import java.util.concurrent.RecursiveTask;

 /**
  * ForkJoin测试类<br>
  * ForkJoin的思想就是递归算法的实现，不同的是通过多线程的方式来处理子任务。
  * @author yangchong
  * @create 2018/7/5 0005
  * @since 1.0.0
  */
 public class CountTask extends RecursiveTask<Long> {

     private static final int THRESHOLD = 10000;
     private long start;
     private long end;

     public CountTask(long start, long end) {
         this.start = start;
         this.end = end;
     }

     //compute()方法就是通过递归计算来执行分解任务操作，然后再将子任务的结果合并。
     @Override
     protected Long compute() {
         long sum = 0;
         boolean canCompute = (end - start) < THRESHOLD;
         //递归出口，结束条件
         if (canCompute) {
             for (long i = start; i < end; i++) {
                 sum += 1;
             }
         } else {
             //进行递归，分成100个小任务
             long step = (start + end) / 100;
             ArrayList<CountTask> subTasks = new ArrayList<>();
             long pos = start;
             for (int i = 0; i < 100; i++) {
                 long lastOne = pos + step;
                 if (lastOne > end) {
                     lastOne = end;
                 }
                 CountTask subTask = new CountTask(pos, lastOne);
                 pos = lastOne + 1;
                 subTasks.add(subTask);
                 //开启子任务执行，开启子线程，并且进入线程队列。
                 subTask.fork();
             }

             for (CountTask t : subTasks) {
                 //子任务结果合并
                 sum += t.join();
             }
         }
         return sum;
     }

     public static void main(String args[]) {
         ForkJoinPool forkJoinPool = new ForkJoinPool();
         CountTask task = new CountTask(0, 200000L);
         ForkJoinTask<Long> result = forkJoinPool.submit(task);

         long res = 0;
         try {
             res = result.get();
         } catch (InterruptedException | ExecutionException e) {
             e.printStackTrace();
         }
         System.out.println("sum = " + res);
     }
 }
