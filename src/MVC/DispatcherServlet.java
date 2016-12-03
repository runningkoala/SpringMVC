package MVC;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import container.Request;
import container.Response;
import container.ServletProcessor;
import container.StaticResourceProcessor;
import container.readXml;
import factory.ApplicationContext;
import factory.ClassPathXmlApplicationContext;
import readJsp.readJsp;
import test.test;

public class DispatcherServlet implements Servlet {

	private Map<String, Method> requestMapping = new ConcurrentHashMap<String, Method>();
	private Map<String, LinkedList<String>> requestMapping2 = new ConcurrentHashMap<String, LinkedList<String>>();
	private test testClass;
	
	@Override
	public void init(ServletConfig arg0) throws ServletException {
		//读取xml文件，得到需要的数据
    	String[] locations = {"src/bean.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(locations);
        this.testClass = (test)ctx.getBean("test");
        this.requestMapping = ctx.getRequestMapping();
        this.requestMapping2 = ctx.getRequestMapping2();
	}

	public void service(Request request, Response response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
        String uri = request.getUri();
        boolean flag = false;
        try{
        	if(uri.substring(uri.length()-4).equals(".jsp")){
        		flag = true;
        	}
        } catch(Exception e){}
        String key = uri.substring(uri.lastIndexOf("/"));
        
        Method amethod = requestMapping.get(key);
    	ModelAndView mav = null;
        if(amethod != null){
        	mav = new ModelAndView();
        	for(String apara : requestMapping2.get(key)){
        		mav.setMap(apara, request.getParameter(apara));
        	}
        	Method method = requestMapping.get(key);
        	try {
        		mav = (ModelAndView) method.invoke(testClass, mav);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
        	String filename = readJsp.testJsp(mav.getViewName()+".jsp", mav);
        	ServletProcessor processor = new ServletProcessor();
            processor.process3(request, response, filename);
        }
        //判断请求是否为servlet 
        else if (readXml.ifServlet(uri))  
        {
        	//返回servlet网页
            ServletProcessor processor = new ServletProcessor();  
            processor.process(request, response);  
        }  
        else if(flag)
        {
        	//返回相应jsp网页
        	ServletProcessor processor = new ServletProcessor();  
            processor.process2(request, response);  
        }  
        else
        {
        	//返回一个固定网页
            StaticResourceProcessor processor = new StaticResourceProcessor();  
            processor.process(request, response);  
        } 
		
	}

	@Override
	public void destroy() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		
	}
}
