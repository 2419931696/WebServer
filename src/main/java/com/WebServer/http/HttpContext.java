package com.WebServer.http;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class HttpContext {
	public static final int CR=13;
	/*
	 * ASC码中对应的回车符
	 */
	public static final int LF=10;
	/*
	 * ASC码中对应的换行符
	 */
	/**
	 * 状态代码与描述的关系
	 * key:状态代码
	 * value:状态描述
	 */
	private static Map<Integer,String>statusCode_Reason_Mapping=new HashMap<Integer,String>();
	/**
	 * 介质类型的映射
	 * key:资源后缀名
	 * value:Content-Type对应的值
	 */
	private static Map<String,String>mimeMapping=new HashMap<String,String>();
	
	
	static{
		initStatusCodeReasonMapping();
		initMimeMapping();
	}
	private static void initMimeMapping(){
		
		/*
		 * 通过解析conf/web.xml文件来完成初始化操作
		 * 
		 * 1:将web.xml文档中根标签下所有名为：
		 * <mime-mapping>的子标签解析出来
		 * 
		 * 2:并将其对应的两个子标签：
		 * <extension>中间的文本作为key
		 * <mime-type>中间的文本作为value
		 * 来初始化mimeMapping这个Map
		 * 
		 */
		try{
		SAXReader reader=new SAXReader();
		Document document=reader.read(new FileInputStream("conf/web.xml"));
		Element root=document.getRootElement();//获取根标签
		List<Element> list=root.elements("mime-mapping");//获取所有名为mime-mapping的子标签
		for(Element mimeEle:list){
			Element extension=mimeEle.element("extension");
			String key=extension.getText();//key=text
			Element mime_type=mimeEle.element("mime-type");
			String value=mime_type.getText();//value=text/html
			mimeMapping.put(key, value);//放入查找表中
		}
		
		
		
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 初始化状态代码与对应描述的关系
	 */
	
	private static void initStatusCodeReasonMapping(){//初始化状态代码与对应描述的关系
		statusCode_Reason_Mapping.put(200, "OK");
		statusCode_Reason_Mapping.put(201, "Created");
		statusCode_Reason_Mapping.put(202, "Accepted");
		statusCode_Reason_Mapping.put(204, "No Content");
		statusCode_Reason_Mapping.put(301, "Moved Permanently");
		statusCode_Reason_Mapping.put(302, "Moved Temporarily");
		statusCode_Reason_Mapping.put(304, "Not Modified");
		statusCode_Reason_Mapping.put(400, "Bad Request");
		statusCode_Reason_Mapping.put(401, "Unauthorized");
		statusCode_Reason_Mapping.put(403, "Forbidden");
		statusCode_Reason_Mapping.put(404, "Not Found");
		statusCode_Reason_Mapping.put(500, "Internal Server Error");
		statusCode_Reason_Mapping.put(501, "Not Implemented");
		statusCode_Reason_Mapping.put(502, "Bad Gateway");
		statusCode_Reason_Mapping.put(503, "Service Unavailable");
	}
	
	public static String getStatusReason(int statusCode){//输入运行状态，得文本描述
		return statusCode_Reason_Mapping.get(statusCode);
	}
	public static String getContentType(String ext){//输入文件类型，得响应头类型
		return mimeMapping.get(ext);
	}
	public static void main(String[] args) {//测试
		String line=getContentType("js");
		System.out.println(line);
	}
	
}
