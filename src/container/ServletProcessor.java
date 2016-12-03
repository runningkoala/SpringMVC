package container;
import java.io.File;
import java.io.IOException;
import java.net.URL;  
import java.net.URLClassLoader;  
import javax.servlet.Servlet;  
import javax.servlet.ServletRequest;  
import javax.servlet.ServletResponse;

import readJsp.readJsp;  
  
public class ServletProcessor
{  
    public void process(Request request, Response response)  
    {  
    	//得到路径
        String uri = request.getUri();  
        String servletname = uri.substring(uri.lastIndexOf("/") + 1);  
        URLClassLoader loader = null;
        try  
        {  
            //得到当前路径
            URL[] urls = new URL[1];  
            File classPath = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator);
            //转为url格式
            URL url = classPath.toURI().toURL();
            urls[0]=url;
            //定义加载器
            loader = new URLClassLoader(urls);  
        }  
        catch (IOException e)  
        {  
            System.out.println(e.toString());  
        }  
        Class<?> myClass = null; 
        try  
        {  
            //加载相应类  
            myClass = loader.loadClass(readXml.getPath(servletname));  
        }  
        catch (ClassNotFoundException e)  
        {  
            System.out.println(e.toString());  
        }  
        Servlet servlet = null;  
        try  
        {  
            //实例化servlet类并运行  
            servlet = (Servlet) myClass.newInstance();
            servlet.service((ServletRequest) request, (ServletResponse) response);
        }  
        catch (Exception e)  
        {  
            System.out.println(e.toString());  
        }  
        catch (Throwable e)  
        {  
            System.out.println(e.toString());  
        }  
    }  
    
    public void process2(Request request, Response response)  
    {  
    	//得到路径
        String uri = request.getUri();  
        String jspname = uri.substring(uri.lastIndexOf("/") + 1);
        String[] names = jspname.split("\\.");
        File file = new File(System.getProperty("user.dir") + File.separator + "WebRoot" + File.separator + jspname);
        
        //判断jsp文件是否存在
        if(file.exists()){
        	try {
				readJsp.readFile(jspname);
			} catch (IOException e) {
				// TODO �Զ���ɵ� catch ��
				e.printStackTrace();
			}
        }else{
        	//不存在时返回相应数据
        	String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";  
            try {
				response.output.write(errorMessage.getBytes());
			} catch (IOException e1) {
				// TODO �Զ���ɵ� catch ��
				e1.printStackTrace();
			}
            return; 
        }

        //判断所需文件是否已加载
		File jsp1 = new File(System.getProperty("user.dir") + File.separator 
    			+ "bin" + File.separator + "JspServlet" + File.separator + names[0] + "_jsp.class");
		while(!jsp1.exists()){}
		
		File jsp2 = new File(System.getProperty("user.dir") + File.separator 
    			+ "src" + File.separator + "JspServlet" + File.separator + names[0] + "_jsp.java");
		while(!jsp2.exists()){}
		
        URLClassLoader loader = null;
        try  
        {  
            //得到路径
            URL[] urls = new URL[1];  
            File classPath = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator);
            //转为url格式
            URL url = classPath.toURI().toURL();
            urls[0]=url;
            //为加载器赋值
            loader = new URLClassLoader(urls);  
        }  
        catch (IOException e)  
        {  
            System.out.println(e.toString());  
        }  
        Class<?> myClass = null; 
        try  
        {
            //加载相应类
            myClass = loader.loadClass("JspServlet." + names[0] + "_jsp");
        }  
        catch (ClassNotFoundException e)  
        {  
        	//未找到文件时返回
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";  
            try {
				response.output.write(errorMessage.getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            return; 
        }  
        Servlet servlet = null;  
        try  
        {  
            //实例化servlet并调用相应函数
            servlet = (Servlet) myClass.newInstance();
            servlet.service((ServletRequest) request, (ServletResponse) response);
        }  
        catch (Exception e)  
        {  
            System.out.println(e.toString());  
        }  
        catch (Throwable e)  
        {  
            System.out.println(e.toString());  
        }  
    }  
    
    public void process3(Request request, Response response, String jspname)  
    {
    	//当所需文件存在时程序继续执行
		File jsp1 = new File(System.getProperty("user.dir") + File.separator 
    			+ "bin" + File.separator + "JspServlet" + File.separator + jspname + "_jsp.class");
		while(!jsp1.exists()){}
		File jsp2 = new File(System.getProperty("user.dir") + File.separator 
    			+ "src" + File.separator + "JspServlet" + File.separator + jspname + "_jsp.java");
		while(!jsp2.exists()){}
		
        URLClassLoader loader = null;
        try  
        {  
            //得到文件路径
            URL[] urls = new URL[1];  
            File classPath = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator);
            //将路径转为url格式
            URL url = classPath.toURI().toURL();
            urls[0]=url;
            //为加载器赋值
            loader = new URLClassLoader(urls);  
        }  
        catch (IOException e)  
        {  
            System.out.println(e.toString());  
        }  
        Class<?> myClass = null; 
        try  
        {
            //加载相应文件  
            myClass = loader.loadClass("JspServlet." + jspname + "_jsp");
        }  
        catch (ClassNotFoundException e)  
        {
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";  
            try {
				response.output.write(errorMessage.getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            return; 
        } finally{
        	if(jsp1.exists()){
        		jsp1.delete();
        	}
        	if(jsp2.exists()){
        		jsp2.delete();
        	}
        }
        Servlet servlet = null;  
        try  
        {  
            //实例化相应类并调用相应函数  
            servlet = (Servlet) myClass.newInstance();
            servlet.service((ServletRequest) request, (ServletResponse) response);
        }  
        catch (Exception e)  
        {  
            System.out.println(e.toString());  
        }
    }  

}  