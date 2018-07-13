 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: ConditionTest
  * Author:   yangchong
  * Date:     2018/7/3 0003 下午 2:36
  * Description: Condition类代码示例
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.threads.tools;

 import java.util.concurrent.locks.Condition;
 import java.util.concurrent.locks.ReentrantLock;

 /**
  * Condition是和ReentrantLock结合使用的，类似synchronized关键字和Object.wait()与Object.notify()结合使用一样<br>
  * Condition也有类似的await()和signal()两个方法<br>
  * 〈Condition类代码示例〉
  *
  * @author yangchong
  * @create 2018/7/3 0003
  * @since 1.0.0
  */
 public class ConditionTest implements Runnable {

     public static ReentrantLock lock = new ReentrantLock();
     public static Condition condition = lock.newCondition();

     @Override
     public void run() {
         try{
             lock.lock();
             condition.await();
             System.out.println("Thread is going on");
         }catch (InterruptedException e){
             e.printStackTrace();
         }finally {
             lock.unlock();
         }
     }

     //运行结果 Thread is going on
     public static void main(String [] args) {
         ConditionTest conditionTest = new ConditionTest();
         Thread t1 = new Thread(conditionTest);
         t1.start();
         try {
             Thread.sleep(2000);
             lock.lock();
             //只有先获得锁才能唤醒，所以要加在lock和unlock之间执行，参照synchronize关键字中执行wait和notify一样。
             condition.signal();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }finally {
             lock.unlock();
         }
     }
 }
