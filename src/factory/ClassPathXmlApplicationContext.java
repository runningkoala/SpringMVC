package factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import MVC.Controller;
import MVC.RequestMapping;
import bean.BeanDefinition;
import bean.BeanUtil;
import bean.PropertyValue;
import bean.PropertyValues;
import test.Autowired;
import test.Component;

public class ClassPathXmlApplicationContext extends AbstractBeanFactory {

	public ClassPathXmlApplicationContext(String[] xmlname)
	{
		String fileName = "";
		for(int i=0; i<xmlname.length;i++){
			fileName += xmlname[i];
		}
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document document = dbBuilder.parse(new FileInputStream(fileName));
			//�õ�xml�ļ������
            NodeList beanList = document.getElementsByTagName("bean");
            for(int i = 0 ; i < beanList.getLength(); i++)
            {
            	Node bean = beanList.item(i);
            	BeanDefinition beandef = new BeanDefinition();
            	String beanClassName = bean.getAttributes().getNamedItem("class").getNodeValue();
            	String beanName = bean.getAttributes().getNamedItem("id").getNodeValue();

        		beandef.setBeanClassName(beanClassName);
        		
        		Class<?> beanClass;
				try {
					beanClass = Class.forName(beanClassName);
					beandef.setBeanClass(beanClass);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		PropertyValues propertyValues = new PropertyValues();
        		
        		NodeList propertyList = bean.getChildNodes();
            	for(int j = 0 ; j < propertyList.getLength(); j++)
            	{
            		Node property = propertyList.item(j);
            		//����property�е�name��value����
            		if (property instanceof Element) {
        				Element ele = (Element) property;

        				String requestname = ele.getAttribute("request");
        				if(!requestname.equals("")){
        					String key = ele.getAttribute("key");
        					propertyValues.AddPropertyValue(new PropertyValue(requestname, key));
        				}else{
	        				String name = ele.getAttribute("name");
	        				Class<?> type;
							try {
								type = beandef.getBeanClass().getDeclaredField(name).getType();
								Object value = ele.getAttribute("value");
	
		        				if(type == Integer.class)
		        				{
		        					value = Integer.parseInt((String) value);
		        				}
		        				
		        				if(type == Object.class)
		        				{
		        					String aname=ele.getAttribute("value");
		        					if(!aname.equals("")){
		        						value = this.getBean(aname);
		        					}
		        				}
		        				
		        				if(type instanceof Class<?>){
		        					String className = ele.getAttribute("ref");
		        					if(!className.equals("")){
			        					Class<?> a = Class.forName(type.getName());
			        					//��ע��Ϊ@Component ʱ�����ൽ�����
			        					if(a.isAnnotationPresent(Component.class)){
				        					Object a2 = a.newInstance();
			        						BeanDefinition a3 = new BeanDefinition();
			        						a3.setBeanClass(a);
			        						a3.setBean(a2);
			        						a3.setBeanClassName(className);
			        		        		PropertyValues a4 = new PropertyValues();
			        						a3.setPropertyValues(a4);
			        						this.registerBeanDefinition(className, a3);
			        					}
		        						value = this.getBean(className);
		        					}
		        				}
		        				//����Ӧ������ӵ�PropertyValue�к�����propertyValues��
		        				propertyValues.AddPropertyValue(new PropertyValue(name,value));
							} catch (Exception e){
								e.printStackTrace();
							}
        				}
        			}
            	}
            	//������װ��BeanDefinition
            	beandef.setPropertyValues(propertyValues);
            	
            	this.registerBeanDefinition(beanName, beandef);
            }
            
		} catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	protected BeanDefinition GetCreatedBean(BeanDefinition beanDefinition) {
		
		try {
			// set BeanClass for BeanDefinition
			Class<?> beanClass = beanDefinition.getBeanClass();
			// set Bean Instance for BeanDefinition
			Object bean = beanClass.newInstance();	

			PropertyValues pvs = beanDefinition.getPropertyValues();
			if(beanClass.isAnnotationPresent(Controller.class)){
				for(Method method : beanClass.getDeclaredMethods()){
					if(method.isAnnotationPresent(RequestMapping.class)){
						this.registerRequestMapping(method.getAnnotation(RequestMapping.class).value(), method);
						LinkedList<String> names = new LinkedList<String>();
						for(PropertyValue pv : pvs.GetPropertyValues()){
							if(pv.getName().equals(method.getName())){
								names.add((String) pv.getValue());
							}
						}
						this.registerRequestMapping2(method.getAnnotation(RequestMapping.class).value(), names);
					}
				}
			}
			
			//��ȡ�ֶ����
			for (Field field : beanClass.getDeclaredFields()) {
				//�ж��ֶ��Ƿ�ע��
				if (field.isAnnotationPresent(Autowired.class)) {
					String fieldname = field.getName();
			        try {
			        	//����Ӧ��ݷ����ֶ�
			        	List<PropertyValue> fieldDefinitionList = beanDefinition.getPropertyValues().GetPropertyValues();
						for(PropertyValue propertyValue: fieldDefinitionList)
						{
							if(propertyValue.getName().equals(fieldname)){
					            field.set(bean, this.getBean(propertyValue.getName()));
							}
						}
			        } catch (IllegalAccessException e) {
			        	e.printStackTrace();
			        }
			    }
			}
			//ִ���Ѵ���set�ķ��亯��
			List<PropertyValue> fieldDefinitionList = beanDefinition.getPropertyValues().GetPropertyValues();
			for(PropertyValue propertyValue: fieldDefinitionList)
			{
				BeanUtil.invokeSetterMethod(bean, propertyValue.getName(), propertyValue.getValue());
			}
			
			beanDefinition.setBean(bean);
			
			return beanDefinition;
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
