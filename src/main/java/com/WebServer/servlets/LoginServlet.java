package com.WebServer.servlets;

import java.io.File;
import java.io.RandomAccessFile;

import com.WebServer.http.HttpRequest;
import com.WebServer.http.HttpResponse;

public class LoginServlet extends HttpServlet{
	public void Service(HttpRequest request,HttpResponse response){
		//2.
		try(RandomAccessFile raf=new RandomAccessFile("user.dat", "r")){
			boolean check=false;
			//1.获取用户登录信息
			try{
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			for(int i=0;i<raf.length()/100;i++){
				//3.将指针移动到该条的头位置
				raf.seek(0+i*100);
				byte data[]=new byte[32];
				raf.read(data);
				String localName=new String(data,"UTF-8").trim();
				raf.seek(32+i*100);
				raf.read(data);
				String localPassword=new String(data,"UTF-8").trim();
				if(username.equals(localName)&&password.equals(localPassword)){
					check=true;
					break;
				}
			}
			}catch(NullPointerException e){
				System.out.println("出现了空指针");
			}
			if(check){
					System.out.println("登录成功！");
					response.setEntity(new File("webapps/myweb/login_success.html"));
			}else{
				System.out.println("登录失败！");
				response.setEntity(new File("webapps/myweb/login_fail.html"));
			}
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
