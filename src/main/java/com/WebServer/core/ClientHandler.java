package com.WebServer.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

import javax.sound.midi.Synthesizer;

import com.WebServer.http.EmptyRequestException;
import com.WebServer.http.HttpRequest;
import com.WebServer.http.HttpResponse;
import com.WebServer.servlets.HttpServlet;
import com.WebServer.servlets.LoginServlet;
import com.WebServer.servlets.RegServlet;

public class ClientHandler implements Runnable{
	private Socket socket;
	public ClientHandler(Socket socket){
		this.socket=socket;
	}
	
	public void run(){
		try{
			/*
			 * 1:׼������
			 * 2:��������
			 * 3:��Ӧ�ͻ���
			 */
			//1
			HttpRequest request=new HttpRequest(socket);
			HttpResponse response=new HttpResponse(socket);
			/*
			 * 2.��������
			 * 
			 * �����������Դ·��,��webappsĿ¼���ҵ���Ӧ����Դ
			 * ����Դ�����򽫸���Դ��Ӧ���ͻ���
			 * ��û���ҵ�����Դ����Ӧ404ҳ����ͻ���
			 */
			String url=request.getRequestURI();
			//�����жϸ������Ƿ�����һ��ҵ����
			String servletName=ServerContext.getServletMapping(url);
			if(servletName!=null){
				System.out.println("���÷�����أ�"+servletName);
				Class cls=Class.forName(servletName);//���ص���������
				HttpServlet servlet=(HttpServlet)cls.newInstance();//ʵ����
				servlet.Service(request, response);//ִ�и÷���
			}else{
			File file=new File("webapps"+url);
			if(file.exists()){
				System.out.println("����Դ���ҵ�!");
				//����һ��HTTP����Ӧ���ͻ���
				response.setEntity(file);
				System.out.println("��Ӧ���!");
			}else{
				System.out.println("����Դ������!");
				//��Ӧ404ҳ��
				response.setStatusCode(404);
				response.setEntity(new File("webapps/root/404.html"));
			}
		}
			response.flush();//3.��Ӧ�ͻ���
			
			
			
			
		} catch	(EmptyRequestException e){
			System.out.println("�û������˿�����");//��������Լ��ɣ��������κδ���
		} catch (Exception e) {
			//��д��
		}finally{//������ͻ��˶Ͽ����ӵĲ���
			try {
				socket.close();//socketҲҪ�ǵùذ�
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
