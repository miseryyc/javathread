 /*
  * Copyright (C), 2018-2018, XXX有限公司
  * FileName: MultiThreadNIOEchoServer
  * Author:   yangchong
  * Date:     2018/7/12 0012 上午 10:53
  * Description: NIO多线程网络通信
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.tjh.concurrent.nio;

 import java.net.InetAddress;
 import java.net.InetSocketAddress;
 import java.net.Socket;
 import java.nio.ByteBuffer;
 import java.nio.channels.SelectionKey;
 import java.nio.channels.Selector;
 import java.nio.channels.ServerSocketChannel;
 import java.nio.channels.SocketChannel;
 import java.nio.channels.spi.SelectorProvider;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.LinkedList;
 import java.util.Map;
 import java.util.Set;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;

 /**
  * 多线程网络通信<br>
  *
  * @author yangchong
  * @create 2018/7/12 0012
  * @since 1.0.0
  */
 public class MultiThreadNIOEchoServer {

     private Selector selector;
     private ExecutorService tp = Executors.newCachedThreadPool();

     public static Map<Socket, Long> geym_time_stat = new HashMap<Socket, Long>();

     class EchoClient {
         private LinkedList<ByteBuffer> outq;

         EchoClient() {
             outq = new LinkedList<ByteBuffer>();
         }

         public LinkedList<ByteBuffer> getOutputQueue() {
             return outq;
         }

         public void enqueue(ByteBuffer bb) {
             outq.addFirst(bb);
         }
     }

     class HandleMsg implements Runnable {
         SelectionKey sk;
         ByteBuffer bb;

         public HandleMsg(SelectionKey sk, ByteBuffer bb) {
             super();
             this.sk = sk;
             this.bb = bb;
         }

         @Override
         public void run() {
             // 获取跟当前key相关联的对象
             EchoClient echoClient = (EchoClient) sk.attachment();
             echoClient.enqueue(bb);
             //设置对读和写感兴趣
             sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
             //处理完了之后唤醒Selector处理数据。
             selector.wakeup();
         }
     }

     private void startServer() throws Exception {
         //创建Selector
         selector = SelectorProvider.provider().openSelector();
         //创建ServerSocketChannel
         ServerSocketChannel ssc = ServerSocketChannel.open();
         //设置非阻塞通道
         ssc.configureBlocking(false);
         //获取ip地址
         InetSocketAddress isa = new InetSocketAddress(8000);
         //服务端的ServerSocket绑定ip地址
         ssc.socket().bind(isa);
         // 注册感兴趣的事件，此处对accept事件感兴趣,将Selector和Channel绑定关联起来
         ssc.register(selector, SelectionKey.OP_ACCEPT);
         //无限循环监听
         while(true){
             //Select()阻塞的方式获取数据
             int num = selector.select();
             Set readyKeys = selector.selectedKeys();
             Iterator i = readyKeys.iterator();
             long e = 0;
             while (i.hasNext()) {
                 SelectionKey sk = (SelectionKey) i.next();
                 i.remove();
                 if (sk.isAcceptable()) {
                     doAccept(sk);
                 } else if (sk.isValid() && sk.isReadable()) {
                     if (!geym_time_stat.containsKey(((SocketChannel) sk
                         .channel()).socket())) {
                         geym_time_stat.put(
                             ((SocketChannel) sk.channel()).socket(),
                             System.currentTimeMillis());
                     }
                     doRead(sk);
                 } else if (sk.isValid() && sk.isWritable()) {
                     doWrite(sk);
                     e = System.currentTimeMillis();
                     long b = geym_time_stat.remove(((SocketChannel) sk
                         .channel()).socket());
                     System.out.println("spend:" + (e - b) + "ms");
                 }
             }
         }
     }

     private void doWrite(SelectionKey sk) {
         // TODO Auto-generated method stub
         SocketChannel channel = (SocketChannel) sk.channel();
         EchoClient echoClient = (EchoClient) sk.attachment();
         LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();
         ByteBuffer bb = outq.getLast();
         try {
             int len = channel.write(bb);
             if (len == -1) {
                 disconnect(sk);
                 return;
             }
             if (bb.remaining() == 0) {
                 outq.removeLast();
             }
         } catch (Exception e) {
             // TODO: handle exception
             disconnect(sk);
         }
         if (outq.size() == 0) {
             sk.interestOps(SelectionKey.OP_READ);
         }
     }

     private void doRead(SelectionKey sk) {
         // TODO Auto-generated method stub
         SocketChannel channel = (SocketChannel) sk.channel();
         ByteBuffer bb = ByteBuffer.allocate(8192);
         int len;
         try {
             len = channel.read(bb);
             if (len < 0) {
                 disconnect(sk);
                 return;
             }
         } catch (Exception e) {
             // TODO: handle exception
             disconnect(sk);
             return;
         }
         bb.flip();
         tp.execute(new HandleMsg(sk, bb));
     }

     private void disconnect(SelectionKey sk) {
         // TODO Auto-generated method stub
         //省略略干关闭操作
     }

     private void doAccept(SelectionKey sk) {
         // TODO Auto-generated method stub
         ServerSocketChannel server = (ServerSocketChannel) sk.channel();
         SocketChannel clientChannel;
         try {
             clientChannel = server.accept();
             clientChannel.configureBlocking(false);
             SelectionKey clientKey = clientChannel.register(selector,
                 SelectionKey.OP_READ);
             EchoClient echoClinet = new EchoClient();
             clientKey.attach(echoClinet);
             InetAddress clientAddress = clientChannel.socket().getInetAddress();
             System.out.println("Accepted connection from "
                 + clientAddress.getHostAddress());
         } catch (Exception e) {
             // TODO: handle exception
         }
     }

     public static void main(String[] args) {
         // TODO Auto-generated method stub
         MultiThreadNIOEchoServer echoServer = new MultiThreadNIOEchoServer();
         try {
             echoServer.startServer();
         } catch (Exception e) {
             // TODO: handle exception
         }

     }

 }
