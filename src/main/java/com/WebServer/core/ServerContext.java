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
	//�����ʹ�õ�Э��汾
	public static String protocol="HTTP/1.1";
	
	//�����ʹ�õĶ˿ں�
	public static int port;
	
	//����˽���URLʱʹ�õ��ַ���
	public static String URIEncoding="UTF-8";
	
	//������̳߳����߳�����
	public static int maxThreads=150;
	
	private static Map<String,String>servletMapping=new HashMap<String,String>();//�������ӦServlet���ֵ�ӳ���ϵ
	
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
	 * ����conf/server.xml�����������������ڳ�ʼ��ServerContent��Ӧ������
	 */
	private static void init(){
		/*
		 * ����conf/server.xml�ļ���������ǩ�µ��ӱ�ǩ<Connector>�и����Ե�ֵ�õ��������ڳ�ʼ��
		 * ��Ӧ������
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
