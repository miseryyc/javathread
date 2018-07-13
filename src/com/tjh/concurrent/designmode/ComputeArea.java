 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: ComputeArea
  * Author:   yangchong
  * Date:     2018/7/6 0006 下午 3:18
  * Description: 作业7
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.designmode;

 import java.util.ArrayList;
 import java.util.concurrent.ExecutionException;
 import java.util.concurrent.ForkJoinPool;
 import java.util.concurrent.ForkJoinTask;
 import java.util.concurrent.RecursiveTask;

 /**
  * 作业7<br> 计算给定函数 y=1/x 在定义域 [1,100]上围城与X轴围成的面积，计算步长0.01 此处用ForkJoin来进行计算。分100
  *
  * @author yangchong
  * @create 2018/7/6 0006
  * @since 1.0.0
  */
 public class ComputeArea extends RecursiveTask<Float> {

     private float start = 0f;
     private float end = 0f;

     /**
      * 计算一步长区域面积，近似认为一步长区域是梯形。
      *
      * @param topSide 上底长度
      * @param bottomSide 下底长度
      * @param height 高度
      * @return 面积
      */
     private float computeArea(float topSide, float bottomSide, float height) {
         return (topSide + bottomSide) * height / 2;
     }

     private ComputeArea(float start, float end) {
         this.start = start;
         this.end = end;
     }

     /**
      * 根据x坐标获取y坐标
      *
      * @param x x坐标值
      * @return y坐标值
      */
     private float getY(float x) {
         return 1 / x;
     }

     @Override
     protected Float compute() {
         float sum = 0F;
         float step = 0.01f;
         //计算100*step内的面积 这1000个step是串行执行的。
         if ((end - start) <= step * 10000) {
             while (start <= end) {
                 sum += computeArea(getY(start), getY(start + step), step);
                 start += step;
             }
         } else {
             ArrayList<ComputeArea> tasks = new ArrayList<>();
             while (end >= start) {
                 ComputeArea task;
                 if (end < start + 10000 * step) {
                     task = new ComputeArea(start, end);
                 } else {
                     task = new ComputeArea(start, start + 10000 * step);
                 }
                 tasks.add(task);
                 start = start + 10000 * step;
                 task.fork();
             }

             for (ComputeArea task: tasks) {
                 sum += task.join();
             }
         }
         return sum;
     }

     /*
     结果输出：4.6057763
     通过积分求解 ln(x)来求解结果:4.60517018
      */
     public static void main(String args[]) {
         ForkJoinPool forkJoinPool = new ForkJoinPool();
         ComputeArea computeArea = new ComputeArea(1, 100000);
         ForkJoinTask task = forkJoinPool.submit(computeArea);
         try {
             System.out.println("计算结果："+ task.get() );
         } catch (InterruptedException | ExecutionException e) {
             e.printStackTrace();
         }
     }
 }
