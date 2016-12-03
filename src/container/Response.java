package container;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.OutputStream;  
import java.io.PrintWriter;  
import java.util.Locale;  
  
import javax.servlet.ServletOutputStream;  
import javax.servlet.ServletResponse;  
  
public class Response implements ServletResponse  
{
	//���岿�ֱ����Ա�ʹ��
    private static final int BUFFER_SIZE = 1024;  
    public final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "WebRoot";  
    Request request;  
    OutputStream output;  
    PrintWriter writer;  
  
    public Response(OutputStream output)  
    {  
        this.output = output;  
    }  
  
    public void setRequest(Request request)  
    {  
        this.request = request;  
    }  
  
    //���ڷ�����ҳ  
    public void sendStaticResource() throws IOException  
    {  
        byte[] bytes = new byte[BUFFER_SIZE];  
        FileInputStream fins = null;  
        try  
        {  
            //��·����Ӧ����ҳ  
            File file = new File(WEB_ROOT, request.getUri());  
            fins = new FileInputStream(file);  
            //��ȡ�ļ��е�����  
            int ch = fins.read(bytes, 0, BUFFER_SIZE);
            //������ѭ�����
            while (ch != -1)  
            {  
                output.write(bytes, 0, ch);  
                ch = fins.read(bytes, 0, BUFFER_SIZE);  
            }  
        }  
        catch (Exception e)  
        {  
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";  
            output.write(errorMessage.getBytes());  
        }  
        finally  
        {  
        	//�ر�����
            if (fins != null)  
                fins.close();  
        }  
    }

	@Override
	public void flushBuffer() throws IOException {
		// TODO �Զ����ɵķ������
		output.flush();
	}

	@Override
	public int getBufferSize() {
		// TODO �Զ����ɵķ������
		return 0;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public String getContentType() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public Locale getLocale() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		// TODO �Զ����ɵķ������
		writer = new PrintWriter(output, true);  
        return writer;  
	}

	@Override
	public boolean isCommitted() {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public void reset() {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void resetBuffer() {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void setBufferSize(int arg0) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void setCharacterEncoding(String arg0) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void setContentLength(int arg0) {
		// TODO �Զ����ɵķ������
		
	}

	public void setContentLengthLong(long arg0) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void setContentType(String arg0) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void setLocale(Locale arg0) {
		// TODO �Զ����ɵķ������
		
	}  
}