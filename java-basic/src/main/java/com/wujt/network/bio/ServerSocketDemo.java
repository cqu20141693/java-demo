package com.wujt.network.bio;

import lombok.SneakyThrows;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TCP： 面向连接的流式协议，具有可靠性。
 * <p>
 * ServerSocket: TCP服务端套接字，主要用于绑定监听端口；对接入的套接字连接进行接入和业务处理
 * <p>
 * API：
 * public void bind(SocketAddress endpoint, int backlog) : 绑定监听的端口
 * public Socket accept() ： 阻塞获取监听端口监听到的Socket 连接
 * public void close() ： 关闭服务端套接字
 *
 * @author wujt
 */
public class ServerSocketDemo {

    private static class ConnectIOnHandler implements Runnable {
        Socket socket = null;

        public ConnectIOnHandler(Socket socket) {
            this.socket = socket;
        }

        @SneakyThrows
        @Override
        public void run() {
            // 当前线程被关闭或者是socket 关闭
            while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                //3.连接后获取输入流，读取客户端信息
                InputStream is = null;
                InputStreamReader isr = null;
                BufferedReader br = null;
                OutputStream os = null;
                PrintWriter pw = null;
                is = socket.getInputStream();     //获取输入流
                isr = new InputStreamReader(is, "UTF-8");
                br = new BufferedReader(isr);
                String info = null;
                while ((info = br.readLine()) != null) {//循环读取客户端的信息
                    System.out.println("客户端发送过来的信息: " + info);
                }
                OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                writer.write("hello client");
                writer.flush();
                socket.shutdownOutput();//关闭输出流
            }
            socket.shutdownInput();//关闭输入流
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {

        ExecutorService executor = Executors.newFixedThreadPool(100);//线程池
        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdown));

        ServerSocket serverSocket = new ServerSocket();
        int port = 8888;
        SocketAddress socketAddress = new InetSocketAddress(port);
        System.out.println(String.format("Server is starting. ip=127.0.0.1,port=%s", port));
        serverSocket.bind(socketAddress);
        while (!Thread.currentThread().isInterrupted()) {//主线程死循环等待新连接到来
            Socket socket = serverSocket.accept();
            // 因为一次socket 连接可能会很耗时，或者是长链接
            // 当服务端处理缓慢时；会导致套接字任务被放到阻塞队列中
            executor.submit(new ConnectIOnHandler(socket));//为新的连接创建新的线程
        }
    }
}
