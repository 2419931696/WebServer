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
			 * 1:准备工作
			 * 2:处理请求
			 * 3:响应客户端
			 */
			//1
			HttpRequest request=new HttpRequest(socket);
			HttpResponse response=new HttpResponse(socket);
			/*
			 * 2.处理请求
			 * 
			 * 根据请求的资源路径,从webapps目录中找到对应的资源
			 * 若资源存在则将该资源响应给客户端
			 * 若没有找到该资源则响应404页面给客户端
			 */
			String url=request.getRequestURI();
			//首先判断该请求是否请求一个业务处理
			String servletName=ServerContext.getServletMapping(url);
			if(servletName!=null){
				System.out.println("利用反射加载："+servletName);
				Class cls=Class.forName(servletName);//加载到方法区中
				HttpServlet servlet=(HttpServlet)cls.newInstance();//实例化
				servlet.Service(request, response);//执行该方法
			}else{
			File file=new File("webapps"+url);
			if(file.exists()){
				System.out.println("该资源已找到!");
				//发送一个HTTP的响应给客户端
				response.setEntity(file);
				System.out.println("响应完毕!");
			}else{
				System.out.println("该资源不存在!");
				//响应404页面
				response.setStatusCode(404);
				response.setEntity(new File("webapps/root/404.html"));
			}
		}
			response.flush();//3.响应客户端
			
			
			
			
		} catch	(EmptyRequestException e){
			System.out.println("用户发送了空请求！");//空请求忽略即可，无需做任何处理
		} catch (Exception e) {
			//不写。
		}finally{//处理与客户端断开连接的操作
			try {
				socket.close();//socket也要记得关啊
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
