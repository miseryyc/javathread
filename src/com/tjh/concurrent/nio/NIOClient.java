 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: NIOClient
  * Author:   yangchong
  * Date:     2018/7/12 0012 上午 11:03
  * Description: NIO客户端
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.nio;

 import java.io.BufferedReader;
 import java.io.InputStreamReader;
 import java.io.PrintWriter;
 import java.net.InetSocketAddress;
 import java.net.Socket;

 /**
  * NIO客户端<br>
  *
  * @author yangchong
  * @create 2018/7/12 0012
  * @since 1.0.0
  */
 public class NIOClient {

     public static void main(String[] args) {
         Socket client;
         PrintWriter writer;
         BufferedReader reader;
         try {
             client = new Socket();
             System.out.println("开始连接服务端。" + System.currentTimeMillis());
             client.connect(new InetSocketAddress("localhost", 8000));
             System.out.println("开始向服务端发送数据。" + System.currentTimeMillis());
             writer = new PrintWriter(client.getOutputStream(), true);
             writer.println("Hello EveryBody!");
             writer.flush();
             System.out.println("向服务端发送数据完毕。" + System.currentTimeMillis());

             System.out.println("从服务端获取数据。" + System.currentTimeMillis());
             reader = new BufferedReader(new InputStreamReader(
                 client.getInputStream()));
             System.out.println("from server: " + reader.readLine());
             System.out.println("从服务端获取数据完毕。" + System.currentTimeMillis());
             client.close();
             writer.close();
             reader.close();

         } catch (Exception e) {
             e.printStackTrace();
         }
     }

 }
