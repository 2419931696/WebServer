package com.WebServer.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.WebServer.http.HttpRequest;
import com.WebServer.http.HttpResponse;

/**
 * Servlet是JAVA EE标准定义的内容
 * 
 * 
 * @author Administrator
 *
 */
public class RegServlet extends HttpServlet{
	public void Service(HttpRequest request,HttpResponse response){
		System.out.println("开始处理注册。。");
		/*
		 * 处理注册流程
		 * 1:通过request获取用户表单提交上来的注册用户信息
		 * 2:将该信息写入到文件user.dat中
		 * 3:设置response对象。将注册成功页面响应给客户端
		 */
		try(RandomAccessFile raf=new RandomAccessFile("user.dat", "rw");){
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String nickname=request.getParameter("nickname");
		int age=Integer.parseInt(request.getParameter("age"));
		System.out.println(username+","+password+","+nickname+","+age);
		/*
		 * 2.将注册信息写入user.dat文件
		 * 每条记录占用100字节，其中，用户名，密码，昵称为字符串，各占用32字节，年龄为int值占用4字节
		 */
		raf.seek(raf.length());
		byte data[]=username.getBytes("UTF-8");
		data=Arrays.copyOf(data, 32);
		raf.write(data);//前32写用户名
		
		data=password.getBytes("UTF-8");
		data=Arrays.copyOf(data, 32);
		raf.write(data);//写密码
		
		data=nickname.getBytes("UTF-8");
		data=Arrays.copyOf(data, 32);//写昵称
		raf.write(data);
		
		raf.writeInt(age);//写年龄
		
		//3响应注册成功页面
		File file=new File("webapps/myweb/reg_success.html");
		response.setEntity(file);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("注册处理完毕！");
	}
}
