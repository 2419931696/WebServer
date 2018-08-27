package com.WebServer.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.WebServer.core.ServerContext;

import java.util.Set;

/**
 * 响应对象
 * 一个响应应当包含三部分：
 * 状态行
 * 响应头
 * 响应正文
 * @author Administrator
 *
 */
public class HttpResponse {
	/*
	 * 状态行相关信息定义
	 */
	private int statusCode=200;//状态代码
	private String statusReason="OK";//状态描述
	
	
	/*
	 * 响应头相关信息定义
	 */
	private Map<String,String> headers=new HashMap<String,String>();
	
	
	/*
	 * 响应正文相关信息定义
	 */
	private File entity;//文件
	
	private Socket socket;//通过Socket获取的输出流，用于给客户端发送响应内容
	private OutputStream out;
	
	public HttpResponse(Socket socket){
		try {
			this.socket=socket;
			this.out=socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void flush(){//将当前响应对象内容发送给客户端
		/*
		 * 1：发送状态行
		 * 2：发送响应头
		 * 3：发送响应正文
		 */
		sendStatusLine();
		sendHeaders();
		sendContent();
	}
	private void sendStatusLine(){//发送状态行
		try{
		String line=ServerContext.protocol+" "+statusCode+" "+statusReason;
		println(line);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void sendHeaders(){//发送响应头
		try {
			Set<Entry<String,String>>entrySet=  headers.entrySet();
			for(Entry<String,String> header:entrySet  ){
				String key=header.getKey();
				String value=header.getValue();
				String line=key+": "+value;
				println(line);
			}
			//单独发送CRLF表示响应头发送完毕
			println("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void sendContent(){//发送响应正文
		if(entity!=null){
			try(FileInputStream fis=new FileInputStream(entity);){
				byte data[]=new byte[1024*10];
				int len=-1;
				while((len=fis.read(data))!=-1){
					out.write(data, 0, len);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public File getEntity() {
		return entity;
	}
	/**
	 * 设置要响应给客户端的实体资源文件
	 * 在设置的同时会自动添加两个响应头：
	 * Content-Type与Content-Length
	 * @param entity
	 */
	public void setEntity(File entity) {
		this.entity = entity;
		this.headers.put("Content-Length", entity.length()+"");
		/*
		 * 设置Content-type时，要先根据文件名的后缀得到对应的值
		 */
		String fileName=entity.getName();
		int index=fileName.lastIndexOf(".")+1;
		String ext=fileName.substring(index);
		String contentType=HttpContext.getContentType(ext);
		this.headers.put("Content-Type",contentType);
	}
	/**
	 * 向客户端发送一行字符串，该字符串发送后会单独发送CR,LF
	 * 
	 */
	private void println(String line){
		try {
			out.write(line.getBytes("ISO8859-1"));
			out.write(HttpContext.CR);//written CR
			out.write(HttpContext.LF);//written LF
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * 设置状态代码
	 * 在设置状态代码的同时会将对应的状态描述默认值自动设置好。
	 * 若希望自行设置描述，可以单独调用对应的方法。
	 * @param statusCode
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		this.statusReason=HttpContext.getStatusReason(statusCode);
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	/**
	 * 向当前响应中设置一个响应头信息
	 */
	public void putHeader(String name,String value){
		this.headers.put(name, value);
	}
}