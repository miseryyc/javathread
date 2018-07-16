/*
 * Copyright (C), 2018-2018, XXX有限公司
 * FileName: LockBias
 * Author:   Administrator
 * Date:     2018/7/16 0016 上午 11:09
 * Description: 锁偏向例子
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.tjh.concurrent.lockoptimizing;

import java.util.List;
import java.util.Vector;

/**
 * 锁偏向例子<br>
 *
 * @author Administrator
 * @create 2018/7/16 0016
 * @since 1.0.0
 */

public class LockBias {

    private static List<Integer> numberList = new Vector<>();

    /*
    输出结果：
    -XX:-UseBiasedLocking 时间：3440
    -XX:-UseBiasedLocking -XX:BiasedLockingStartupDelay=0 时间：2919
    结论：
    测试结果表明，锁偏向能提升性能。
     */
    public static void main(String args[]){
        long begin = System.currentTimeMillis();
        int count = 0;
        int startnum = 0;
        while (count < 10000000) {
            numberList.add(startnum);
            startnum += 2;
            count ++;
        }

        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }

}
