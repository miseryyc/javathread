     /*
      * Copyright (C), 2018-2018, XXX有限公司
      * FileName: ScheduleThreadPoolTest
      * Author:   yangchong
      * Date:     2018/7/5 0005 下午 4:49
      * Description: 调度线程测试类
      * History:
      * <author>          <time>          <version>          <desc>
      * 作者姓名           修改时间           版本号              描述
      */
     package com.tjh.concurrent.threads.pool;

     import java.util.concurrent.Executors;
     import java.util.concurrent.ScheduledExecutorService;
     import java.util.concurrent.TimeUnit;

     /**
      * 〈调度线程测试类〉<br>
      *
      * @author yangchong
      * @create 2018/7/5 0005
      * @since 1.0.0
      */
     public class ScheduleThreadPoolTest {

         public static void main(String args[]) {
             ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);
             pool.scheduleWithFixedDelay(() -> System.out.println(Thread.currentThread().getId()), 0, 1, TimeUnit.SECONDS);
         }
     }
