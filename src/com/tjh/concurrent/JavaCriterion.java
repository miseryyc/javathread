package com.tjh.concurrent;

/**
 * JAVA语言规范
 */
public class JavaCriterion {
    /**
     * 中文充当函数名
     * 输出结果：中文方法哦
     */
    public static void 打印() {
        System.out.println("中文方法哦");
    }

    /**
     * Java词法结构
     */
    public static void wordStruct() {

        //输出结果：23,
        //JDK7中加入了下划线可以分割int和Long类型。
        //例如：0 2 0372 0xDada_Cafe 1996 0x00_FF__00_FF
        System.out.println(2_3);


    }

    public static void main(String[] args) {
        wordStruct();
    }
}
