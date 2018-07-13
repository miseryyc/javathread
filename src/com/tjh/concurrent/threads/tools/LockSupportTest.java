 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: LockSupportTest
  * Author:   yangchong
  * Date:     2018/7/4 0004 上午 9:07
  * Description: SupportLock
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.threads.tools;

 import java.util.Random;
 import java.util.concurrent.locks.LockSupport;

 /**
  * SupportLock <br> 〈SupportLock〉
  *
  * @author yangchong
  * @create 2018/7/4 0004
  * @since 1.0.0
  */
 public class LockSupportTest implements Runnable {

     @Override
     public void run() {
         try {
             Thread.sleep(new Random().nextInt(10) * 1000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         LockSupport.park(this);
     }

     public static void main(String args[]) {
         Thread t1 = new Thread(new LockSupportTest());
         t1.setName("t1");
         t1.start();
         LockSupport.unpark(t1);
     }
 }
