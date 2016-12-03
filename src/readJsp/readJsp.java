package readJsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import MVC.ModelAndView;

public class readJsp {
	  
	public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "WebRoot";
	
    public static void change(String file, String filename) throws IOException  
    {
    	//���ı���jsp����StringBuffer
    	StringBuffer content = new StringBuffer();
    	String[] names = filename.split("\\.");
    	content.append("package JspServlet;" + "\r\n");
    	int p = file.indexOf("%>", file.lastIndexOf("<%@"));
    	p += 2;
    	int p4 = 0;
    	int p5 = 0;
    	int p6 = 0;
    	int p7 = 0;
    	//��jsp�ļ�����Ҫ������ļ�д���ļ�ͷ
    	while(true){
    		p4 = file.indexOf("import", p4);
    		if(p4 == -1 || p4 > p){
    			break;
    		} else{
    			p5 = file.indexOf("\"", p4);
    			p6 = file.indexOf("\"", p5+1);
    			p7 = file.indexOf(";", p4);
    			if(p5<p6){
    				String[] datas = file.substring(p5+1, p7).split(",");
    				for(int i=0; i<datas.length; i++){
    					content.append("import " + datas[i] + ";\r\n");
    				}
    			}
    		}
    		p4 = p6+1;
    	}
    	//���������Ҫ������
    	content.append("import javax.servlet.*;" + "\r\n"
    			+ "import java.io.IOException;" + "\r\n"
    			+ "import java.io.PrintWriter;" + "\r\n"
    			+ "public class " 
    			+ names[0] + "_jsp" 
    			+ " implements Servlet {" + "\r\n"
    			+ "public void init(ServletConfig config) throws ServletException {}" + "\r\n"
    			+ "public void service(ServletRequest request, ServletResponse response)" + "\r\n"
    			+ "throws ServletException, IOException {" + "\r\n"
    			+ "PrintWriter out = response.getWriter();\r\n");
    	
    	//��Ӿ�����ļ�����
    	content.append("out.println(\"");
    	int p2 = 0;
    	int p3 = 0;
    	int j;
    	while(true){
    		//���html�ļ��Ĳ���
    		p2 = file.indexOf("<%=", p);
    		p3 = file.indexOf("<%", p);
    		if(p3 == -1){
    			String[] datas = file.substring(p).split("\n");
    			for(int i=0; i<datas.length; i++){
    				String[] datas2 = datas[i].split("\"");
    				for(j=0; j<datas2.length-1; j++){
    					content.append(datas2[j] + "\\\"");
    				}
    				content.append(datas2[j] + "\");\r\nout.println(\"");
    			}
    			content.append("\");");
    			break;
    		//���<% %>��<%= %> ����
    		} else{
    			String[] datas = file.substring(p,p3).split("\n");
    			for(int i=0; i<datas.length; i++){
    				String[] datas2 = datas[i].split("\"");
    				for(j=0; j<datas2.length-1; j++){
    					content.append(datas2[j] + "\\\"");
    				}
    				content.append(datas2[j] + "\");\r\nout.println(\"");
    			}
    			content.append("\");\r\n");
    			p = file.indexOf("%>", p3);
    			//����һ����ǩ��<%ʱ���
        		if(p2 == -1 || p3 < p2){
        			String[] datas12 = file.substring(p3+2, p).split("\n");
        			for(int i=0; i<datas12.length; i++){
        				content.append(datas12[i] + "\r\n");
        			}
        	    	content.append("out.println(\"");
        	    //����ǩ��<%=ʱ���
        		}else {
        			String[] datas12 = file.substring(p2+3, p).split("\n");
        			for(int i=0; i<datas12.length; i++){
        				content.append("out.println(" + datas12[i] + ");\r\n");
        			}
        	    	content.append("out.println(\"");
        		}	
        		p += 2;
    		}
    	}
    	//���������
    	content.append("}" + "\r\n"
    			+ "public void destroy() {}" + "\r\n"
    			+ "public String getServletInfo() {" + "\r\n"
    			+ "return null;" + "\r\n"
    			+ "}" + "\r\n"
    			+ "public ServletConfig getServletConfig() {" + "\r\n"
    			+ "return null;}}");
    	int c1, c2;
    	while(content.indexOf("${", 0) != -1){
    		c1 = content.indexOf("${", 0);
    		c2 = content.indexOf("}", c1);
    		content.replace(c1, c2+1, "null");
    	}
    	//输出到文件
    	outToFile(filename, content);
    }
    	
    private static String outToFile(String filename, StringBuffer content) {
    	// TODO �Զ���ɵķ������
    	String[] jspname = filename.split("\\."); 
    	File jsp = new File(System.getProperty("user.dir") + File.separator 
    			+ "src" + File.separator + "JspServlet" + File.separator + jspname[0] + "_jsp.java");
		try {
			//���ļ�����ݴ����ļ�
			FileOutputStream fos = new FileOutputStream(jsp);
            PrintWriter pw = new PrintWriter(fos);
            pw.write(content.toString().toCharArray());
            pw.flush();
            pw.close();
            fos.close();
		} catch (IOException e) {
			// TODO �Զ���ɵ� catch ��
			e.printStackTrace();
		}
		return filename;
	}

	//���ڶ�ȡjsp�ļ�  
    public static void readFile(String uri) throws IOException  
    {
        FileInputStream fins = null;
        String filecontent = "";
        try  
        {  
            //����Ӧ��jsp�ļ����ж�ȡ
            File file = new File(WEB_ROOT, uri);  
            StringBuilder sb = new StringBuilder();
            String s ="";
            BufferedReader br = new BufferedReader(new FileReader(file));

            while( (s = br.readLine()) != null) {
            sb.append(s + "\n");
            }

            //�ر��ļ���
            br.close();
            filecontent = sb.toString();
        }  
        catch (Exception e)  
        {  
        	//�Ҳ����ļ�ʱ����
            filecontent = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";  
        }  
        finally  
        {  
        	//�ر�����
            if (fins != null)  
                fins.close();  
        }  
        //��jsp��ʽ��Ϊservlet
        change(filecontent, uri);
    }

