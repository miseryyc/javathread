 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: ThreadPoolExtTest
  * Author:   yangchong
  * Date:     2018/7/5 0005 下午 5:17
  * Description: 线程池扩展测试
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.threads.pool;

 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.LinkedBlockingQueue;
 import java.util.concurrent.ThreadPoolExecutor;
 import java.util.concurrent.TimeUnit;

 /**
  * 线程池在线程运行前后都可以进行一些处理工作<br>
  *
  * @author yangchong
  * @create 2018/7/5 0005
  * @since 1.0.0
  */
 public class ThreadPoolExtTest {

     /*
     运行结果：
     开始执行
     执行过程中
     准备执行
     结束执行
      */
     public static void main(String args[]) {
         //此处不用Exectors工厂类，直接用ThreadPoolExecutor的构造函数
         ExecutorService pool = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.SECONDS,
             new LinkedBlockingQueue<>()){
             @Override
             protected void afterExecute(Runnable r, Throwable t) {
                 System.out.println("准备执行");
             }

             @Override
             protected void beforeExecute(Thread t, Runnable r) {
                 System.out.println("开始执行");
             }

             @Override
             protected void terminated() {
                 System.out.println("结束执行");
             }
         };

         pool.submit(() -> {
             try {
                 System.out.println("执行过程中");
                 Thread.sleep(2000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         });

         pool.shutdown();

     }
 }
