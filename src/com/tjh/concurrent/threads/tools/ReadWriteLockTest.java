 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: ReadWriteLockTest
  * Author:   yangchong
  * Date:     2018/7/3 0003 下午 4:45
  * Description: 读写锁测试类
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.threads.tools;

 import java.util.concurrent.locks.ReentrantReadWriteLock;

 /**
  * 读写锁测试类<br> 〈读写锁测试类〉
  *
  * @author yangchong
  * @create 2018/7/3 0003
  * @since 1.0.0
  */
 public class ReadWriteLockTest {

     private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

     private synchronized void readWithSyn() {
         for (int i = 0; i < 10; i++) {
             try {
                 Thread.sleep(200);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
     }

     private void readWithReadLock() {
         try {
             reentrantReadWriteLock.readLock().lock();
             for (int i = 0; i < 10; i++) {
                 Thread.sleep(200);
             }
         } catch (InterruptedException e) {
             e.printStackTrace();
         } finally {
             reentrantReadWriteLock.readLock().unlock();
         }
     }

     //程序输出结果：
     //阻塞线程读数据耗时：14170ms
     //ReadWriteLock线程读数据耗时：2004ms
     public static void  main(String args[]) throws InterruptedException {
         ReadWriteLockTest readWriteLockTest = new ReadWriteLockTest();
         Thread [] th = new Thread[10];
         long start = System.currentTimeMillis();
         for (int i = 0; i < 10; i++) {
             th[i] = new Thread(() -> readWriteLockTest.readWithSyn());
             th[i].start();
         }

         for (int i = 0; i < 3; i++) {
             th[i].join();
         }
         long end = System.currentTimeMillis();
         System.out.println("阻塞线程读数据耗时：" + (end-start) + "ms");

         start = System.currentTimeMillis();
         for (int i = 0; i < 3; i++) {
             th[i] = new Thread(() -> readWriteLockTest.readWithReadLock());
             th[i].start();
         }

         for (int i = 0; i < 3; i++) {
             th[i].join();
         }
         end = System.currentTimeMillis();
         System.out.println("ReadWriteLock线程读数据耗时：" + (end-start) + "ms");
     }
 }
