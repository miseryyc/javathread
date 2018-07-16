 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: ThreadLocalTest
  * Author:   yangchong
  * Date:     2018/7/16 0016 下午 10:52
  * Description: 线程局部测试
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.lockoptimizing;

 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;

 /**
  * 线程局部的例子<br>
  *
  * @author yangchong
  * @create 2018/7/16 0016
  * @since 1.0.0
  */
 public class ThreadLocalTest {

     //private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

     public static class ParseDate implements Runnable {
         int i;
         ParseDate(int i){
             this.i = i;
         }

         @Override
         public void run() {
             try{
                 //sdf被多线程访问的时候会抛出异常，sdf不是线程安全的。
                 //如果用synchronized关键字封装parse方法是可行的，但是性能相对较差。
                 //Date t = sdf.parse("2015-03-29 19:29:" + i%60);

                 //用ThreadLocal能提高性能
                 if(tl.get()  == null) {
                     tl.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                 }
                 //用ThreadLocal将SimpleDateFormat转换成局部变量，为每个线程分配一个实例
                 Date t = tl.get().parse("2015-03-29 19:29:" + i%60);

                 System.out.println(i + ":" + t);
             }catch (ParseException e) {
                 e.printStackTrace();
             }
         }
     }

     public static void main(String args []) {
         ExecutorService pool = Executors.newFixedThreadPool(10);
         for (int i = 0; i < 1000; i++) {
             pool.execute(new ParseDate(i));
         }
         pool.shutdown();
     }
 }
