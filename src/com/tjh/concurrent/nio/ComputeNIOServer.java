/*
 * Copyright (C), 2018-2018, XXX有限公司
 * FileName: ComputeNIOServer
 * Author:   Administrator
 * Date:     2018/7/25 0025 上午 9:37
 * Description: NIO计算服务端
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.tjh.concurrent.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NIO计算服务端，将客户端传过来的参数进行解析，然后计算，最后将结果返回给客户端<br>
 *
 * @author Administrator
 * @create 2018/7/25 0025
 * @since 1.0.0
 */

public class ComputeNIOServer {

    private Selector selector;
    private ExecutorService pool = Executors.newFixedThreadPool(10);

    /**
     * 任务缓冲区，将客户端传入的请求放入任务处理缓冲区
     */
    private class TaskBuffer {

        private LinkedList<ByteBuffer> queue;

        TaskBuffer() {
            queue = new LinkedList<>();
        }

        public ByteBuffer pop() {
            return queue.pop();
        }

        public void push(ByteBuffer param) {
            queue.push(param);
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    /**
     * 处理Accept事件
     * @param key SelectionKey
     */
    private void handleAccept(SelectionKey key) {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel;
        try {
            socketChannel = ssc.accept();
            socketChannel.configureBlocking(false);
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
            TaskBuffer taskBuffer = new TaskBuffer();
            selectionKey.attach(taskBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据操作任务
     */
    private class ReadingTask implements Runnable {
        private SelectionKey selectionKey;
        private ByteBuffer byteBuffer;

        ReadingTask(SelectionKey sk, ByteBuffer bb) {
            selectionKey = sk;
            byteBuffer = bb;
        }

        @Override
        public void run() {
            TaskBuffer taskBuffer = (TaskBuffer)selectionKey.attachment();
            taskBuffer.push(byteBuffer);
            selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            selector.wakeup();
        }
    }

    /**
     * 计算 A + B 四则运算
     * @param byteBuffer 运算参数
     * @return 运算结果
     */
    private static ByteBuffer getAddResult(ByteBuffer byteBuffer) {
        String res = convertToString(byteBuffer);
        String [] params = res.split(",");
        Integer sum = 0;
        for (int i = 0; i < params.length; i++) {
            try{
                sum += Integer.parseInt(params[i]);
            }catch (Exception e) {
                System.out.println("请输入整数进行运算");
            }
        }
        //如果客户端用readLine(),则当读到'\r'或者'\n'时才能读出结果，否则一直阻塞。
        String resStr = sum.toString() + "\n";
        return ByteBuffer.wrap(resStr.getBytes());
    }

    /**
     * 将ByteBuffer转成String
     * @return 字符串
     */
    private static String convertToString(ByteBuffer byteBuffer) {
        Charset charset;
        CharsetDecoder decoder;
        CharBuffer charBuffer;
        try
        {
            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            charBuffer = decoder.decode(byteBuffer.asReadOnlyBuffer());
            return charBuffer.toString();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }

    private class WriteTask implements Runnable {
        private SelectionKey selectionKey;

        WriteTask(SelectionKey sk) {
            selectionKey = sk;
        }

        @Override
        public void run() {
            TaskBuffer taskBuffer = (TaskBuffer) selectionKey.attachment();

            if(taskBuffer.isEmpty()) {
                selectionKey.interestOps(SelectionKey.OP_READ);
            }

            ByteBuffer bb = taskBuffer.pop();
            ByteBuffer result = getAddResult(bb);
            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
            try {
                int len = socketChannel.write(result);
                if(len == -1) {
                    disconnect(selectionKey);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //selector.wakeup();
        }
    }


    /**
     * 多线程读取客户端的数据
     * @param key SelectionKey
     */
    private void handleRead(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        ByteBuffer bb = ByteBuffer.allocate(1024);
        try {
            int len = socketChannel.read(bb);
            if(len < 0) {
                disconnect(key);
                return;
            }
        } catch (IOException e) {
            disconnect(key);
            e.printStackTrace();
        }
        bb.flip();
        pool.submit(new ReadingTask(key, bb));
    }

    /**
     * 多线程计算返回结果
     * @param key SelectionKey
     */
    private void handleWrite(SelectionKey key) {
        pool.submit(new WriteTask(key));
    }

    /**
     * 取消Selector
     * @param key SelectionKey
     */
    private void disconnect(SelectionKey key) {
        key.cancel();
    }

    /**
     * 服务器启动主函数
     */
    private void startServer() {
        try {
            selector = SelectorProvider.provider().openSelector();
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            //用socket去bind
            ssc.socket().bind(new InetSocketAddress(9999));
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                int num = selector.select();
                System.out.println("有" + num + "个通道的数据准备好了。" + System.currentTimeMillis());
                Set keys = selector.selectedKeys();
                Iterator iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = (SelectionKey) iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }

                    if (key.isValid() && key.isReadable()) {
                        handleRead(key);
                    }

                    if (key.isValid() && key.isWritable()) {
                        handleWrite(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ComputeNIOServer server = new ComputeNIOServer();
        server.startServer();
    }
}
