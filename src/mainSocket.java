

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import MVC.DispatcherServlet;
import container.Request;
import container.Response;
import container.readXml;  
  
public class mainSocket
{  
    //定义关闭
    private static final String CLOSE_COMMAND = "/SHUTDOWN";
    private boolean close = false;  
  
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException  
    {  
    	//实例化本身并调用,读取xml文件
    	mainSocket servlet = new mainSocket();
    	readXml.readX();
    	servlet.waiting();
    }  
  
    public void waiting()  
    {
    	DispatcherServlet ds = new DispatcherServlet();
    	try {
			ds.init(null);
		} catch (ServletException e2) {
			// TODO 自动生成的 catch 块
			e2.printStackTrace();
		}
    	//添加服务器处理
        ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
		} catch (UnknownHostException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}  
        
        //服务未关闭时，建立连接 
        while (!close)  
        {  
        	//定义变量
            Socket socket = null;  
            InputStream ins = null;  
            OutputStream os = null;  
            try  
            {
            	//得到输入输出
                socket = serverSocket.accept();  
                ins = socket.getInputStream();  
                os = socket.getOutputStream();  
                //实例化请求类
                Request request = new Request(ins);  
                request.parse();  
                //实例化返回类  
                Response response = new Response(os);  
                response.setRequest(request);
                ds.service(request, response); 
                //关闭连接
                socket.close();  
                //判断是否需要关闭连接  
                close = request.getUri().equals(CLOSE_COMMAND);  
            }  
            catch (Exception e)  
            {  
                e.printStackTrace();  
                System.exit(0);
            }  
        }  
    }  
}  