package com.tjh.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class LockFreeStack<T> {

    private static int size = 10000;

    private static AtomicInteger currentSize = new AtomicInteger(size);

    //申明一个原子类型的对象数组
    private AtomicReferenceArray<T> array;

    //Stack的游标
    private static AtomicInteger current_position = new AtomicInteger(0);

    public LockFreeStack() {
        array = new AtomicReferenceArray<T>(size);
    }

    /**
     * 当数组满时，需要扩容，每次扩容为原来的两倍
     */
    private void grow() {
        AtomicReferenceArray<T> temp = new AtomicReferenceArray<>(currentSize.intValue() * 2);
        copy(array, temp);
        array = temp;
        currentSize.compareAndSet(currentSize.intValue(), currentSize.intValue() * 2);
    }

    private void copy(AtomicReferenceArray<T> src, AtomicReferenceArray<T> dest) {
        AtomicInteger i = new AtomicInteger(0);
        while (i.intValue() < src.length()) {
            dest.set(i.intValue(), src.get(i.intValue()));
            i.incrementAndGet();
        }
    }

    /**
     * Stack的Push方法
     * @param element 要插入的元素
     */
    public void push(T element) {
        //push元素的时候，如果满了，则需要扩容
        if(current_position.intValue() == currentSize.intValue()) {
            grow();
        }

        array.compareAndSet(current_position.intValue(), null, element);
        current_position.incrementAndGet();
    }

    /**
     * Stack的Pop方法
     * @return 返回pop出的元素
     */
    public T pop() {
        if(current_position.intValue() == 0) {
            System.out.println("栈中没有元素，无法pop");
            return null;
        }

        current_position.decrementAndGet();
        T value = array.get(current_position.intValue());
        array.compareAndSet(current_position.intValue(), value, null);
        return value;
    }

    /**
     * 打印输出所有的元素
     */
    public void printSize () {
        System.out.println(array.length());
    }

    /*
    程序运行结果：
    10000

    几次的结果都是10000
     */
    public static void main(String args []) {
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        Thread [] t = new Thread[10000];
        for (int i = 0; i < 10000; i++) {
            t[i] = new Thread(() -> stack.push(1));
            t[i].start();
        }

        for (int i = 0; i < 10000; i++) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stack.printSize();
    }
}
