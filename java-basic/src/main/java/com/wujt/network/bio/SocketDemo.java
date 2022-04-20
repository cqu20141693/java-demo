package com.wujt.network.bio;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Socket : TCP客户端套接字，用于建立点对点连接，每一个socket 对应一个文件句柄
 * <p>
 * API：
 * public void connect(SocketAddress endpoint) : 请求建立socket 连接
 * public OutputStream getOutputStream() ： 获取输出流
 * public InputStream getInputStream() ： 获取输入流
 * public void shutdownOutput() ：关闭输出流
 * public void shutdownInput() ： 关闭输入流
 * public synchronized void close() ： 关闭套接字
 *
 * @author wujt
 */
public class SocketDemo {
    public static void main(String[] args) throws IOException {
        //1.创建客户端Socket，指定服务器地址和端口
        String host = "127.0.0.1";
        int port = 8888;
        Socket socket = new Socket();

        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        socket.connect(socketAddress);
        //2.获取输出流，向服务器端发送信息
        OutputStream os = socket.getOutputStream();//字节输出流
        PrintWriter pw = new PrintWriter(os);//将输出流包装为打印流
        // 得到CLIENT的输入流，从输入流出取出传输的数据
        BufferedReader reader_from_client = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //获取客户端的IP地址
        InetAddress address = InetAddress.getLocalHost();
        String ip = address.getHostAddress();
        pw.write("客户端：~" + ip + "~ 接入服务器！！");
        pw.println();
        pw.write("Done");
        pw.println();
        pw.flush();
        socket.shutdownOutput();//关闭输出流,但是还可以接收到数据
        String line = null;
        System.out.print("from server message:");
        while ((line = reader_from_client.readLine()) != null) {
            System.out.println(line);
        }
        //如果不发送关闭的包;就会导致服务器出现问题;
        // socket.shutdownOutput();//关闭输出流,但是还可以接收到数据
        socket.close();  //关闭连接;不会再接收响应数据
        System.out.println("B success");
    }
}
