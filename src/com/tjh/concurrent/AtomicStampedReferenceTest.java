package com.tjh.concurrent;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest {
    static AtomicStampedReference<Integer> money = new AtomicStampedReference<Integer>(19,0);

    /*
     输出结果：
     余额小于20元，充值成功，余额：20元
     大于10元
     成功消费10元，余额：10
     没有足够的金额
     没有足够的金额
     没有足够的金额
     没有足够的金额
     没有足够的金额
     没有足够的金额
     没有足够的金额
     没有足够的金额
     没有足够的金额
     */
    public static void main(String[] args) {
        //模拟多个线程同时更新后台数据库，为用户充值，此时要求只给用户一次充值的机会。如果不用stampAtomic，则会不断的重复充值。
        for (int i = 0; i < 3; i++) {
            final int timestamp = money.getStamp();
            new Thread(){
                public void  run() {
                    while (true){
                        Integer m = money.getReference();
                        if(m < 20) {
                            if(money.compareAndSet(m,  +20, timestamp, timestamp + 1)) {
                                System.out.println("余额小于20元，充值成功，余额："+money.getReference() + "元");
                                try {
                                    sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }else {
                            //System.out.println("余额大于20元，无需充值);
                            break;
                        }
                    }
                }
            }.start();
        }

        //用户消费线程，模拟消费行为
        new Thread(){
            public void run() {
                for (int i = 0; i < 100; i++) {
                    while (true){
                        int timestamp = money.getStamp();
                        Integer m = money.getReference();
                        if(m >= 10) {
                            System.out.println("大于10元");
                            if(money.compareAndSet(m,m-10, timestamp, timestamp + 1)) {
                                System.out.println("成功消费10元，余额："+money.getReference());
                                break;
                            }
                        }else {
                            System.out.println("没有足够的金额");
                            break;
                        }
                    }
                }
            }
        }.start();
    }
}
