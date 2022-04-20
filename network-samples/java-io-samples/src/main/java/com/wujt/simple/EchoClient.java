package com.wujt.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Socket 客户端套接字
 * public new Socket() 创建一个套接字
 * public void connect(SocketAddress endpoint) 连接服务端Socket
 * public OutputStream getOutputStream() 获取套接字输出流
 * public InputStream getInputStream() 获取套接字输入流
 * public synchronized void close() 关闭套接字
 *
 * @author wujt
 */
public class EchoClient {
    public static void main(String[] args) throws IOException {
        Socket client = null;
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            client = new Socket();
            client.connect(new InetSocketAddress("localhost", 8686));
            printWriter = new PrintWriter(client.getOutputStream(), true);
            printWriter.println("hello");
            printWriter.flush();

            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));            //读取服务器返回的信息并进行输出
            System.out.println("来自服务器的信息是：" + bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            // 如果客户端一直不关闭链路，大量连接时服务端的线程资源为瓶颈
            printWriter.close();
            bufferedReader.close();
            client.close();
        }
    }
}
