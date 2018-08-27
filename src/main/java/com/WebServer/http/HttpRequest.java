package com.WebServer.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.WebServer.core.ServerContext;



/**
 * 请求对象
 * HttpRequest的每一个实例用于表示客户端发送过来的一个具体的请求内容.
 * 一个请求由三部分构成:
 * 请求行,消息头,消息正文.
 * @author Administrator
 *
 */
public class HttpRequest{
	
	
	/*
	 * 请求行相关信息定义
	 */
	private String method;//请求方式
	private String url;//请求资源路径
	private String protocol;//协议版本
	private String requestURI;//url的请求部分 url中"?"左侧内容
	private String queryString;//url中的参数部分 url中"?"右侧内容
	/**
	 * key=passwork
	 * value=qq8;
	 */
	private Map<String,String> parameters=new HashMap<String,String>();//存入所有参数
	/*
	 * 消息头相关信息定义
	 */
	private Map<String, String> headers=new HashMap<String,String>();
	/*
	 * 消息正文相关信息定义
	 */
	private Socket socket;//对应客户端的Socket
	private InputStream in;//用于读取客户端发送过来消息的输入流
	public HttpRequest(Socket socket) throws EmptyRequestException{//构造方法,用来初始化HttpRequest
		try {
			/*
			 * 1.解析请求行
			 * 2.解析消息头
			 * 3.解析消息正文
			 */
			this.socket=socket;
			this.in=socket.getInputStream();
			parseRequestLine();
			parseHeaders();
			parseContent();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void parseRequestLine() throws EmptyRequestException{//解析请求行
		try{
		System.out.println("正在解析请求行...");
		String line=readLine();
		
		/*
		 * 解析请求行的步骤:
		 * 1:将请求行内容按照空格拆分为三部分
		 * 2:分别将三部分内容设置到对应的属性上:method,url,protocol
		 * 
		 * 
		 * 这里将来可能会抛出数组下标越界,原因在于HTTP协议中也有所提及,允许客户端链接后发送空请求
		 * (实际就是什么也没发过来).这时候若解析请求行是拆分不出三项的.后面遇到再解决
		 */
		String data[]=line.split("\\s");
		if(data.length<3){
			throw new EmptyRequestException();
		}
		method=data[0];
		url=data[1];
		parseUrl();//进一步解析url
		protocol=data[2];
		
		System.out.println("method:"+method);
		System.out.println("url:"+url);
		System.out.println("protocol:"+protocol);
		
		
		System.out.println("解析请求行完毕!");
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void parseUrl(){//进一步解析url
		/*
		 * 首先将url进行转码，将含有的%XX内容转换为对应的字符
		 * 例如：原url的样子大致为：
		 * myweb/login?username=%E8%8C%83%E4%BC%A0%E5%A5%87
		 * 
		 * 经过URLDecoder.decoder(url,"UTF-8")后，得到的字符串的样子为：
		 * myweb/login?username=范传奇
		 */
		//URLDecoder decoder=new URLDecoder();
		try {
			this.url=URLDecoder.decode(url,ServerContext.URIEncoding);//把%E8之类的东西转化为UTF-8所对应的文字
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * 1.先判断当前url是否含有参数部分(是否含有"?")
		 * 若没有参数部分，则直接将url赋值给requestURI,
		 * 若含有问号才进行下面的操作
		 * 含有问号再进行下面的操作
		 * 2.按照"?"将url拆分为两部分
		 * 		将?之前的内容设置到属性requestURI上
		 * 		将?之后的内容设置到属性queryString上
		 * 3:将queryString内容进行进一步解析
		 * 		首先按照"&"拆分出每一个参数。然后再将每个参数按照"="拆分为参数名与参数值，并put到
		 * 		属性parameters这个Map中
		 */
		String data[]=url.split("\\?");//?表示0-1次
		requestURI=data[0];
		/*
		 * 这里根?拆分后之所以要判断数组长度是否>1,原因在于，有的请求发送过来可能如下：
		 *	/myweb/reg?
		 *	?后面实际没有带任何参数。(页面form表单中所有的输入域都没有指定name属性时就会出现
		 *这样的情况，如果不判断，可能会出现下标越界的情况。)
		 */
		if(data.length>1){
		queryString=data[1];
		}
		if(url.contains("?")){
			parseParameters(queryString);
			
		System.out.println("queryString:"+queryString);
		System.out.println("requestURI:"+requestURI);
	}
		else{
			requestURI=url;
		}
	}
	/**
	 * 解析参数部分，该内容改格式应当为:name=value&name=value&...
	 * @param paraLine
	 */
	private void parseParameters(String paraLine){
		
		String paras[]=paraLine.split("&");
		for(String para:paras){
			String arr[]=para.split("=");
			/*
			 * 这里判断的原因是因为，如果在表单中某个输入框没有输入值，那么传递过来的数据样子
			 * 会是：/myweb/reg?username=&password...如果没有输入，右边没有内容，按照=打
			 * 断是只有一段的，如果赋值2段会出现下标越界异常
			 */
			if(arr.length>1){
				parameters.put(arr[0], arr[1]);
			}else{
				parameters.put(arr[0],null);
			}
		}
	}
	
	private void parseHeaders(){//解析消息头
		System.out.println("正在解析消息头...");
		try{
			/*
			 * 循环调用readLine方法读取每一行字符串如果读取到的字符串是空字符串则表示单独读取到了CRLF
			 * ,那么表示消息头部分读取完毕,停止循环即可.
			 * 否则读取一行字符串后应当是一个消息头的内容,接下来将该字符串按照": "拆分为两项,第一项是消息
			 * 头的内容,第二项为对应的值,存入到属性headers即可.
			 */
			while(true){
				String line=readLine();
				if("".equals(line)){
					break;
				}
				System.out.println(line);
				String data[]=line.split(":\\s");
				headers.put(data[0], data[1]);
			}
		
		
		
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("解析消息头完毕!");
		
	}
	private void parseContent(){//解析消息正文
		System.out.println("正在解析消息正文...");
		try {
			/*
			 * 1:根据消息头中是否含有Content-Length来判断当前请求是否含有消息正文
			 */
			if(headers.containsKey("Content-Length")){
				int length=Integer.parseInt(headers.get("Content-Length"));
				//2.根据长度读取消息正文的数据
				try {
					byte data[]=new byte[length];
					in.read(data);
					
					//3.根据消息头Content-Type判断正文内容
					String ContentType=headers.get("Content-Type");
					if("application/x-www-form-urlencoded".equals(ContentType)){
						//将消息正文字节转换为字符串
						String line=new String(data,"ISO8859-1");
						System.out.println("post表单提交数据："+line);
						try {
							line=URLDecoder.decode(line,ServerContext.URIEncoding);
						} catch (Exception e) {
							e.printStackTrace();
						}
						//解析参数
						parseParameters(line);
					}
					//将来可以添加其他分支，判断其他种类的数据
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("解析消息正文完毕!");
	}
	private String readLine() throws IOException{//读一行数据
		StringBuilder builder=new StringBuilder();//builder是一个char数组,增删减改会比String快,因为String需要new对象
		char c1='a';
		char c2='a';
		int len=-1;
		while((len=in.read())!=-1){
			c2=(char)len;
			if(c1==HttpContext.CR&&c2==HttpContext.LF){//如果 c1=CR 并且c2=LF,跳出循环
				break;
			}
			builder.append(c2);
			c1=c2;
		}
		return builder.toString().trim();//toString,把char数组转化为字符串,trim把空白字符CR删除
	}
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	public String getRequestURI() {
		return requestURI;
	}
	public String getQueryString() {
		return queryString;
	}
	/**
	 * 根据给定的参数名获取对应的参数值
	 * @param name
	 * @return
	 */
	public String getParameter(String name){
		return this.parameters.get(name);
	}
	
}
