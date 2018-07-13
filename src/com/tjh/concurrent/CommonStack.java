package com.tjh.concurrent;

import java.util.ArrayList;

public class CommonStack<T> {
    private static int size = 20;

    private static int currentSize = size;

    //申明一个原子类型的对象数组
    private ArrayList<T> array;

    //Stack的游标
    private static int current_position = 0;

    public CommonStack() {
        array = new ArrayList<T>(size);
    }

    /**
     * 当数组满时，需要扩容，每次扩容为原来的两倍
     */
    private void grow() {
        ArrayList<T> temp = new ArrayList<T>(currentSize * 2);
        copy(array, temp);
        array = temp;
        currentSize = currentSize * 2;
    }

    private void copy(ArrayList<T> src, ArrayList<T> dest) {
        int i = 0;
        while (i < src.size()) {
            dest.add(src.get(i));
            i++;
        }
    }

    /**
     * Stack的Push方法
     * @param element 要插入的元素
     */
    public void push(T element) {
        //push元素的时候，如果满了，则需要扩容
        if(current_position == currentSize) {
            grow();
        }

        array.add(element);
        current_position ++;
    }

    /**
     * Stack的Pop方法
     * @return 返回pop出的元素
     */
    public T pop() {
        if(current_position == 0) {
            System.out.println("栈中没有元素，无法pop");
            return null;
        }

        current_position -- ;
        T value = array.get(current_position);
        array.remove(current_position);
        return value;
    }

    /**
     * 打印输出所有的元素
     */
    public void printSize () {
        System.out.println(array.size());
    }

    /*
    程序运行结果:
    9992

    几次运行结果都不一样，但都达不到10000
     */
    public static void main(String args []) {
        CommonStack<Integer> stack = new CommonStack<Integer>();
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
