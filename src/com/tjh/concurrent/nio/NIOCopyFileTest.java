 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: NIOCopyFileTest
  * Author:   yangchong
  * Date:     2018/7/10 0010 下午 5:17
  * Description: 通过NIO复制文件
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.nio;

 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.nio.ByteBuffer;
 import java.nio.channels.Channel;
 import java.nio.channels.FileChannel;

 /**
  * 通过NIO的Channel和Buffer来复制文件<br>
  *
  * @author yangchong
  * @create 2018/7/10 0010
  * @since 1.0.0
  */
 public class NIOCopyFileTest {

     /**
      * 文件复制方法
      * @param res 被复制的文件路径
      * @param des 目的文件路径
      * @throws IOException IO异常
      */
     private static void nioCopyFile(String res, String des) throws IOException {
         FileInputStream inputStream = new FileInputStream(res);
         FileOutputStream outputStream = new FileOutputStream(des);

         FileChannel inChannel = inputStream.getChannel();
         FileChannel outChannel  = outputStream.getChannel();

         ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
         while (true) {
             byteBuffer.clear();
             if(-1 != inChannel.read(byteBuffer)) {
                 byteBuffer.flip();
                 outChannel.write(byteBuffer);
                 continue;
             }
             break;
         }
         inChannel.close();
         outChannel.close();
     }

     /*
      结果:产生一个复制文件，文件名为17外校真题_1.docx
      */
     public static void main(String args[]) {
         try {
             NIOCopyFileTest.nioCopyFile("E:\\杨冲\\17外校真题.docx","E:\\杨冲\\17外校真题_1.docx");
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }
