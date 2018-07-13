/**
 * Copyright (C), 2018-2018, XXX有限公司
 * FileName: DeadLockChecker
 * Author:   yangchong
 * Date:     2018/7/2 0002 下午 4:24
 * Description: 死锁检查
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.tjh.concurrent.threads.tools;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 死锁检查<br>
 * 〈死锁检查〉
 *
 * @author yangchong
 * @create 2018/7/2 0002
 * @since 1.0.0
 */
public class DeadLockChecker {
    private final static ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
    final static Runnable deadlockCheck = new Runnable() {
        @Override
        public void run() {
            while (true) {
                long[] deadlockedThreadIds = mbean.findDeadlockedThreads();
                if (deadlockedThreadIds != null) {
                    ThreadInfo[] threadInfos = mbean.getThreadInfo(deadlockedThreadIds);
                    for (Thread t : Thread.getAllStackTraces().keySet()) {
                        for (ThreadInfo threadInfo : threadInfos) {
                            if (t.getId() == threadInfo.getThreadId()) {
                                t.interrupt();
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
            }
        }
    };

    public static void check() {
        Thread t = new Thread(deadlockCheck);
        t.setDaemon(true);
        t.start();
    }
}
