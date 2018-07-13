 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: RejectThreadPoolTest
  * Author:   yangchong
  * Date:     2018/7/5 0005 下午 8:03
  * Description: 线程池拒绝策略测试
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.threads.pool;

 import java.util.concurrent.LinkedBlockingQueue;
 import java.util.concurrent.RejectedExecutionHandler;
 import java.util.concurrent.SynchronousQueue;
 import java.util.concurrent.ThreadPoolExecutor;
 import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
 import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
 import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
 import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
 import java.util.concurrent.TimeUnit;

 /**
  * 〈线程池拒绝策略测试〉<br>
  *
  * @author yangchong
  * @create 2018/7/5 0005
  * @since 1.0.0
  */
 public class RejectThreadPoolTest {

     /**
      * 任务类
      */
     static class Task implements Runnable {

         @Override
         public void run() {
             try {
                 System.out.println("执行任务的线程Thread：" + Thread.currentThread().getId());
                 Thread.sleep(100);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
     }

     public static void main(String args []) {
         //AbortPolicy、DiscardPolicy、DiscardOldestPolicy、CallerRunsPolicy、new RejectedExecutionHandler接口自定义
         ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.SECONDS,
             new SynchronousQueue<>(), new DiscardPolicy());

         for (int i = 0; i < 100; i++) {
             pool.submit(new Task());
         }

         pool.shutdown();
     }
 }
