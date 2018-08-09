/*
 * Copyright (C), 2018-2018, XXX有限公司
 * FileName: AIOClient
 * Author:   Administrator
 * Date:     2018/7/27 0027 下午 4:18
 * Description: AIO客户端
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.tjh.concurrent.aio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * AIO客户端<br>
 *
 * @author Administrator
 * @create 2018/7/27 0027
 * @since 1.0.0
 */

public class AIOClient {

    public static void main(String args []) {
        AsynchronousSocketChannel asc = null;
        try {
            asc = AsynchronousSocketChannel.open();
            asc.connect(new InetSocketAddress("localhost", 8888), null,
                new CompletionHandler<Void, Object>() {
                    @Override
                    public void completed(Void result, Object attachment) {
                        System.out.println("连接成功");
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        System.out.println("连接失败");
                        exc.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                asc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
