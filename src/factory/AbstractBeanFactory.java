package factory;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bean.BeanDefinition;


public abstract class AbstractBeanFactory implements ApplicationContext{
	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
	private Map<String, Method> requestMapping = new ConcurrentHashMap<String, Method>();
	private Map<String, LinkedList<String>> requestMapping2 = new ConcurrentHashMap<String, LinkedList<String>>();

	public Object getBean(String beanName)
	{
		return this.beanDefinitionMap.get(beanName).getBean();
	}
	
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
	{
		beanDefinition = GetCreatedBean(beanDefinition);
		this.beanDefinitionMap.put(beanName, beanDefinition);
	}
	
	public void registerRequestMapping(String requestName, Method method)
	{
		this.requestMapping.put(requestName, method);
	}
	
	public Map<String, Method> getRequestMapping()
	{
		return requestMapping;
	}
	
	public void registerRequestMapping2(String requestName, LinkedList<String> paras)
	{
		this.requestMapping2.put(requestName, paras);
	}
	
	public Map<String, LinkedList<String>> getRequestMapping2()
	{
		return requestMapping2;
	}
	
	protected abstract BeanDefinition GetCreatedBean(BeanDefinition beanDefinition);
}
