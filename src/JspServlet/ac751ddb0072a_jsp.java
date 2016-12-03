package JspServlet;
import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
public class ac751ddb0072a_jsp implements Servlet {
public void init(ServletConfig config) throws ServletException {}
public void service(ServletRequest request, ServletResponse response)
throws ServletException, IOException {
PrintWriter out = response.getWriter();
out.println("");
out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
out.println("<html>");
out.println("<head>");
out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
out.println("<title>Insert title here</title>");
out.println("</head>");
out.println("<body>");
out.println("<h1>这就是是一个简单的测试，就是根据名字来找到文件！</h1>");
out.println("用户名：hahaha");
out.println("密码：123");
out.println("</body>");
out.println("</html>");
out.println("");}
public void destroy() {}
public String getServletInfo() {
return null;
}
public ServletConfig getServletConfig() {
return null;}}