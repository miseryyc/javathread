 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: CountDownLatchTest
  * Author:   yangchong
  * Date:     2018/7/3 0003 下午 8:07
  * Description: 计数器锁测试类
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.threads.tools;

 import java.util.Random;
 import java.util.concurrent.CountDownLatch;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;

 /**
  * 倒数计数器，作用是检查多个任务运行时，每个任务（线程）是否执行完毕，如果执行完毕则计数器减一，直到所有的任务都完成<br> 〈计数器锁测试类〉
  *
  * @author yangchong
  * @create 2018/7/3 0003
  * @since 1.0.0
  */
 public class CountDownLatchTest implements Runnable {

     //此处设置有10个任务需要执行
     private static  CountDownLatch countDownLatch = new CountDownLatch(10);

     @Override
     public void run() {
         try {
             Thread.sleep(new Random().nextInt(10)*1000);
             System.out.println(Thread.currentThread().getId() + "的任务完成！");
             countDownLatch.countDown();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
     }

     /*
     执行结果：
     17的任务完成！
    20的任务完成！
    14的任务完成！
    15的任务完成！
    11的任务完成！
    18的任务完成！
    19的任务完成！
    12的任务完成！
    13的任务完成！
    16的任务完成！
    所有的任务完成！
      */
     public static void main(String args[]) {
         ExecutorService pool = Executors.newFixedThreadPool(10);
         CountDownLatchTest test = new CountDownLatchTest();
         for (int i = 0; i < 10; i++) {
             pool.submit(test);
         }
         try {
             countDownLatch.await();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         System.out.println("所有的任务完成！");
         pool.shutdown();
     }
 }
