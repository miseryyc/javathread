 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: SemaphoneTest
  * Author:   yangchong
  * Date:     2018/7/3 0003 下午 3:43
  * Description: 信号量类测试
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.threads.tools;

 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.Semaphore;

 /**
  * 〈一句话功能简述〉<br> 〈信号量类测试〉
  *
  * @author yangchong
  * @create 2018/7/3 0003
  * @since 1.0.0
  */
 public class SemaphoneTest implements Runnable {

     final Semaphore semaphore = new Semaphore(5);
     @Override
     public void run() {
        try{
            //semaphore.acquire(2); 如果一次申请2个访问权限。那么同时只能运行2个这样的线程。因为总共只有5个线程权限。
            semaphore.acquire();
            Thread.sleep(2000);
            //System.out.println(Thread.currentThread().getId() + ":done!");
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            //semaphore.release(2); 如果前面acquire的时候是2，释放的时候也必须是2
            semaphore.release(2);
        }
     }

     //输出结果，一次输出5个done，共四次输出，每次大概2秒，总共花时8007ms：
     /*  15:done!
         14:done!
         11:done!
         12:done!
         13:done!
         17:done!
         16:done!
         18:done!
         21:done!
         19:done!
         22:done!
         20:done!
         23:done!
         25:done!
         24:done!
         26:done!
         30:done!
         27:done!
         29:done!
         28:done!
         8007*/
     public static void main(String args[]) throws InterruptedException {
         //创建固定线程数量的线程池
         ExecutorService executorService = Executors.newFixedThreadPool(20);
         final SemaphoneTest semaphoneTest = new SemaphoneTest();
         //2秒内同时只能有5个线程处理，20个线程任务应该需要8秒的时间。
         long start = System.currentTimeMillis();
         for (int i = 0; i < 20; i++) {
             executorService.submit(semaphoneTest);
         }
         //1.shutdown方法：这个方法会平滑地关闭ExecutorService，当我们调用这个方法时，ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：
         // 一类是已经在执行的，另一类是还没有开始执行的)，当所有已经提交的任务执行完毕后将会关闭ExecutorService。
         //
         //2.awaitTermination方法：这个方法有两个参数，一个是timeout即超时时间，另一个是unit即时间单位。这个方法会使线程等待timeout时长，当超过timeout时间后，
         // 会监测ExecutorService是否已经关闭，若关闭则返回true，否则返回false。一般情况下会和shutdown方法组合使用。(awaitTermination不会关闭ExecutorService，只是定时检测一下他是否关闭)
         executorService.shutdown();
         //executorService.awaitTermination(100, TimeUnit.SECONDS);
         System.out.println(System.currentTimeMillis() - start);
     }
 }
