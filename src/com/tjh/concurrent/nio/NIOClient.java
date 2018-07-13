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

     public static void main(String[] args) throws Exception {
         Socket client = null;
         PrintWriter writer = null;
         BufferedReader reader = null;
         try {
             client = new Socket();
             client.connect(new InetSocketAddress("localhost", 8000));
             writer = new PrintWriter(client.getOutputStream(), true);
             writer.println("Hello EveryBody!");
             writer.flush();
             reader = new BufferedReader(new InputStreamReader(
                 client.getInputStream()));
             System.out.println("from server: " + reader.readLine());
         } catch (Exception e) {
         } finally {
             // 省略资源关闭
         }
     }

 }
