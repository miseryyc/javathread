/*
 * Copyright (C), 2018-2018, XXX有限公司
 * FileName: ComputeNIOClient
 * Author:   Administrator
 * Date:     2018/7/25 0025 上午 9:12
 * Description: 四则混合运算NIO客户端
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.tjh.concurrent.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 基于NIO将参数发送给计算服务器，然后返回计算结果<br>
 *
 * @author yangchong
 * @create 2018/7/25 0025
 * @since 1.0.0
 */

public class ComputeNIOClient {

    /**
     * 获取远程计算结果
     * @param param1 计算入参1
     * @param param2 计算入参2
     */
    private static void getRemoteResult(String param1, String param2) {
        Socket client = null;
        PrintWriter writer = null;
        BufferedReader reader = null;

        try {
            client = new Socket();
            client.connect(new InetSocketAddress("localhost", 9999));

            writer = new PrintWriter(client.getOutputStream(), true);
            writer.print(param1 + "," + param2);
            writer.flush();

            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("输出结果：" + reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                client.close();
                writer.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
    输出结果：102
     */
    public static void main(String args[]) {
        getRemoteResult("100","2");
    }
}
