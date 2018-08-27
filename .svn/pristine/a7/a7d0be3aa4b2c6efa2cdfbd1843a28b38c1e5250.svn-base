package com.WebServer.demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
public class ShowAllUserDemo {
	public static void main(String[] args) throws IOException {
		/*RandomAccessFile file=new RandomAccessFile("user.dat","r");
		for(int i=0;i<file.length()/100;i++){
			byte[] a=new byte[32];
			file.read(a);
			String name=new String(a,"GBK").trim();//把数据以GBK字符组输出,trim()代表把前后空格输出
			System.out.println("用户名:"+name);
			
			file.read(a);
			String password=new String(a,"GBK").trim();
			System.out.println("密码;"+password);
			
			file.read(a);
			String name1=new String(a,"GBK").trim();
			System.out.println("昵称:"+name1);
			
			System.out.println(file.readInt());
			
		}*/
		RandomAccessFile file=new RandomAccessFile("user.dat","r");
		for(int i=0;i<file.length()/100;i++){
			byte a[]=new byte[32];
			file.read(a);
			String name=new String(a,"UTF-8").trim();
			
			file.read(a);
			String password=new String(a,"UTF-8").trim();
			
			file.read(a);
			String name1=new String(a,"UTF-8").trim();
			
			
			System.out.println(name+","+password+","+name1+","+file.readInt());
			
		}
		
		
		
		
		
		
		
		
		
		
		
	}
}
