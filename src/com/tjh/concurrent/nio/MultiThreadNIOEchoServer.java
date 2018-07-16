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

     private static Map<Socket, Long> geym_time_stat = new HashMap<>();

     class EchoClient {

         private LinkedList<ByteBuffer> outq;

         EchoClient() {
             outq = new LinkedList<>();
         }

         //因为是多线程处理，所有需要将所有读数据线程的数据保存到队列中。
         LinkedList<ByteBuffer> getOutputQueue() {
             return outq;
         }

         public void enqueue(ByteBuffer bb) {
             outq.addFirst(bb);
         }
     }

     class HandleMsg implements Runnable {

         SelectionKey sk;
         ByteBuffer bb;

         HandleMsg(SelectionKey sk, ByteBuffer bb) {
             super();
             this.sk = sk;
             this.bb = bb;
         }

         @Override
         public void run() {
             // 获取跟当前key相关联的对象
             EchoClient echoClient = (EchoClient) sk.attachment();
             echoClient.enqueue(bb);
             System.out.println("将读数据插入队列中并设置兴趣到读和写" + System.currentTimeMillis());
             //设置对读和写感兴趣
             sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
             //处理完了之后唤醒Selector处理数据。
             System.out.println("开始唤醒selector" + System.currentTimeMillis());
             selector.wakeup();
         }
     }

     @SuppressWarnings("InfiniteLoopStatement")
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
         //注册感兴趣的事件，此处对accept事件感兴趣,将Selector和Channel绑定关联起来
         ssc.register(selector, SelectionKey.OP_ACCEPT);
         //无限循环监听
         while (true) {
             System.out.println("开始阻塞等待。" + System.currentTimeMillis());
             int num = selector.select();
             System.out.println("有" + num + "个通道的数据准备好了。" + System.currentTimeMillis());
             //如果没有准备好的Channel则直接进行下一次循环监听
             if (num <= 0) {
                 continue;
             }
             //根据Selector获取已经准备好的SelectKey集合
             Set readyKeys = selector.selectedKeys();
             Iterator i = readyKeys.iterator();
             long e;
             //循环迭代每个SelectKey，一个SelectKey与一个Channel对应
             while (i.hasNext()) {
                 SelectionKey sk = (SelectionKey) i.next();
                 //每次遍历之后都要删除，避免下次监听的时候重复遍历。这次准备好的Channel不删除，下次监听的时候还会获取得到。
                 i.remove();
                 //判断acceptable表示客户端连接已经准备好了。
                 if (sk.isAcceptable()) {
                     System.out.println("连接服务器成功。" + System.currentTimeMillis());
                     //处理accept事件
                     doAccept(sk);
                 }
                 //判断read事件，进行读数据。
                 //每次向选择器注册通道时就会创建一个选择键。通过调用某个键的 cancel 方法、关闭其通道，或者通过关闭其选择器来取消 该键之前，
                 //它一直保持有效。取消某个键不会立即从其选择器中移除它；相反，会将该键添加到选择器的已取消键集，以便在下一次进行选择操作时移除它。
                 //可通过调用某个键的 isValid 方法来测试其有效性。
                 else if (sk.isValid() && sk.isReadable()) {
                     //Channel从客户端读取数据时的开始时间
                     if (!geym_time_stat.containsKey(((SocketChannel) sk
                         .channel()).socket())) {
                         geym_time_stat.put(
                             ((SocketChannel) sk.channel()).socket(),
                             System.currentTimeMillis());
                     }
                     //进行读数据操作
                     doRead(sk);
                 }
                 //判断write事件，进行读数据。
                 else if (sk.isValid() && sk.isWritable()) {
                     doWrite(sk);
                     //将数据写入到channel中后的时间
                     e = System.currentTimeMillis();
                     long b = geym_time_stat.remove(((SocketChannel) sk
                         .channel()).socket());
                     System.out.println("spend:" + (e - b) + "ms");
                     //整个服务端响应客户端的时间是从读channel读数据到写数据到channel这段期间花的时间
                 }
             }
             Thread.sleep(2000);
         }
     }

     /**
      * 写数据到通道中
      * @param sk SelectionKey
      */
     private void doWrite(SelectionKey sk) {
         System.out.println("开始写数据。" + System.currentTimeMillis());
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
             //如果数据队列中的数据写完了，则将数据从队列中移除
             if (bb.remaining() == 0) {
                 outq.removeLast();
             }
         } catch (Exception e) {
             disconnect(sk);
         }
         //如果所有的队列数据都没了，则经兴趣设置成读操作。
         if (outq.size() == 0) {
             sk.interestOps(SelectionKey.OP_READ);
         }
     }

     /**
      * 多线程从通道中读数据
      * @param sk SelectionKey
      */
     private void doRead(SelectionKey sk) {
         System.out.println("监听到读操作事件，进行读操作" + System.currentTimeMillis());
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
             disconnect(sk);
             return;
         }
         bb.flip();
         tp.execute(new HandleMsg(sk, bb));
     }

     private void disconnect(SelectionKey skey) {
         skey.cancel();
     }

     private void doAccept(SelectionKey sk) {
         ServerSocketChannel server = (ServerSocketChannel) sk.channel();
         SocketChannel clientChannel;
         try {
             clientChannel = server.accept();
             clientChannel.configureBlocking(false);
             System.out.println("客户端连接完毕，注册监听读操作"+ System.currentTimeMillis());
             SelectionKey clientKey = clientChannel.register(selector,
                 SelectionKey.OP_READ);
             EchoClient echoClient = new EchoClient();
             clientKey.attach(echoClient);
             InetAddress clientAddress = clientChannel.socket().getInetAddress();
             System.out.println("Accepted connection from "
                 + clientAddress.getHostAddress());
         } catch (Exception e) {
            e.printStackTrace();
         }
     }

     public static void main(String[] args) {
         MultiThreadNIOEchoServer echoServer = new MultiThreadNIOEchoServer();
         try {
             echoServer.startServer();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

 }
