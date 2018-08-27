package com.WebServer.servlets;

import com.WebServer.http.HttpRequest;
import com.WebServer.http.HttpResponse;

public abstract class HttpServlet{
	/**
	 * 这里定义一个抽象方法service,要求所有的Servlet都必须含有该方法，用于处理业务。但是由于不同
	 * 的Servlet处理的业务不同。对此，该方法才是抽象方法。
	 * @param request
	 * @param response
	 */
	public abstract void Service(HttpRequest request,HttpResponse response);
	
	
	
}
