 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: FileMapToMemoryTest
  * Author:   yangchong
  * Date:     2018/7/11 0011 上午 11:19
  * Description: 文件映射到内存
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.nio;

 import java.io.IOException;
 import java.io.RandomAccessFile;
 import java.nio.MappedByteBuffer;
 import java.nio.channels.FileChannel;
 import java.nio.channels.FileChannel.MapMode;

 /**
  * 文件映射到内存<br>
  *
  * @author yangchong
  * @create 2018/7/11 0011
  * @since 1.0.0
  */
 public class FileMapToMemoryTest {

     private static void fileMapToMemory(String fileName) {
         try {
             RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
             FileChannel fileChannel = raf.getChannel();
             //将文件内容写入到mbb对象中，mbb内存更改之后写入文件。
             MappedByteBuffer mbb = fileChannel.map(MapMode.READ_WRITE,0 , raf.length());
             while (mbb.hasRemaining()) {
                 System.out.println(mbb.get());
             }
             //在index之后插入数据。
             mbb.put(0, (byte)98);
             raf.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public static void main(String args[]) {
         FileMapToMemoryTest.fileMapToMemory("E:\\程序代码\\程序测试文件\\test.txt");
     }
 }
