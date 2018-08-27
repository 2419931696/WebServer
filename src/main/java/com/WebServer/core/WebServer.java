package com.WebServer.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 	WebServer����
 * ����TCP
 * @author Administrator
 *
 */
public class WebServer {
	private ServerSocket server;
	private ExecutorService threadPool;//�������ڴ���ͻ���������߳�
	public WebServer(){//���췽��,������ʼ��������
		try{
		System.out.println("���ڳ�ʼ��������..");
		server=new ServerSocket(ServerContext.port);
		System.out.println("��������ʼ���ɹ�!");
		threadPool=Executors.newFixedThreadPool(ServerContext.maxThreads);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void start(){////�������������
		try{//ѭ�����տͻ�������Ĺ�����ʱ������,���Խ׶�ֻ����һ������
			while(true){
					System.out.println("�ȴ��ͻ�����...");
					Socket socket=server.accept();
					System.out.println("һ���ͻ���������!");
					
					//����һ���߳�������ͻ�������
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