    public static String testJsp(String filename, ModelAndView mav) throws IOException  
    {
        UUID uuid = UUID.randomUUID();
        String guid = uuid.toString().replace("-", "");
        String name = "a" + guid.substring(0, 12);
        
        FileInputStream fins = null;
        String file = "";
        try  
        {  
            File afile = new File(WEB_ROOT, filename);  
            StringBuilder sb = new StringBuilder();
            String s ="";
            BufferedReader br = new BufferedReader(new FileReader(afile));

            while( (s = br.readLine()) != null) {
            	sb.append(s + "\n");
            }
            br.close();
            file = sb.toString();
        } catch (Exception e)  
        {
            file = "HTTP/1.1 404 File Not Found\n" + "Content-Type: text/html\n" + "Content-Length: 23\n" + "\n" + "<h1>File Not Found</h1>";  
        } finally {  
            if (fins != null)  
                fins.close();  
        }  
    	//���ı���jsp����StringBuffer
    	StringBuffer content = new StringBuffer();
    	content.append("package JspServlet;" + "\r\n");
    	int p = file.indexOf("%>", file.lastIndexOf("<%@"));
    	p += 2;
    	int p4 = 0;
    	int p5 = 0;
    	int p6 = 0;
    	int p7 = 0;
    	//��jsp�ļ�����Ҫ������ļ�д���ļ�ͷ
    	while(true){
    		p4 = file.indexOf("import", p4);
    		if(p4 == -1 || p4 > p){
    			break;
    		} else{
    			p5 = file.indexOf("\"", p4);
    			p6 = file.indexOf("\"", p5+1);
    			p7 = file.indexOf(";", p4);
    			if(p5<p6){
    				String[] datas = file.substring(p5+1, p7).split(",");
    				for(int i=0; i<datas.length; i++){
    					content.append("import " + datas[i] + ";\r\n");
    				}
    			}
    		}
    		p4 = p6+1;
    	}
    	//���������Ҫ������
    	content.append("import javax.servlet.*;" + "\r\n"
    			+ "import java.io.IOException;" + "\r\n"
    			+ "import java.io.PrintWriter;" + "\r\n"
    			+ "public class " 
    			+ name + "_jsp" 
    			+ " implements Servlet {" + "\r\n"
    			+ "public void init(ServletConfig config) throws ServletException {}" + "\r\n"
    			+ "public void service(ServletRequest request, ServletResponse response)" + "\r\n"
    			+ "throws ServletException, IOException {" + "\r\n"
    			+ "PrintWriter out = response.getWriter();\r\n");
    	
    	//��Ӿ�����ļ�����
    	content.append("out.println(\"");
    	int p2 = 0;
    	int p3 = 0;
    	int j;
    	while(true){
    		//���html�ļ��Ĳ���
    		p2 = file.indexOf("<%=", p);
    		p3 = file.indexOf("<%", p);
    		if(p3 == -1){
    			String[] datas = file.substring(p).split("\n");
    			for(int i=0; i<datas.length; i++){
    				String[] datas2 = datas[i].split("\"");
    				for(j=0; j<datas2.length-1; j++){
    					content.append(datas2[j] + "\\\"");
    				}
    				content.append(datas2[j] + "\");\r\nout.println(\"");
    			}
    			content.append("\");");
    			break;
    		//���<% %>��<%= %> ����
    		} else{
    			String[] datas = file.substring(p,p3).split("\n");
    			for(int i=0; i<datas.length; i++){
    				String[] datas2 = datas[i].split("\"");
    				for(j=0; j<datas2.length-1; j++){
    					content.append(datas2[j] + "\\\"");
    				}
    				content.append(datas2[j] + "\");\r\nout.println(\"");
    			}
    			content.append("\");\r\n");
    			p = file.indexOf("%>", p3);
    			//����һ����ǩ��<%ʱ���
        		if(p2 == -1 || p3 < p2){
        			String[] datas12 = file.substring(p3+2, p).split("\n");
        			for(int i=0; i<datas12.length; i++){
        				content.append(datas12[i] + "\r\n");
        			}
        	    	content.append("out.println(\"");
        	    //����ǩ��<%=ʱ���
        		}else {
        			String[] datas12 = file.substring(p2+3, p).split("\n");
        			for(int i=0; i<datas12.length; i++){
        				content.append("out.println(" + datas12[i] + ");\r\n");
        			}
        	    	content.append("out.println(\"");
        		}	
        		p += 2;
    		}
    	}
    	//���������
    	content.append("}" + "\r\n"
    			+ "public void destroy() {}" + "\r\n"
    			+ "public String getServletInfo() {" + "\r\n"
    			+ "return null;" + "\r\n"
    			+ "}" + "\r\n"
    			+ "public ServletConfig getServletConfig() {" + "\r\n"
    			+ "return null;}}");
    	int c1, c2;
    	while(content.indexOf("${", 0) != -1){
    		c1 = content.indexOf("${", 0);
    		c2 = content.indexOf("}", c1);
    		String name2 = content.substring(c1+2, c2);
    		String value = (String)mav.getObject(name2);
    		if(value == null){
        		content.replace(c1, c2+1, "null");
    		}else{
    			content.replace(c1, c2+1, value);
    		}
    	}
    	//输出到文件
    	return outToFile(name, content);
    }
}
