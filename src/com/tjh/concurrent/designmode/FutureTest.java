 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: FutureTest
  * Author:   yangchong
  * Date:     2018/7/6 0006 上午 11:40
  * Description: Futrue模式测试
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.designmode;

 import java.util.concurrent.Callable;
 import java.util.concurrent.ExecutionException;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.Future;
 import java.util.concurrent.ThreadPoolExecutor;

 /**
  * Future<br>
  *
  * @author yangchong
  * @create 2018/7/6 0006
  * @since 1.0.0
  */
 public class FutureTest {

     static class ResultData implements Callable<Integer>{
         private Integer value;

         public ResultData(Integer value) {
            this.value = value;
         }

         @Override
         public Integer call() throws Exception {
             for (int i = 0; i < 10; i++) {
                 value = value + i;
                 System.out.println("任务1计算中间结果：" + value);
                 Thread.sleep(1000);
             }

             return value;
         }
     }

     /*
        输出结果：
        任务2开始计算...
        任务1计算中间结果：2
        任务1计算中间结果：3
        任务1计算中间结果：5
        任务1计算中间结果：8
        任务1计算中间结果：12
        任务2计算完成
        任务1计算中间结果：17
        任务1计算中间结果：23
        任务1计算中间结果：30
        任务1计算中间结果：38
        任务1计算中间结果：47
        任务1最终结果47
      */
     public static void main(String args[]) {
         ExecutorService pool = Executors.newFixedThreadPool(10);
         Integer result = 2;
         //启动线程进行处理
         Future<Integer> future = pool.submit(new ResultData(result));
         //此处主线程可以进行其他处理

         try {
             System.out.println("任务2开始计算...");
             Thread.sleep(5000);
             System.out.println("任务2计算完成");
             //此处此处主线程阻塞获取结果
             System.out.println("任务1最终结果" + future.get());
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (ExecutionException e) {
             e.printStackTrace();
         }
         pool.shutdown();
     }
 }
