package container;
import java.io.IOException;  
  
public class StaticResourceProcessor  
{  
    public void process(Request request, Response response)  
    {  
        try  
        {  
        	//������Ӧ����ҳ
            response.sendStaticResource();
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
    }  
}  