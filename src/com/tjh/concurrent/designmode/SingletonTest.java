 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: SingletonTest
  * Author:   yangchong
  * Date:     2018/7/6 0006 上午 9:28
  * Description: 单例模式
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.designmode;

 /**
  * 〈单例模式〉<br>
  *
  * @author yangchong
  * @create 2018/7/6 0006
  * @since 1.0.0
  */
 public class SingletonTest {

     static class Singleton {
         private static Singleton singleton = null;
         private static int a = 0;

         public void setValue(int param) {
             this.a = param;
         }

         public int getValue() {
             return a;
         }

         /**
          * 构造函数
          */
         private Singleton() {
             System.out.println("创建类");
         }

         public static Singleton getInstance() {
             if (singleton == null) {
                 singleton =  new Singleton();
             }
             return singleton;
         }
     }

     /*
     输出结果：
     创建类
     9
     9
     9
     9
     9
     9
     9
     9
     9
     9
      */
     public static void main(String args []) {
         Singleton [] singletons = new Singleton[10];
         for (int i = 0; i < 10; i++) {
             singletons[i] = Singleton.getInstance();
             singletons[i].setValue(i);
         }

         for (int i = 0; i < 10; i++) {
             System.out.println(singletons[i].getValue());
         }
     }
 }
