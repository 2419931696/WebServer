package com.WebServer.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleConsumer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ServerContext {
	//服务端使用的协议版本
	public static String protocol="HTTP/1.1";
	
	//服务端使用的端口号
	public static int port;
	
	//服务端解析URL时使用的字符集
	public static String URIEncoding="UTF-8";
	
	//服务端线程池中线程数量
	public static int maxThreads=150;
	
	private static Map<String,String>servletMapping=new HashMap<String,String>();//请求与对应Servlet名字的映射关系
	
	static{
		init();
		initServletMapping();
	}
	private static void initServletMapping(){
		SAXReader reader=new SAXReader();
		try {
			Document document=reader.read(new File("conf/servlets.xml"));
			Element root=document.getRootElement();
			List<Element> list=root.elements("servlet");
			for(Element ele:list){
				String url=ele.attributeValue("url");
				String className=ele.attributeValue("className");
				System.out.println(url+","+className);
				servletMapping.put(url, className);
			}
			
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/*
	 * 解析conf/server.xml，将所有配置项用于初始化ServerContent对应的属性
	 */
	private static void init(){
		/*
		 * 解析conf/server.xml文件，将根标签下的子标签<Connector>中各属性的值得到，并用于初始化
		 * 对应的属性
		 */
		SAXReader reader=new SAXReader();
		try {
			Document document= reader.read(new File("conf/server.xml"));
			Element root=document.getRootElement();
			Element Connector=root.element("Connector");
			
			
			protocol=Connector.attributeValue("protocol");
			port=Integer.parseInt(Connector.attributeValue("port"));
			URIEncoding=Connector.attributeValue("URIEncoding");
			maxThreads=Integer.parseInt(Connector.attributeValue("maxThreads"));
			
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		System.out.println("port:"+ServerContext.port);
	}
	public static String getServletMapping(String uri){
		return servletMapping.get(uri);
	}
	
	
	public static void main(String[] args) {
		System.out.println(port);
	}
}
