package com.WebServer.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 	WebServer主类
 * 基于TCP
 * @author Administrator
 *
 */
public class WebServer {
	private ServerSocket server;
	private ExecutorService threadPool;//管理用于处理客户端请求的线程
	public WebServer(){//构造方法,用来初始化服务器
		try{
		System.out.println("正在初始化服务器..");
		server=new ServerSocket(ServerContext.port);
		System.out.println("服务器初始化成功!");
		threadPool=Executors.newFixedThreadPool(ServerContext.maxThreads);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void start(){////服务端启动方法
		try{//循环接收客户端请求的工作暂时不启动,测试阶段只接收一次请求
			while(true){
					System.out.println("等待客户连接...");
					Socket socket=server.accept();
					System.out.println("一个客户端连接了!");
					
					//启动一个线程来处理客户端请求
					ClientHandler handler=new ClientHandler(socket);
					threadPool.execute(handler);
					
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		WebServer server=new WebServer();
		server.start();
	}
	
}
